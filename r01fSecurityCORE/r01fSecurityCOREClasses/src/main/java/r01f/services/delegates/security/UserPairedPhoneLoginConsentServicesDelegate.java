package r01f.services.delegates.security;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import com.google.common.hash.Hashing;

import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.pairedphone.CRUDServicesForPairedPhoneLoginConsent;
import r01f.api.interfaces.security.login.pairedphone.PairedPhoneLoginConsentServices;
import r01f.api.interfaces.security.user.CRUDServicesForUser;
import r01f.core.services.notifier.NotifierPushMessage;
import r01f.eventbus.EventBus;
import r01f.internal.notifier.security.user.pairedphone.PairedPhoneLoginConsentMessage;
import r01f.internal.notifier.security.user.pairedphone.PairedPhoneLoginConsentMessageData;
import r01f.internal.notifier.security.user.pairedphone.PairedPhoneLoginConsentNotifier;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.PersistenceRequestedOperation;
import r01f.model.security.login.pairedphone.PairedPhoneLoginConsentBase;
import r01f.model.security.login.pairedphone.PairedPhoneLoginConsentRequest;
import r01f.model.security.login.pairedphone.PairedPhoneLoginConsentResponse;
import r01f.model.security.login.pairedphone.PairedPhoneLoginConsentStatus;
import r01f.model.security.operation.SecurityOperationExecResultBuilder;
import r01f.model.security.operation.SecurityOperationResult;
import r01f.model.security.user.UserBase;
import r01f.model.services.COREServiceErrorTypes;
import r01f.model.services.COREServiceMethod;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.SecurityToken;
import r01f.securitycontext.SecurityOIDs.PairedPhoneLoginConsentOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.PersistenceServicesDelegateForModelObjectBase;
import r01f.types.contact.Phone;

@Slf4j
public abstract class UserPairedPhoneLoginConsentServicesDelegate<U extends UserBase<U>,
																  C extends PairedPhoneLoginConsentBase>
			  extends PersistenceServicesDelegateForModelObjectBase<PairedPhoneLoginConsentOID,C>
		   implements PairedPhoneLoginConsentServices<C> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	final CRUDServicesForPairedPhoneLoginConsent<C> _crudForConsent; 
	final PairedPhoneLoginConsentNotifier _loginConsentNotifier;	
	final CRUDServicesForUser<U> _crudServicesForUser;
	final Marshaller _marshaller;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public UserPairedPhoneLoginConsentServicesDelegate(final ServicesCoreModuleConfig coreCfg,
													   final Class<C> modelObjectType,
													   final Marshaller marshaller, 
													   final EventBus eventBus,
													   final CRUDServicesForUser<U> crudServicesForUser,													 
													   final CRUDServicesForPairedPhoneLoginConsent<C> crudForConsent,
													   final PairedPhoneLoginConsentNotifier loginConsentNotifier) {
		super(coreCfg,
			  modelObjectType,
			  crudForConsent,				 
			  eventBus);
		_crudServicesForUser = crudServicesForUser;
		_crudForConsent = crudForConsent;
		_loginConsentNotifier =  loginConsentNotifier;	
		_marshaller = marshaller;		
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS TO IMPLEMENT
/////////////////////////////////////////////////////////////////////////////////////////	
	public  abstract C _buildFrom(final SecurityContext securityContext,  final PairedPhoneLoginConsentRequest req);
	public  abstract SecurityToken _loadFrom(final SecurityContext securityContext,
											 final Phone phone);
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public SecurityOperationResult<C> request(final SecurityContext securityContext,
											  final PairedPhoneLoginConsentRequest inputReq) {
		
		// 0. Create new request completed with a security token.
		PairedPhoneLoginConsentRequest req = _buildWithSecurityToken(inputReq);	
		
		// 1. Create at db login consent.				
		C consent =  _buildFrom(securityContext,
							    req);			
		log.info("Create login consent with oid {}",
				 consent.getOid() );
		CRUDResult<C> crudResultCreatedConsent = _crudForConsent.create(securityContext,
																		consent);
		if (crudResultCreatedConsent.hasFailed()) {		
			return SecurityOperationExecResultBuilder.notExecuted(COREServiceMethod.fromString("consent.request"))
											 .because(COREServiceErrorTypes.SERVER_ERROR,
													  crudResultCreatedConsent.getDetailedMessage());
		}															
		C createdConsent = crudResultCreatedConsent.getOrThrow();
		SecurityToken token = _loadFrom(securityContext,
										req.getPhone());
		PairedPhoneLoginConsentMessage consentNotification = new PairedPhoneLoginConsentMessage();
		try {
			
			PairedPhoneLoginConsentMessageData consentMessageData = PairedPhoneLoginConsentMessageData.from(createdConsent);
			Map<String,String> keyValueData = new LinkedHashMap<>();
			keyValueData.put("loginConsentRequest", 
							 _marshaller.forWriting()
							 			.toJson(consentMessageData));
			consentNotification.setSecurityTokenForPush(token);			
			consentNotification.setPushMesagge(new NotifierPushMessage("Login Consent",
																	   "Login Consent",
																	   "Default",
																	   keyValueData));
			 // 1. This way produces an async post.
			 //    this.getEventBus().post(consentNotification);		
			 // 2. This way produces an sync post.
			_loginConsentNotifier.sendNotification(consentNotification);		
		} catch (final  Throwable th) {	
			log.error("error sending push notification {}",
					  th.getLocalizedMessage(),th);
			return SecurityOperationExecResultBuilder.notExecuted(COREServiceMethod.fromString("consent.request"))
													 .because(COREServiceErrorTypes.SERVER_ERROR, th.getLocalizedMessage());
													
		}
		// 3. Update consent status to requested.
		createdConsent.setStatus(PairedPhoneLoginConsentStatus.REQUESTED);
		CRUDResult<C> crudResultUpdatedConsent =  _crudForConsent.update(securityContext,
																		 createdConsent);
		
		if (crudResultUpdatedConsent.hasFailed()) {
			SecurityOperationExecResultBuilder.notExecuted(COREServiceMethod.fromString("consent.request"))
													 .because(COREServiceErrorTypes.SERVER_ERROR,
															  crudResultUpdatedConsent.getDetailedMessage());
		}															
		return SecurityOperationExecResultBuilder.executed(COREServiceMethod.fromString("consent.request"))
												 .returning(crudResultUpdatedConsent.getOrThrow());
	}
	@Override
	public SecurityOperationResult<C> response(final SecurityContext securityContext,
											   final PairedPhoneLoginConsentResponse response) {
		// 0. Log Response
		log.info("received response : {}" ,
				 _marshaller.forWriting()
				 			.toJson(response));
		// 1. Load  db login consent.				
		PairedPhoneLoginConsentOID consentOid = response.getLoginConsentOid();
		
		log.info("Loading login consent with oid {}",
				 consentOid);			
		CRUDResult<C> crudResultLoadedConsent = _crudForConsent.load(securityContext,
																     consentOid);
		if (crudResultLoadedConsent.hasFailed()) {		
			log.error("...not found any consent with oid {}",consentOid);
			return SecurityOperationExecResultBuilder.notExecuted(COREServiceMethod.fromString("consent.response"))
													 .because(COREServiceErrorTypes.BAD_CLIENT_REQUEST,
															  crudResultLoadedConsent.getDetailedMessage());
			  
		
		}	
		C loginConsent = crudResultLoadedConsent.getOrThrow();		
		//2. Load user
		UserOID userOid = response.getUserOid();		
		log.info("Loading paired user {}",
				 userOid);			
		CRUDResult<U> crudResultForUser = _crudServicesForUser.load(securityContext,
																	userOid);
		
		if (crudResultForUser.hasFailed()) {
			log.error(" ...not found any user with userOid {}",userOid);
			return SecurityOperationExecResultBuilder.notExecuted(COREServiceMethod.fromString("consent.response"))
													 .because(COREServiceErrorTypes.BAD_CLIENT_REQUEST,
															  crudResultForUser.getDetailedMessage());
		}		
		// 3. Update consent status 
		if (! response.getAccepted()) {
			//...denied
			 loginConsent.setStatus(  PairedPhoneLoginConsentStatus.DENIED);
		} else {
			//..accepted ( ..but check security token )			
			SecurityToken responseSecurityToken  = response.getSecurityToken();
			SecurityToken requestSecurityToken = loginConsent.getRequest()
															 .getSecurityToken();
			if (requestSecurityToken != null) {
				// ..has security token
				log.info(" response security token {}",responseSecurityToken);
				log.info(" requestSecurityToken security token {}",requestSecurityToken);
				if (responseSecurityToken.equals(requestSecurityToken)) {
					 loginConsent.setStatus( PairedPhoneLoginConsentStatus.ACCEPTED);
				} else {
					 loginConsent.setStatus(PairedPhoneLoginConsentStatus.ERROR);
				}
			} else {
				log.warn("... Has no security token , so there is nothing to check.");
				loginConsent.setStatus(  PairedPhoneLoginConsentStatus.ACCEPTED);
			}
		}
		loginConsent.setResponse(response);		
		CRUDResult<C> crudResultUpdatedConsent =  _crudForConsent.update(securityContext, loginConsent);		
		if (crudResultUpdatedConsent.hasFailed()) {
			SecurityOperationExecResultBuilder.notExecuted(COREServiceMethod.fromString("consent.response"))
											  .because(COREServiceErrorTypes.SERVER_ERROR,
													   crudResultUpdatedConsent.getDetailedMessage());
		}
		return SecurityOperationExecResultBuilder.executed(COREServiceMethod.fromString("consent.response"))
												 .returning(crudResultUpdatedConsent.getOrThrow());	
	}	
	@Override
	public SecurityOperationResult<C> loadConsent(final SecurityContext securityContext, 
												  final PairedPhoneLoginConsentOID oid) {
		log.info("Loading login consent with oid {}",
				 oid);			
		CRUDResult<C> crudResultLoadedConsent = _crudForConsent.load(securityContext,
																	 oid);
		
		if (crudResultLoadedConsent.hasFailed()) {
			SecurityOperationExecResultBuilder.notExecuted(PersistenceRequestedOperation.LOAD.getCOREServiceMethod())
													 .because(COREServiceErrorTypes.SERVER_ERROR,
															 crudResultLoadedConsent.getDetailedMessage());
		}
		return SecurityOperationExecResultBuilder.executed(PersistenceRequestedOperation.LOAD.getCOREServiceMethod())
												 .returning(crudResultLoadedConsent.getOrThrow());	
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@SuppressWarnings("static-method")
	protected  PairedPhoneLoginConsentRequest _buildWithSecurityToken(final PairedPhoneLoginConsentRequest inputReq) {
	  PairedPhoneLoginConsentRequest req = PairedPhoneLoginConsentRequest.from(inputReq);
	  req.setSecurityToken(SecurityToken.from(Hashing.murmur3_32_fixed().hashBytes(UUID.randomUUID().toString().getBytes()).toString()));
	  return req;	  
	}
}
