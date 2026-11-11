package r01f.client.api.security;

import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.SecurityPrincipalStatus;
import r01f.model.security.SecurityRoleInModule;
import r01f.model.security.login.Login;
import r01f.model.security.login.data.userpassword.LoginDataForUserPassword;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityIDs.Password;
import r01f.oids.SecurityIDs.SecurityPrincipalID;
import r01f.patterns.Factory;
import r01f.util.types.collections.CollectionUtils;

/**
 * An utility used to create an initial user/password login
 * Usage:
 * <pre class='brush:java'>
 *		return InitialUserPasswordLoginHelpper.createUserObjectInstancesUsing(FN01User::new)
 *											  .createUserPasswordLoginObjectInstancesUsing(FN01UserPasswordLogin::new)
 *											  .use(this)
 *											  .ensureInitialUserWithUserPasswordLogin(LoginID.ADMIN,Password.CHANGE_ME,
 *																					  "System administrator",
 *																					  userRoles);
 * </pre>
 */
@Slf4j
public class InitialUserPasswordLoginHelpper<P extends IsSecurityPrincipal,
											 A extends SecurityAPIBase<P>> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final A _api;
	private final Factory<P> _securityPrincipalFactory;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////
	public InitialUserPasswordLoginHelpper(final A api,
										   final Factory<P> securityPrincipalFactory) {
		_api = api;
		_securityPrincipalFactory = securityPrincipalFactory;
	}
	public static <P extends IsSecurityPrincipal> InitialUserPasswordLoginHelpperApiStep<P> createUserObjectInstancesUsing(final Factory<P> userFactory) {
		return new InitialUserPasswordLoginHelpperApiStep<>(userFactory);
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public static class InitialUserPasswordLoginHelpperApiStep<P extends IsSecurityPrincipal> {
		private final Factory<P> _userFactory;

		public <A extends SecurityAPIBase<P>> InitialUserPasswordLoginHelpper<P,A> use(final A api) {
			return new InitialUserPasswordLoginHelpper<>(api,
														 _userFactory);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Ensures there exists an initial user with password and login
	 * @param loginId
	 * @param password
	 * @param userRoles
	 * @return
	 */
	@SuppressWarnings({ "unused" })
	public boolean ensureInitialUserWithUserPasswordLogin(final LoginID loginId,final Password password,
														  final SecurityRoleInModule... userRoles) {
		try {
			// use the api to load the user (if it exists)
			P existingPrincipal = _api.getPrincipalsApi()
								 	  .getForCrud()
								 	  .loadByOrNull(IdentityProvider.FABRIC,SecurityPrincipalID.fromLoginId(loginId));

			// the required user does NOT exist... create it
			if (existingPrincipal == null) {
				log.warn("The {} [principal] DOES NOT exist at the system: create it",
						 loginId);

				// a) use the api to create the user with given roles
				P newPrincipal = _securityPrincipalFactory.create();
				newPrincipal.setStatus(SecurityPrincipalStatus.ACTIVE);
				if (CollectionUtils.hasData(userRoles)) {
					Stream.of(userRoles)
						  .forEach(userRole -> newPrincipal.addSystemWideRoleInModule(userRole));
				}

				P createdAdminUser = _api.getPrincipalsApi()
										 .getForCrud()
										 .save(newPrincipal);
				log.warn("Created the {} [user] oid={}",
						 loginId,createdAdminUser.getOid());

				// b) create a [user/password login] entry for the [user]
				LoginDataForUserPassword loginData = new LoginDataForUserPassword();
				loginData.setPassword(password);
				loginData.setPasswordHash(password.hash());
				loginData.setConfirmed(true);
				
				Login usrPwdLogin = new Login(loginId);
				usrPwdLogin.setSecurityPrincipalOid(createdAdminUser.getOid());
				usrPwdLogin.setData(loginData);

				Login createdUsrPwdLogin = _api.getLoginApi()
										   	   .getForCrud()
										   	   .create(usrPwdLogin);
				log.warn("Created the {} [user/password login] oid={} > " +
						 "the {} can log-in whith loginId = {}",
						 loginId,createdUsrPwdLogin.getOid(),
						 createdUsrPwdLogin.getLoginId());
			}
			// the user [principal] already exists... ensure it has the required roles
			else {
				log.warn("The {} [user] DOES exist at the system: ensure it has the required roles",
						 loginId);
				if (CollectionUtils.hasData(userRoles)) {
					Stream.of(userRoles)
						  .forEach(userRole -> existingPrincipal.addSystemWideRoleInModule(userRole));
				}
				P updatedUser = _api.getPrincipalsApi()
									.getForCrud()
									.update(existingPrincipal);
			}
			return existingPrincipal != null;
		} catch (final Throwable th) {
			log.error("\n\n\n\n");
			log.error("///////// Error creating the {} user:  {}",
					  loginId,
					  th.getMessage(),th);
			log.error("\n\n\n\n");
		}
		return false;
	}
}
