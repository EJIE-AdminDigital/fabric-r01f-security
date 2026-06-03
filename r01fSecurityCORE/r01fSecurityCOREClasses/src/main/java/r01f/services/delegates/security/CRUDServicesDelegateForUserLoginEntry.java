package r01f.services.delegates.security;



import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.entry.CRUDServicesForUserLoginEntry;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.CRUDResultBuilder;
import r01f.model.security.login.entry.UserLoginEntry;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.securitycontext.SecurityIDS.SecurityToken;
import r01f.securitycontext.SecurityIDS.UserLoginEntryID;
import r01f.securitycontext.SecurityOIDs.UserLoginEntryOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.CRUDServicesForModelObjectDelegateBase;

@Slf4j
public abstract class CRUDServicesDelegateForUserLoginEntry<E extends UserLoginEntry>
			  extends CRUDServicesForModelObjectDelegateBase<UserLoginEntryOID,E>
		   implements CRUDServicesForUserLoginEntry<E> {

/////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTOR   & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public CRUDServicesDelegateForUserLoginEntry(final ServicesCoreModuleConfig coreCfg,
												 final Class<E> modelObjectType,
												 final CRUDServicesForUserLoginEntry<E> dbCrud,
												 final Marshaller marshaller,
												 final EventBus eventBus) {
		super(coreCfg,
			  modelObjectType,
			  dbCrud,
			  eventBus);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<E> loadLastLoginEntryFor(final SecurityContext securityContext,
											   final SecurityProviderID providerId,final LoginID loginId,
											   final UserLoginEntryID entryId) {
		if (loginId == null) throw new IllegalArgumentException("The loginId is mandatory to load last [login entry]");
		UserLoginEntryID theEntryId = entryId != null ? entryId
													  : UserLoginEntryID.LOGIN_OK;	// login ok by default
		return this.getServiceImplAs(CRUDServicesForUserLoginEntry.class)
				   .loadLastLoginEntryFor(securityContext,
						   				  providerId,loginId,
						   				  theEntryId);
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	PASSWORD
///////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<E> loadForPasswordReset(final SecurityContext securityContext,
											  final UserOID userOid,
											  final SecurityToken token) {
		if (userOid == null || token == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientBadRequest("The userOid and token MUST not be null")
									.build();
		}
		CRUDResult<E> outResult = this.getServiceImplAs(CRUDServicesForUserLoginEntry.class)
									  .loadForPasswordReset(securityContext,
															userOid, token);
		return outResult;
	}
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<E> updateAfterPasswordReset(final SecurityContext securityContext,
												  final UserOID userOid,
												  final SecurityToken token) {
		if (userOid == null || token == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientBadRequest("The entity userCode and token MUST not be null")
									.build();
		}
		CRUDResult<E> outResult = this.getServiceImplAs(CRUDServicesForUserLoginEntry.class)
									  .updateAfterPasswordReset(securityContext,
																 userOid, token);
		return outResult;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  IMPESONATION
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<E> pushImpersonationLogin(final SecurityContext securityContext,
												final UserOID impersonatingUserOid,final UserOID impersonatedUserOid,
												final SecurityToken securityToken) {
		log.info(" Impersonating  (the user requesting the impersonation ) User OID {}" , impersonatingUserOid);
		log.info(" Impersonated   (the user being impersonated)User OID {}" , impersonatedUserOid);
		if (impersonatingUserOid == null || impersonatedUserOid == null) {
			throw new IllegalArgumentException("Both [impersonating user oid] and [impersonated user oid] are required!");
		}
		if (securityToken == null) {
			throw new IllegalArgumentException("Security token is required!");
		}

		return this.getServiceImplAs(CRUDServicesForUserLoginEntry.class)
				   .pushImpersonationLogin(securityContext,
									   	   impersonatingUserOid,impersonatedUserOid,
									   	   securityToken);
	}
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<E> popImpersonationLogin(final SecurityContext securityContext,
											   final UserLoginEntryOID loginEntryOid) {
		if (loginEntryOid == null) throw new IllegalArgumentException("The [login entry oid] is mandatory!!");

		return this.getServiceImplAs(CRUDServicesForUserLoginEntry.class)
				   .popImpersonationLogin(securityContext,
						   				  loginEntryOid);
	}
}
