package r01f.client.api.security;

import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import r01f.locale.Language;
import r01f.model.security.login.userpassword.UserPasswordLogin;
import r01f.model.security.user.User;
import r01f.model.security.user.UserRoleInModule;
import r01f.model.security.user.UserStatus;
import r01f.patterns.Factory;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.Password;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.types.contact.HasMailAddress;
import r01f.types.contact.HasPhoneNumber;
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
public class InitialUserPasswordLoginHelpper<U extends User & HasMailAddress & HasPhoneNumber,L extends UserPasswordLogin,
											 A extends SecurityAPIBase<U,?,?,?,?>> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final A _api;
	private final Factory<U> _userFactory;
	private final Factory<L> _loginFactory;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////
	public InitialUserPasswordLoginHelpper(final A api,
										   final Factory<U> userFactory,final Factory<L> loginFactory) {
		_api = api;
		_userFactory = userFactory;
		_loginFactory = loginFactory;
	}
	public static <U extends User & HasMailAddress & HasPhoneNumber> InitialUserPasswordLoginHelpperUserPasswordLoginFactoryStep<U> createUserObjectInstancesUsing(final Factory<U> userFactory) {
		return new InitialUserPasswordLoginHelpperUserPasswordLoginFactoryStep<>(userFactory);
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public static class InitialUserPasswordLoginHelpperUserPasswordLoginFactoryStep<U extends User & HasMailAddress & HasPhoneNumber> {
		private final Factory<U> _userFactory;

		public <L extends UserPasswordLogin> InitialUserPasswordLoginHelpperApiStep<U,L> createUserPasswordLoginObjectInstancesUsing(final Factory<L> userPasswordLoginFactory) {
			return new InitialUserPasswordLoginHelpperApiStep<>(_userFactory,userPasswordLoginFactory);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public static class InitialUserPasswordLoginHelpperApiStep<U extends User & HasMailAddress & HasPhoneNumber,L extends UserPasswordLogin> {
		private final Factory<U> _userFactory;
		private final Factory<L> _userPasswordLoginFactory;

		public <A extends SecurityAPIBase<U,?,?,?,?>> InitialUserPasswordLoginHelpper<U,L,A> use(final A api) {
			return new InitialUserPasswordLoginHelpper<>(api,
														 _userFactory,_userPasswordLoginFactory);
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
	@SuppressWarnings({ "unused","unchecked" })
	public boolean ensureInitialUserWithUserPasswordLogin(final LoginID loginId,final Password password,
														  final UserRoleInModule... userRoles) {
		try {
			// use the api to load the user (if it exists)
			U existingUser = _api.getUsersApi()
								 .getForCrud()
								 .loadByOrNull(SecurityProviderID.USER_PASSWORD,loginId);

			// the required user does NOT exist... create it
			if (existingUser == null) {
				log.warn("The {} [user] DOES NOT exist at the system: create it",
						 loginId);

				// a) use the api to create the user with given roles
				U newUser = _userFactory.create();
				newUser.setPrefLang(Language.DEFAULT);
				newUser.setStatus(UserStatus.ACTIVE);
				if (CollectionUtils.hasData(userRoles)) {
					Stream.of(userRoles)
						  .forEach(userRole -> newUser.addUserSystemWideRoleInModule(userRole));
				}

				U createdAdminUser = _api.getUsersApi()
										 .getForCrud()
										 .save(newUser);
				log.warn("Created the {} [user] oid={}",
						 loginId,createdAdminUser.getOid());

				// b) create a [user/password login] entry for the [user]
				L usrPwdLogin = _loginFactory.create();
				usrPwdLogin.setUserOid(createdAdminUser.getOid());
				usrPwdLogin.setLoginId(loginId);
				usrPwdLogin.setPassword(password);	// user/password = admin/admin CHANGE!
				usrPwdLogin.setConfirmed(true);

				L createdUsrPwdLogin = (L)_api.getUserPasswordLoginApi()
										   	  .getForCrud()
										   	  .create(usrPwdLogin);
				log.warn("Created the {} [user/password login] oid={} > " +
						 "the {} can log-in whith user/password = {}/{}",
						 loginId,
						 createdUsrPwdLogin.getOid(),
						 createdUsrPwdLogin.getLoginId(),createdUsrPwdLogin.getPassword());
			}
			// the user user does exist... ensure it has the required roles
			else {
				log.warn("The {} [user] DOES exist at the system: ensure it has the required roles",
						 loginId);
				if (CollectionUtils.hasData(userRoles)) {
					Stream.of(userRoles)
						  .forEach(userRole -> existingUser.addUserSystemWideRoleInModule(userRole));
				}
				U updatedUser = _api.getUsersApi()
									.getForCrud()
									.save(existingUser);
			}
			return existingUser != null;
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
