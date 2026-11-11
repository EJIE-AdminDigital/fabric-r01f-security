package r01f.security.authn.handler;

import jakarta.inject.Provider;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.api.interfaces.security.principal.CRUDServicesForPrincipal;
import r01f.jwt.JWTDecoded;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.PersistenceException;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.Login;
import r01f.model.security.operation.IsSecurityOperationResult;
import r01f.model.security.operation.SecurityOperation;
import r01f.model.security.operation.SecurityOperationExecResultBuilder;
import r01f.model.security.principal.SecurityPrincipalAndLogin;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityIDs.SecurityPrincipalID;
import r01f.patterns.FactoryFrom;
import r01f.security.authn.provider.config.AuthenticationProviderSecretConfig;
import r01f.types.StringBase64;


@Slf4j
public abstract class SecurityPrincipalDataFetchHandlerForThirdPartyProviderBase<P extends IsSecurityPrincipal,T  extends IsIdentityProviderReturnedData>
			  extends SecurityPrincipalDataFetchHandlerBase<P> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final FactoryFrom<T,P> _localPrincipalFactoryFromIdentityProviderReturnedData;
	protected final FactoryFrom<T,Login> _loginFactoryFromIdentityProviderReturnedData;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public SecurityPrincipalDataFetchHandlerForThirdPartyProviderBase(final Class<P> userType,
																	  final Provider<SecurityContext> securityContextProvider,
																	  final CRUDServicesForPrincipal<P> principalCrud,final CRUDServicesForLogin loginCrud,
																	  final AuthenticationProviderSecretConfig securityProviderSecretConfig,
																	  final FactoryFrom<T,P> localPrincipalFactoryFromIdentityProviderReturnedData,
																	  final FactoryFrom<T,Login> loginFactoryFromIdentityProviderReturnedData) {
		super(userType,
			  securityContextProvider,
			  principalCrud,loginCrud,
			  securityProviderSecretConfig);
		_localPrincipalFactoryFromIdentityProviderReturnedData = localPrincipalFactoryFromIdentityProviderReturnedData;
		_loginFactoryFromIdentityProviderReturnedData = loginFactoryFromIdentityProviderReturnedData;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * the [identity provider] type (ie: google, xlnets, etc)
	 */
	public abstract IdentityProvider getIdentityProvider();
	/**
	 * the [authentication method] type (ie: user/password, paired phone, etc)
	 */
	public abstract AuthenticationMethod getAuthenticationMethod();
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public IsSecurityOperationResult<SecurityPrincipalAndLogin<P>> fetchPrincipalAndLogin(final JWTDecoded jwtDecoded,
													   							  		  final StringBase64 base64SecurityProviderSession) {
		log.info(" ... [1]  Fetch the [principal] data from the THIRD PARTY [identity provider]={} ({})",
				 this.getIdentityProvider(),this.getClass());

		// [1] - Fetch the user data from the THIRD PARTY provider (ie: google, xlnets, etc)
		T thirdPartyData = this.fetchThirdPartyPrincipalDataUsing(jwtDecoded, // get data on google, xlnets, etc...
													     base64SecurityProviderSession);

		// [2] - Load the user data from the LOCAL storage
		//		 (the user data might NOT exists on the LOCAL storage)
		log.debug("... [2]  Load the [principal] & [login] from the LOCAL storage  ");
		SecurityPrincipalAndLogin<P> localPrincipalAndLogin = this.fetchLocalSecurityPrincipalUsing(thirdPartyData);
		
		P localPrincipal = localPrincipalAndLogin.getSecurityPrincipal();
		Login localLogin = localPrincipalAndLogin.getLogin();

		// [3] - Depending on the [user] existing on the [local storage]:
		//			- If the user DOES NOT previously exists > ask the security provider (ie google) for the [user data] and create a NEW local [user]
		//			- If the user DOES previously exists > update it with the
		if (localPrincipal == null) {
			// the [principal] DOES NOT EXISTS at the LOCAL storage
			// a) ask the [security provider] (ie google) for the [user data]
			localPrincipal = _localPrincipalFactoryFromIdentityProviderReturnedData.from(thirdPartyData);	// Create a new User from the GoogleUserData, XlnetsUserData, etc...
			if (localPrincipal == null) throw new IllegalStateException("The [security provider] (ie google, xlnets, etc) did NOT return data enough to build the [security principal]");
			if (localPrincipal.getOid() == null) throw new IllegalStateException("The [principal] OID cannot be null!!");
			if (localPrincipal.getSourceIdentityProvider() == null) throw new IllegalStateException("The [identity provider] cannot be null!!");
			
			// b) CREATE a NEW local [security principal]
			try {
				localPrincipal = _principalCrud.create(_securityContextProvider.get(),
								 				 	   localPrincipal)
						 				 	   .getOrThrow();
			} catch (Exception e) {
				log.error("... error create new local [principal] with oid={}. Message: {}",
						  e.getMessage(),
						  localPrincipal);
				throw e;
			}
			// c) CREATE a NEW local [login]
			localLogin = _createLocalLogin(_securityContextProvider.get(),
										   localPrincipal,thirdPartyData);
		} else {
			// the [principal] ALREADY EXISTS at the LOCAL storage
			// a) ask the [security provider] (ie google) for the [principal data] (it might be updated)
			P localPrincipalFromThirdPartyUser = _localPrincipalFactoryFromIdentityProviderReturnedData.from(thirdPartyData);	// Create a new User from the GoogleUserData, XLNETs,etc..

			// c) UPDATE the local [user]
			if (localPrincipal.hasDifferentDataTo(localPrincipalFromThirdPartyUser)) {
				localPrincipal.updateFrom(localPrincipalFromThirdPartyUser);
				localPrincipal = _principalCrud.update(_securityContextProvider.get(),
								 				 	   localPrincipal)
										 	   .getOrThrow();	// this object was UPDATED
			}
		}
		log.warn("INFORMATIVE Purpose: END FETCH [principal] from [identity provider]={}. [principal] oid={}", 
				this.getClass(),
				localPrincipal.getOid());

		return 	SecurityOperationExecResultBuilder.executed(SecurityOperation.USER_DATA_REQUEST_VIA_JWT)
						  	  					  .returning(new SecurityPrincipalAndLogin<>(localPrincipal,
						  	  							  									 localLogin));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	FETCH THIRD PARTY USER
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Fetch user data from a third party provider (ie: google)
	 * @param jwt
	 * @param base64SecurityProviderSession
	 * @return
	 */
	public abstract T fetchThirdPartyPrincipalDataUsing(final JWTDecoded jwtDecoded,
											   			final StringBase64 base64SecurityProviderSession);
/////////////////////////////////////////////////////////////////////////////////////////
//	FETCH LOCAL USER
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Fetch the local-stored user data
	 * @param thirdPartyData
	 * @return
	 */
	public SecurityPrincipalAndLogin<P> fetchLocalSecurityPrincipalUsing(final T thirdPartyData) {
		log.trace("=========================================================");
		log.trace("Fetch LOCAL [security principal] using {}",thirdPartyData.getClass().getSimpleName());
		log.trace("=========================================================");

		// [0] - Get the system security context
		SecurityContext sysSecurityContext = _securityContextProvider.get();

		// [1] - load the login
		Login localLogin = _loadLocalLoginUsing(sysSecurityContext,
								  	   	        this.getIdentityProvider(),this.getAuthenticationMethod(),thirdPartyData.getLoginId());
		log.info("login {} using third parth user data (loginId={} identityProvider={} / authenticationMethod={})",
				 localLogin != null ? "FOUND" : "NOT FOUND",
				 thirdPartyData.getLoginId(),this.getIdentityProvider(),this.getAuthenticationMethod());

		// [2] - Load the [principal]
		P localPrincipal = null;
		if (localLogin != null
		 && localLogin.getSecurityPrincipalOid() != null) {
			try {
				log.info("...loading principal={} for login={} (identityProvider={})",
					     localLogin.getSecurityPrincipalOid(),
					     localLogin.getLoginId(),this.getIdentityProvider());
				localPrincipal = _principalCrud.load(sysSecurityContext,
									  	   			 localLogin.getSecurityPrincipalOid())
										  	   .getOrThrow();
			} catch (PersistenceException pEx) {
				if (pEx.isEntityNotFound()) {
					log.error("The DB is in an INCONSISTENT state: principalOid={} / identityProvider={} does NOT exist for login oid={}",
							  localLogin.getSecurityPrincipalOid(),this.getIdentityProvider(),
							  localLogin.getOid());
				} else {
					throw pEx;
				}
			}
		}
		if (localPrincipal == null) {
			// maybe there's no [login] but the [principal] exists...
			// ... try to find the [principal] using some any data from the [third party user data] (email, user code, etc)
			log.info("Could NOT find the [principal]... try to [principal] some data from the third party [identity provider]-returned data] (email, user code, etc)");
			localPrincipal = _loadLocalPrincipalUsing(sysSecurityContext,
								  	   	    	 	  this.getIdentityProvider(),thirdPartyData.getSecurityPrincipalId());
		}
		// The [principal] exists but the login does NOT: create a NEW [login] associated with the existing [principal]]
		if (localPrincipal != null
		 && localLogin == null) {
			log.info("The [principal] exists but the [login] does NOT: create a NEW [login] associated with the existing [principal]");
			localLogin = _createLocalLogin(sysSecurityContext,
										   localPrincipal,thirdPartyData);
		}
		return new SecurityPrincipalAndLogin<>(localPrincipal,
											  localLogin);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LOCAL
/////////////////////////////////////////////////////////////////////////////////////////
	protected Login _loadLocalLoginUsing(final SecurityContext sysSecurityContext,
									 	 final IdentityProvider identityProvider,final AuthenticationMethod authnMethod,final LoginID loginId) {
		Login login = null;
		try {
			login = _loginCrud.loadBy(sysSecurityContext,
							   		  identityProvider,authnMethod,loginId)
							  .getOrThrow();
		} catch (PersistenceException pEx) {
			if (pEx.isEntityNotFound()) {
				log.warn("... no user found for [login]={} ([identity provider]={}/authentication method={})",
						 identityProvider,authnMethod,loginId);
			} else {
				throw pEx;
			}
		}
		return login;
	}
	protected P _loadLocalPrincipalUsing(final SecurityContext sysSecurityContext,
							   			 final IdentityProvider identityProvider,final SecurityPrincipalID securityPrincipaId) {
		if (securityPrincipaId == null) return null;

		// ... try to find the user using the [mail]
		P principal = null;
		try {
			principal = _principalCrud.loadBy(sysSecurityContext,
										 	  identityProvider,securityPrincipaId)
									  .getOrThrow();
		} catch (PersistenceException pEx) {
			if (pEx.isEntityNotFound()) {
				log.info("... there does NOT exist any [principal] with principalId={}",securityPrincipaId);
			} else {
				throw pEx;
			}
		}
		return principal;
	}
	protected Login _createLocalLogin(final SecurityContext sysSecurityContext,
									  final P localPrincipal,final T thirdPartyData) {
		Login login = _loginFactoryFromIdentityProviderReturnedData.from(thirdPartyData);	
		login.setSecurityPrincipalOid(localPrincipal.getOid());			// DO NOT FORGET!!!
		
		log.debug("...there does NOT exists a login={}: create it associated with [principal]={}",
				 login.getLoginId(),login.getSecurityPrincipalOid());
		CRUDResult<Login> create = _loginCrud.create(sysSecurityContext,
						  							 login);
		return create.getOrThrow();
	}
}
