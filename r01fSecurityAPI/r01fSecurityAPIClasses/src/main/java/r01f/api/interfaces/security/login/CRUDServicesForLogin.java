package r01f.api.interfaces.security.login;

import r01f.model.persistence.CRUDResult;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.Login;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityIDs.SecurityPrincipalID;
import r01f.oids.SecurityOIDs.LoginOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.interfaces.CRUDServicesForModelObject;

public interface CRUDServicesForLogin
		 extends CRUDServicesForModelObject<LoginOID,Login> {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns a login for the given [principal]
	 * @param securityContext 
	 * @param identityProvider the identity provider
	 * @param authnMethod the authentication method
	 * @param principalOid 
	 * @return a {@link CRUDResult} that encapsulates the entity if it was loaded successfully
	 */
	public CRUDResult<Login> loadBy(final SecurityContext securityContext,
									final IdentityProvider identityProvider,final AuthenticationMethod authnMethod,final SecurityPrincipalOID principalOid);
	
	/**
	 * Returns a login for the given [principal]
	 * @param securityContext
	 * @param identityProvider
	 * @param authnMethod
	 * @param principalId
	 * @return
	 */
	public CRUDResult<Login> loadBy(final SecurityContext securityContext,
									final IdentityProvider identityProvider,final AuthenticationMethod authnMethod,final SecurityPrincipalID principalId);
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Returns a login for the given [login]
	 * @param securityContext 
	 * @param identityProvider the identity provider 
	 * @param authnMethod the authentication method
	 * @param login the login
	 * @return a {@link CRUDResult} that encapsulates the entity if it was loaded successfully
	 */
	public CRUDResult<Login> loadBy(final SecurityContext securityContext,
									final IdentityProvider identityProvider,final AuthenticationMethod authnMethod,final LoginID login);
}

