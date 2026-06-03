package r01f.security.login;

import java.time.Instant;
import java.util.Collection;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.guids.CommonOIDs.WebSessionID;
import r01f.model.security.SecurityObject;
import r01f.model.security.user.User;
import r01f.model.security.user.UserRoleInModule;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.securitycontext.SecurityIDS.UserRole;
import r01f.securitycontext.SecurityOIDs.UserOID;

@Accessors(prefix="_")
public class LoginSession<U extends User>
  implements SecurityObject {
	
	private static final long serialVersionUID = 4322523635887085378L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Getter private final WebSessionID _sessionUID;
	@Getter private final SecurityProviderID _securityProviderId;
	@Getter private final LoginID _loginId;
	@Getter private final U _user;
	@Getter private final Instant _createdAt;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR																		  
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginSession(final WebSessionID sessionOid,
						final SecurityProviderID securityProviderId,final LoginID loginId,
						final U user,
						final Instant createDate) {
		_sessionUID = sessionOid;
		_securityProviderId = securityProviderId;
		_loginId = loginId;
		_user = user;
		_createdAt = createDate;
	}
	public LoginSession(final WebSessionID sessionOid,	// use: WebSessionOID.forId(request.getSession(true).getId()),	// get the web session id
						final SecurityProviderID securityProviderId,final LoginID loginId,
						final U user) {
		_sessionUID = sessionOid;
		_securityProviderId = securityProviderId;
		_loginId = loginId;
		_user = user;
		_createdAt = Instant.now();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CLONE
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Creates a NEW {@link LoginSession} with an updated user
	 * @param user
	 * @return
	 */
	public LoginSession<U> cloneForUpdatedUser(final U user) {
		if (user == null) throw new IllegalArgumentException("user is NOT valid!");
		if (user.getOid() == null
		 || user.getOid().isNOT(_user.getOid())) throw new IllegalArgumentException("The userOid=" + user.getOid() + " DOES NOT MATCH the actual userOid=" + _user.getOid());
		return new LoginSession<>(_sessionUID,
								  _securityProviderId,_loginId,
								  user,
								  _createdAt);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//																			  
/////////////////////////////////////////////////////////////////////////////////////////
	@Deprecated
	public U getUserData() {
		return _user;
	}
	public UserOID getUserOid() {
		return _user != null ? _user.getOid()
							 : null;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public Collection<UserRoleInModule> getUserSystemWideRoles() {
		return _user != null ? _user.getUserSystemWideRoles()
							 : null;
	}
	public Collection<UserRole> getUserSystemWideRolesIn(final AppComponent module) {
		return _user != null ? _user.getUserSystemWideRolesIn(module)
							 : null;
	}
	public boolean hasAnyUserSystemWideRoleIn(final AppComponent module) {
		return _user != null ? _user.hasAnyUserSystemWideRoleIn(module)
							 : false;
	}
	public boolean hasUserSystemWideRoleIn(final AppComponent module,final UserRole userRole) {
		return _user != null ? _user.hasUserSystemWideRoleIn(module,
															 userRole)
							 : false;
	}
}
