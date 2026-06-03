package r01f.services.security;


import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.login.entry.CRUDServicesForUserLoginEntry;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CRUDResult;
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
import r01f.services.core.persistence.CoreCRUDServicesForModelObjectBase;

@Accessors(prefix="_")
public abstract class CoreCRUDServicesImplForUserLoginEntry<E extends UserLoginEntry>
			  extends CoreCRUDServicesForModelObjectBase<UserLoginEntryOID,E>
		   implements CRUDServicesForUserLoginEntry<E>,
					  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreCRUDServicesImplForUserLoginEntry(final ServicesCoreModuleConfig coreCfg,
												 final Marshaller modelObjectsMarshaller,
												 final EventBus eventBus,
												 final Provider<EntityManager> entityManagerProvider) {
		super(coreCfg,
			  modelObjectsMarshaller,
			  eventBus,
			  entityManagerProvider);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<E> loadLastLoginEntryFor(final SecurityContext securityContext,
											   final SecurityProviderID providerId,final LoginID loginId,
											   final UserLoginEntryID entryId) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(CRUDServicesForUserLoginEntry.class)
				   .loadLastLoginEntryFor(securityContext,
						   				  providerId,loginId,
						   				  entryId);
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	PASSWORD RESET
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<E> loadForPasswordReset(final SecurityContext securityContext,
											  final UserOID userOid,
											  final SecurityToken token) {
		return this.forSecurityContext(securityContext)
				.createDelegateAs(CRUDServicesForUserLoginEntry.class)
					.loadForPasswordReset(securityContext,
										  userOid,
										  token);
	}
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<E> updateAfterPasswordReset(final SecurityContext securityContext,
												  final UserOID userOid,
												  final SecurityToken token) {
		return this.forSecurityContext(securityContext)
				.createDelegateAs(CRUDServicesForUserLoginEntry.class)
					.updateAfterPasswordReset(securityContext,
											  userOid,
											  token);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	IMPERSONATION
/////////////////////////////////////////////////////////////////////////////////////////
	@Transactional
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<E> pushImpersonationLogin(final SecurityContext securityContext,
												final UserOID impersonatingUserOid,final UserOID impersonatedUserOid,
												final SecurityToken token) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(CRUDServicesForUserLoginEntry.class)
				   .pushImpersonationLogin(securityContext,
									   	   impersonatingUserOid,impersonatedUserOid,
									   	   token);
	}
	@Transactional
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<E> popImpersonationLogin(final SecurityContext securityContext,
											   final UserLoginEntryOID loginEntryOid) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(CRUDServicesForUserLoginEntry.class)
				   .popImpersonationLogin(securityContext,
						   				  loginEntryOid);
	}
}