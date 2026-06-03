package r01f.security.handler;

import jakarta.inject.Provider;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.api.interfaces.security.user.CRUDServicesForUser;
import r01f.jwt.JWTDecoded;
import r01f.model.persistence.PersistenceException;
import r01f.model.security.login.Login;
import r01f.model.security.operation.SecurityOperation;
import r01f.model.security.operation.SecurityOperationExecResultBuilder;
import r01f.model.security.operation.SecurityOperationResult;
import r01f.model.security.user.User;
import r01f.patterns.FactoryFrom;
import r01f.security.provider.SecurityProviderSecretConfig;
import r01f.security.user.UserDataFromThirdPartySecurityProvider;
import r01f.securitycontext.SecurityContext;
import r01f.types.StringBase64;

@Slf4j
public abstract class UserDataFetchHandlerForThirdPartyProviderBase<U extends User,L extends Login,
																	T  extends UserDataFromThirdPartySecurityProvider>
			  extends UserDataFetchHandlerBase<U,L> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final FactoryFrom<T,U> _localUserFactoryFromThirdPartyProviderUser;
	protected final FactoryFrom<T,L> _loginFactoryFromThirdPartyProviderUser;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public UserDataFetchHandlerForThirdPartyProviderBase(final Class<U> userType,final Class<L> loginType,
														 final Provider<SecurityContext> securityContextProvider,
														 final CRUDServicesForUser<U> userCrud,final CRUDServicesForLogin<L> loginCrud,
														 final SecurityProviderSecretConfig securityProviderSecretConfig,
														 final FactoryFrom<T,U> localUserFactoryFromThirdPartyProviderUser,
														 final FactoryFrom<T,L> loginFactoryFromThirdPartyProviderUser) {
		super(userType,loginType,
			  securityContextProvider,
			  userCrud,loginCrud,
			  securityProviderSecretConfig);
		_localUserFactoryFromThirdPartyProviderUser = localUserFactoryFromThirdPartyProviderUser;
		_loginFactoryFromThirdPartyProviderUser = loginFactoryFromThirdPartyProviderUser;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public SecurityOperationResult<U> fetchUser(final JWTDecoded jwtDecoded,
												final StringBase64 base64SecurityProviderSession) {
		log.info(" ... [1]  Fetch the user data from the THIRD PARTY provider (ie: google, xlnets, etc)  ");

		log.warn("INFORMATIVE Purpose: FETCH user data using JWT data, for provider type: {}.",
				 this.getClass());
		
		// [1] - Fetch the user data from the THIRD PARTY provider (ie: google, xlnets, etc)
		T thirdPartyUserData = this.fetchThirdPartyUserUsing(jwtDecoded, // get data on google, xlnets, etc...
															 base64SecurityProviderSession);

		// [2] - Load the user data from the LOCAL storage
		//		 (the user data might NOT exists on the LOCAL storage)
		log.debug("... [2]  Load the user data from the LOCAL storage  ");
		U localUserData = this.fetchLocalUserUsing(thirdPartyUserData);

		// [3] - Depending on the [user] existing on the [local storage]:
		//			- If the user DOES NOT previously exists > ask the security provider (ie google) for the [user data] and create a NEW local [user]
		//			- If the user DOES previously exists > update it with the
		if (localUserData == null) {
			// the user DOES NOT EXISTS at the LOCAL storage
			// a) ask the [security provider] (ie google) for the [user data]
			localUserData = _localUserFactoryFromThirdPartyProviderUser.from(thirdPartyUserData);	// Create a new User from the GoogleUserData, XlnetsUserData, etc...

			// b) CREATE a NEW local [user]
			try {
				localUserData = _userCrud.create(_securityContextProvider.get(),
								 				 localUserData)
						 				 .getOrThrow();
			} catch (Exception e) {
				log.error("... error create new local user with oid={}. Message: {}",
						  e.getMessage(),
						  localUserData);
				throw e;
			}
			// c) CREATE a NEW local [login]
			L login = _loginFactoryFromThirdPartyProviderUser.from(thirdPartyUserData);				// BEWARE!! this does NOT set the user oid
			login.setUserOid(localUserData.getOid());		// DO NOT FORGET!!!

			log.debug("...there does NOT exists a login={}: create it associated with user oid={}",
					 login.getLoginId(),login.getUserOid());
			_loginCrud.create(_securityContextProvider.get(),
							  login);
		} else {
			// the user ALREADY EXISTS at the LOCAL storage
			// a) ask the [security provider] (ie google) for the [user data] (it might be updated)
			U localUserFromThirdPartyUser = _localUserFactoryFromThirdPartyProviderUser.from(thirdPartyUserData);	// Create a new User from the GoogleUserData, XLNETs,etc..

			// b) check if any of the [security provider]-returned data was updated
			boolean thirdpPartyUserDataChanged = localUserData.updateFrom(localUserFromThirdPartyUser);

			// c) UPDATE the local [user]
			if (thirdpPartyUserDataChanged) {
				localUserData = _userCrud.update(_securityContextProvider.get(),
								 				localUserData)
										 .getOrThrow();	// this object was UPDATED
			}
		}

		log.warn("INFORMATIVE Purpose: END FETCH USER, for provider type: {}. User: {}", 
				this.getClass(),
				localUserData.getOid());

		return 	SecurityOperationExecResultBuilder.executed(SecurityOperation.USER_DATA_REQUEST_VIA_JWT)
						  	  					  .returning(localUserData);
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
	public abstract T fetchThirdPartyUserUsing(final JWTDecoded jwtDecoded,
											   final StringBase64 base64SecurityProviderSession);
/////////////////////////////////////////////////////////////////////////////////////////
//	FETCH LOCAL USER
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Fetch the local-stored user data
	 * @param thirdPartyUserData
	 * @return
	 */
	public U fetchLocalUserUsing(final T thirdPartyUserData) {
		log.trace("=========================================================");
		log.trace("Fetch LOCAL user data using {} user data",thirdPartyUserData.getClass().getSimpleName());
		log.trace("=========================================================");

		// [0] - Get the system security context
		SecurityContext sysSecurityContext = _securityContextProvider.get();

		// [1] - load the login
		L login = _loadLoginUsing(sysSecurityContext,
								  thirdPartyUserData);
		log.info("login {} using third parth user data",
				 login != null ? "FOUND" : "NOT FOUND");

		// [2] - Load the user
		U user = null;
		if (login != null
		 && login.getUserOid() != null) {
			try {
				log.info("...loading user={} for login={}",
					     login.getUserOid(),login.getLoginId());
				user = _userCrud.load(sysSecurityContext,
									  login.getUserOid())
								.getOrThrow();
			} catch (final PersistenceException pEx) {
				if (pEx.isEntityNotFound()) {
					log.error("The DB is in an INCONSISTENT state: user oid={} does NOT exist for login oid={}",
							  login.getUserOid(),login.getOid());
				} else {
					throw pEx;
				}
			}
		}
		if (user == null) {
			// maybe there's no [login] but the [user] exists...
			// ... try to find the [user] using some any data from the [third party user data] (email, user code, etc)
			log.info("Could NOT find the [user]... try to user some data from the [third party provider user data] (email, user code, etc)");
			user = _loadUserUsing(sysSecurityContext,
								  thirdPartyUserData);
		}
		// The user exists but the login does NOT: create a NEW [login] associated with the existing [user]
		if (user != null && login == null) {
			login = _loginFactoryFromThirdPartyProviderUser.from(thirdPartyUserData);	// BEWARE!! this does NOT set the user oid
			login.setUserOid(user.getOid());		// DO NOT FORGET!!!
			_loginCrud.create(_securityContextProvider.get(),
							  login);
		}
		return user;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LOCAL
/////////////////////////////////////////////////////////////////////////////////////////
	protected L _loadLoginUsing(final SecurityContext sysSecurityContext,
								final T thirdPartyUserData) {
		L login = null;
		try {
			login = _loginCrud.loadBy(sysSecurityContext,
							   		  thirdPartyUserData.getLoginId())
							  .getOrThrow();
		} catch (final PersistenceException pEx) {
			if (pEx.isEntityNotFound()) {
				log.warn("... no user found for [login] for login id ={}",thirdPartyUserData.getLoginId());
			} else {
				throw pEx;
			}
		}
		return login;
	}
	protected U _loadUserUsing(final SecurityContext sysSecurityContext,
							   final T thirdPartyUserData) {
		if (thirdPartyUserData.getMail() == null) return null;

		// ... try to find the user using the [mail]
		U user = null;
		try {
			user = _userCrud.loadBy(sysSecurityContext,
									thirdPartyUserData.getMail())
							.getOrThrow();
		} catch (final PersistenceException pEx) {
			if (pEx.isEntityNotFound()) {
				log.info("... there does NOT exist any user with email={}",thirdPartyUserData.getMail());
			} else {
				throw pEx;
			}
		}
		return user;
	}
}
