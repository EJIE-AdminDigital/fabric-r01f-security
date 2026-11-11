package r01f.services.security;


import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.Login;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityIDs.SecurityPrincipalID;
import r01f.oids.SecurityOIDs.LoginOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreCRUDServicesForModelObjectBase;

@Accessors(prefix="_")
public abstract class CoreCRUDServicesImplForLoginBase
			  extends CoreCRUDServicesForModelObjectBase<LoginOID,Login>
		   implements CRUDServicesForLogin,
					  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreCRUDServicesImplForLoginBase(final ServicesCoreModuleConfig coreCfg,
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
	@Override 
	public CRUDResult<Login> loadBy(final SecurityContext securityContext,
									final IdentityProvider identityProvider,final AuthenticationMethod authnMethod,final SecurityPrincipalOID principalOid) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(CRUDServicesForLogin.class)
				   .loadBy(securityContext,
						   identityProvider,authnMethod,principalOid);
	}
	@Transactional
	@Override 
	public CRUDResult<Login> loadBy(final SecurityContext securityContext,
									final IdentityProvider identityProvider,final AuthenticationMethod authnMethod,final SecurityPrincipalID principalId) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(CRUDServicesForLogin.class)
				   .loadBy(securityContext,
						   identityProvider,authnMethod,principalId);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Transactional
	@Override 
	public CRUDResult<Login> loadBy(final SecurityContext securityContext,
									final IdentityProvider identityProvider,final AuthenticationMethod authnMethod,final LoginID login) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(CRUDServicesForLogin.class)
				   .loadBy(securityContext,
						   identityProvider,authnMethod,login);
	}
}