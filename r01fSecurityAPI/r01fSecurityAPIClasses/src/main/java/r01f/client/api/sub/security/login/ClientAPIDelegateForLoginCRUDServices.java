package r01f.client.api.sub.security.login;

import jakarta.inject.Provider;
import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.Login;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityBroker;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityIDs.SecurityPrincipalID;
import r01f.oids.SecurityOIDs.LoginOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.api.delegates.ClientAPIDelegateForModelObjectCRUDServices;

public class ClientAPIDelegateForLoginCRUDServices
     extends ClientAPIDelegateForModelObjectCRUDServices<LoginOID,Login> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForLoginCRUDServices(final Provider<SecurityContext>  securityContextProvider,
												 final Marshaller marshaller,
												 final CRUDServicesForLogin crudServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  crudServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns a login from the given principal
	 * @param identityProvider the identity provider
	 * @param authnMethod the authentication method
	 * @param principalOid the login
	 * @return
	 */
	public Login loadBy(final IdentityProvider identityProvider,final AuthenticationMethod authnMethod,final SecurityPrincipalOID principalOid,
						final IdentityBroker identityBroker) {
		CRUDResult<Login> opResult = this.getServiceProxyAs(CRUDServicesForLogin.class)
									 	 .loadBy(this.getSecurityContext(),
									 			 identityProvider,authnMethod,principalOid);
		return ClientAPIDelegateForModelObjectCRUDServices.objFormLoadResult(opResult);
	}
	/**
	 * Returns a login from the given principal
	 * @param identityProvider the identity provider
	 * @param authnMethod the authentication method
	 * @param principalId the login
	 * @return
	 */
	public Login loadBy(final IdentityProvider identityProvider,final AuthenticationMethod authnMethod,final SecurityPrincipalID principalId,
						final IdentityBroker identityBroker) {
		CRUDResult<Login> opResult = this.getServiceProxyAs(CRUDServicesForLogin.class)
									 	 .loadBy(this.getSecurityContext(),
									 			 identityProvider,authnMethod,principalId);
		return ClientAPIDelegateForModelObjectCRUDServices.objFormLoadResult(opResult);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Returns a login from the given login
	 * @param identityProvider the identity provider
	 * @param authnMethod the authentication method
	 * @param login the login
	 * @return
	 */
	public Login loadBy(final IdentityProvider identityProvider,final AuthenticationMethod authnMethod,final LoginID login) {
		CRUDResult<Login> opResult = this.getServiceProxyAs(CRUDServicesForLogin.class)
									 	 .loadBy(this.getSecurityContext(),
									 			 identityProvider,authnMethod,login);
		return ClientAPIDelegateForModelObjectCRUDServices.objFormLoadResult(opResult);
	}
}
