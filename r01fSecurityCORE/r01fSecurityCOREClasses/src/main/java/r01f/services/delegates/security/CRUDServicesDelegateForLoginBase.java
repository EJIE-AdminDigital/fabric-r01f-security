package r01f.services.delegates.security;



import lombok.extern.slf4j.Slf4j;
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
import r01f.services.core.delegates.persistence.CRUDServicesForModelObjectDelegateBase;

@Slf4j
public abstract class CRUDServicesDelegateForLoginBase
			  extends CRUDServicesForModelObjectDelegateBase<LoginOID,Login>
		   implements CRUDServicesForLogin {

/////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTOR   & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public CRUDServicesDelegateForLoginBase(final ServicesCoreModuleConfig coreCfg,
											final CRUDServicesForLogin dbCrud,
											final Marshaller marshaller,
											final EventBus eventBus) {
		super(coreCfg,
			  Login.class,
			  dbCrud,
			  eventBus);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override 
	public CRUDResult<Login> loadBy(final SecurityContext securityContext,
									final IdentityProvider identityProvider,final AuthenticationMethod authnMethod,final SecurityPrincipalOID principalOid) {
		if (principalOid == null) throw new IllegalArgumentException("The [principal] is mandatory to load [login ]");
		
		return this.getServiceImplAs(CRUDServicesForLogin.class)
				   .loadBy(securityContext,
						   identityProvider,authnMethod,principalOid);
	}
	@Override 
	public CRUDResult<Login> loadBy(final SecurityContext securityContext,
									final IdentityProvider identityProvider,final AuthenticationMethod authnMethod,final SecurityPrincipalID principalId) {
		if (principalId == null) throw new IllegalArgumentException("The [principal] is mandatory to load [login ]");
		
		return this.getServiceImplAs(CRUDServicesForLogin.class)
				   .loadBy(securityContext,
						   identityProvider,authnMethod,principalId);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override 
	public CRUDResult<Login> loadBy(final SecurityContext securityContext,
									final IdentityProvider identityProvider,final AuthenticationMethod authnMethod,final LoginID login) {
		if (identityProvider == null) throw new IllegalArgumentException("The [identity provider] is mandatory to load [login ]");
		if (login == null) throw new IllegalArgumentException("The [load] is mandatory to load [login ]");
		
		return this.getServiceImplAs(CRUDServicesForLogin.class)
				   .loadBy(securityContext,
						   identityProvider,authnMethod,login);
	}
}
