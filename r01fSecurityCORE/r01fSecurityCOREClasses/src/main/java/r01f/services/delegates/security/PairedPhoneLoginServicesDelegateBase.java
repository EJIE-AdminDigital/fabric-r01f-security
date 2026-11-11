package r01f.services.delegates.security;



import java.util.LinkedHashMap;
import java.util.Map;

import com.google.common.hash.Hashing;

import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.api.interfaces.security.login.PairedPhoneLoginServices;
import r01f.api.interfaces.security.login.entry.CRUDServicesForLoginEntry;
import r01f.api.interfaces.security.principal.CRUDServicesForPrincipal;
import r01f.core.services.notifier.NotifierPushMessage;
import r01f.db.crud.security.DBCRUDForLoginBase;
import r01f.eventbus.EventBus;
import r01f.internal.notifier.security.user.pairedphone.PairedPhoneLoginConsentMessage;
import r01f.internal.notifier.security.user.pairedphone.PairedPhoneLoginConsentMessageData;
import r01f.internal.notifier.security.user.pairedphone.PairedPhoneLoginConsentNotifier;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.PersistenceRequestedOperation;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.Login;
import r01f.model.security.login.data.pairedphone.LoginDataForPairedPhone;
import r01f.model.security.login.pairedphone.messages.login.PairedPhoneConsentAction;
import r01f.model.security.login.pairedphone.messages.login.PairedPhoneLoginConsentRequest;
import r01f.model.security.login.pairedphone.messages.login.PairedPhoneLoginConsentResponseFromPairedPhone;
import r01f.model.security.login.pairedphone.messages.login.PairedPhoneLoginResponse;
import r01f.model.security.login.pairedphone.messages.login.PairedPhoneLoginResult;
import r01f.model.security.operation.IsSecurityOperationResult;
import r01f.model.security.operation.SecurityOperationExecResultBuilder;
import r01f.model.services.COREServiceErrorTypes;
import r01f.model.services.COREServiceMethod;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityIDs.SecurityToken;
import r01f.oids.SecurityOIDs.LoginOID;
import r01f.oids.SecurityOIDs.PairedPhoneLoginConsentOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.PersistenceServicesDelegateForModelObjectBase;
import r01f.types.contact.Phone;

@Slf4j
public abstract class PairedPhoneLoginServicesDelegateBase<P extends IsSecurityPrincipal>
			  extends PersistenceServicesDelegateForModelObjectBase<LoginOID,Login>
		   implements PairedPhoneLoginServices {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final Marshaller _marshaller;
	
	// Crud services
	protected final CRUDServicesForPrincipal<P> _principalCrud;
	protected final CRUDServicesForLogin _loginCrud;
	// Notifier services
	protected final PairedPhoneLoginConsentNotifier _loginConsentNotifier;		
/////////////////////////////////////////////////////////////////////////////////////////
// 	CONSTRUCTOR   & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public PairedPhoneLoginServicesDelegateBase(final ServicesCoreModuleConfig coreCfg,
												 final Marshaller marshaller,
												 final EventBus eventBus,
												 // Crud services
										 	     final CRUDServicesForPrincipal<P> principalCrud,
										 	     final CRUDServicesForLogin loginCrud,
										 	     // Notifier services
										 	     final PairedPhoneLoginConsentNotifier loginConsentNotifier) {
		super(coreCfg,
			  Login.class,
			  null, 	// no CRUD services for Login
			  eventBus);
		_marshaller = marshaller;
		
		_principalCrud = principalCrud;
		_loginCrud = loginCrud;
		_loginConsentNotifier = loginConsentNotifier;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	ABSTRACT METHODS
/////////////////////////////////////////////////////////////////////////////////////////	
	// BEWARE!!! Use the DAO layer (the deepest one) since if the service layer was used (as usual),
	//			 the password / password hash will NOT be present at the returned object 
	//			 because the password is REMOVED at the CRUD delegate layer
	protected abstract <CRUD extends DBCRUDForLoginBase<?>> CRUD _createDBCRUDServicesForLogin();
	
	protected abstract CRUDServicesForLoginEntry _createCRUDServicesForLoginEntry();
	
	protected abstract CRUDServicesForPrincipal<P> _createCRUDServicesForPrincipal();
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public IsSecurityOperationResult<PairedPhoneLoginConsentRequest> loadConsentRequest(final SecurityContext securityContext,
                                                   										final Phone phone) {
		// [1] - Load the [login]				
		CRUDResult<Login> loginLoad = _loginCrud.loadBy(securityContext,
														IdentityProvider.FABRIC,AuthenticationMethod.PAIRED_PHONE,LoginID.fromPhone(phone));
		if (loginLoad.hasFailed()) return SecurityOperationExecResultBuilder.notExecuted(COREServiceMethod.current())
											 		 						.because(loginLoad.asCOREServiceMethodExecError());
		Login login = loginLoad.getOrThrow();
		
		// [2] - Get the [consent request] from the login
		LoginDataForPairedPhone loginData = login.getDataAsForPairedPhone();
		if (loginData == null
		 || loginData.getLoginConsentRequest() == null) return SecurityOperationExecResultBuilder.notExecuted(COREServiceMethod.current())
																	 							 .because(COREServiceErrorTypes.ILLEGAL_STATUS,
																	 									  "No paired phone login data found for phone " + phone);
		// [3] - Return
		return SecurityOperationExecResultBuilder.executed(PersistenceRequestedOperation.LOAD.getCOREServiceMethod())
												 .returning(loginData.getLoginConsentRequest());
	}
	@Override
	public IsSecurityOperationResult<PairedPhoneLoginConsentRequest> createConsentRequestFor(final SecurityContext securityContext,
										        			 							  	 final Phone phone) {
		// [0] - Create new request with a security token.
		PairedPhoneLoginConsentRequest req = new PairedPhoneLoginConsentRequest(PairedPhoneLoginConsentOID.supply());
		req.setSecurityToken(SecurityToken.from(Hashing.murmur3_32_fixed()
													   .hashBytes(req.getOid().toString()
																			  .getBytes())
													   .toString()));
		// [1] - Load the [login]				
		CRUDResult<Login> loginLoad = _loginCrud.loadBy(securityContext,
														IdentityProvider.FABRIC,AuthenticationMethod.PAIRED_PHONE,LoginID.fromPhone(phone));
		if (loginLoad.hasFailed()) return SecurityOperationExecResultBuilder.notExecuted(COREServiceMethod.current())
											 		 						.because(loginLoad.asCOREServiceMethodExecError());
		Login login = loginLoad.getOrThrow();
		
		// [2] - Send push notification to the paired phone
		PairedPhoneLoginConsentMessage consentNotification = new PairedPhoneLoginConsentMessage();
		try {
			PairedPhoneLoginConsentMessageData consentMessageData = new PairedPhoneLoginConsentMessageData(req.getOid(),
																  										   phone);
			Map<String,String> keyValueData = new LinkedHashMap<>();
			keyValueData.put("loginConsentRequest", 
							 _marshaller.forWriting()
							 			.toJson(consentMessageData));
			consentNotification.setSecurityTokenForPush(req.getSecurityToken());			
			consentNotification.setPushMesagge(new NotifierPushMessage("Login Consent",
																	   "Login Consent",
																	   "Default",
																	   keyValueData));
			 // 1. This way produces an async post.
			 //    this.getEventBus().post(consentNotification);		
			 // 2. This way produces an sync post.
			_loginConsentNotifier.sendNotification(consentNotification);		
		} catch(Throwable th) {	
			log.error("error sending push notification {}",
					  th.getLocalizedMessage(),th);
			return SecurityOperationExecResultBuilder.notExecuted(COREServiceMethod.fromString("consent.request"))
													 .because(COREServiceErrorTypes.SERVER_ERROR,
															  th.getLocalizedMessage());
		}
		// [3] - Update the login
		login.setData(new LoginDataForPairedPhone(req));
		CRUDResult<Login> loginUpdate = _loginCrud.update(securityContext,
														  login);
		if (loginUpdate.hasFailed()) SecurityOperationExecResultBuilder.notExecuted(COREServiceMethod.current())
																	   .because(loginUpdate.asCOREServiceMethodExecError());
		
		// [4] - Return the consent request
		return SecurityOperationExecResultBuilder.executed(COREServiceMethod.fromString("consent.request"))
												 .returning(req);
	}
	@Override
	public IsSecurityOperationResult<PairedPhoneLoginResponse> receiveConsentResponseFromPairedPhone(final SecurityContext securityContext,
		                                          													 final PairedPhoneLoginConsentResponseFromPairedPhone response) {
		// [0] - Log Response
		log.info("received response : {}" ,
				 _marshaller.forWriting()
				 			.toJson(response));
		// [1] - Load  db login consent.				
		CRUDResult<Login> loginLoad = _loginCrud.loadBy(securityContext,
														IdentityProvider.FABRIC,AuthenticationMethod.PAIRED_PHONE,response.getSecurityPrincipalOid());
		if (loginLoad.hasFailed()) return SecurityOperationExecResultBuilder.notExecuted(COREServiceMethod.current())
													 						.because(loginLoad.asCOREServiceMethodExecError());
		Login login = loginLoad.getOrThrow();
		LoginDataForPairedPhone loginData = login.getDataAsForPairedPhone();
		
		// [2] - Get the result
		PairedPhoneLoginResult outResult = PairedPhoneLoginResult.CONSENT_NOT_GRANTED;
		
		if (response.getConsentAction().is(PairedPhoneConsentAction.DENIED)) {
			//...denied
			outResult = PairedPhoneLoginResult.CONSENT_NOT_GRANTED;
		} 
		else if (response.getConsentAction().is(PairedPhoneConsentAction.ACCEPTED)) {
			//..accepted ( ..but check security token )			
			SecurityToken responseSecurityToken  = response.getSecurityToken();
			SecurityToken requestSecurityToken = loginData.getLoginConsentRequest()
														  .getSecurityToken();
			if (requestSecurityToken != null) {
				// ..has security token
				log.info("response security token {}",responseSecurityToken);
				log.info("requestSecurityToken security token {}",requestSecurityToken);
				if (responseSecurityToken.equals(requestSecurityToken)) {
					 outResult = PairedPhoneLoginResult.CONSENT_GRANTED;
				} else {
					 outResult = PairedPhoneLoginResult.CONSENT_NOT_GRANTED;
				}
			} 
		}
		
		// [3] - Update the login: remove the consent
		login.setData(null);	// remove the consent request
		CRUDResult<Login> loginUpdate = _loginCrud.update(securityContext,
														  login);		
		if (loginUpdate.hasFailed()) return SecurityOperationExecResultBuilder.notExecuted(COREServiceMethod.current())
											  								  .because(loginUpdate.asCOREServiceMethodExecError());
		
		// [4] - Return the consent request
		PairedPhoneLoginResponse outResponse = new PairedPhoneLoginResponse(outResult);
		outResponse.setLoginConsentOid(loginData.getLoginConsentRequest()
												.getOid());
		outResponse.setSecurityPrincipalOid(login.getSecurityPrincipalOid());
		outResponse.setSecurityPrincipalId(login.getSecurityPrincipalId());
		return SecurityOperationExecResultBuilder.executed(COREServiceMethod.current())
												 .returning(outResponse);	
	}
}
