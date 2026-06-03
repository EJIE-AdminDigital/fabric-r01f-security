package r01f.services.security;


import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.user.CRUDServicesForUser;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.user.User;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreCRUDServicesForModelObjectBase;
import r01f.types.contact.EMail;
import r01f.types.contact.Phone;

@Accessors(prefix="_")
public abstract class CoreCRUDServicesImplForUser<U extends User>
			  extends CoreCRUDServicesForModelObjectBase<UserOID,U>
		   implements CRUDServicesForUser<U>,
					  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreCRUDServicesImplForUser(final ServicesCoreModuleConfig coreCfg,
									   final Marshaller modelObjectsMarshaller,
									   final EventBus eventBus,
									   final Provider<EntityManager> entityManagerProvider) {
		super(coreCfg,
			  modelObjectsMarshaller,
			  eventBus,
			  entityManagerProvider);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EXTENSION  METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Transactional
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<U> loadBy(final SecurityContext securityContext,
								final SecurityProviderID securityProviderId,final LoginID loginId) {
		CRUDResult<U> outResult = this.forSecurityContext(securityContext)
									  .createDelegateAs(CRUDServicesForUser.class)
									  .loadBy(securityContext,
											  securityProviderId,loginId);
		return outResult;
	}
	@Transactional
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<U> loadBy(final SecurityContext securityContext,
								final EMail email) {
		CRUDResult<U> outResult = this.forSecurityContext(securityContext)
									  .createDelegateAs(CRUDServicesForUser.class)
									  .loadBy(securityContext,
											  email);
		return outResult;
	}
	@Transactional
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<U> loadBy(final SecurityContext securityContext,
								final Phone phone) {
		CRUDResult<U> outResult = this.forSecurityContext(securityContext)
									  .createDelegateAs(CRUDServicesForUser.class)
									  .loadBy(securityContext,
											  phone);
		return outResult;
	}
}
