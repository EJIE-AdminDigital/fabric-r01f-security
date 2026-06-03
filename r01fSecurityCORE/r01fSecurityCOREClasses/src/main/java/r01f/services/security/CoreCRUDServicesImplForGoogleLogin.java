package r01f.services.security;


import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.login.google.CRUDServicesForGoogleLogin;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.login.google.GoogleLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreCRUDServicesForModelObjectBase;
import r01f.types.contact.EMail;

@Accessors(prefix="_")
public abstract class CoreCRUDServicesImplForGoogleLogin<L extends GoogleLogin>
			  extends CoreCRUDServicesForModelObjectBase<LoginOID,L>
		   implements CRUDServicesForGoogleLogin<L>,
		   			  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreCRUDServicesImplForGoogleLogin(final ServicesCoreModuleConfig coreCfg,
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
	@Transactional
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
								final UserOID userOid) {
		return this.forSecurityContext(securityContext)
				.createDelegateAs(CRUDServicesForGoogleLogin.class)
					.loadBy(securityContext,
							userOid);
	}
	@Transactional
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
								final LoginID login) {
		return this.forSecurityContext(securityContext)
				.createDelegateAs(CRUDServicesForGoogleLogin.class)
					.loadBy(securityContext,
							login);
	}
	@Transactional
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
								final EMail email) {
		return this.forSecurityContext(securityContext)
				.createDelegateAs(CRUDServicesForGoogleLogin.class)
					.loadBy(securityContext,
							email);
	}
}

