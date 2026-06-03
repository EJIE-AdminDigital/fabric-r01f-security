package r01f.security.authorization.filter;

import java.util.Collection;
import java.util.Iterator;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.debug.Debuggable;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.model.security.SecurityObject;
import r01f.model.security.authorization.UserAuthorizationOnResource;
import r01f.model.security.user.UserRoleInModule;
import r01f.securitycontext.SecurityIDS.UserRole;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.util.types.collections.CollectionUtils;

/**
 * Just a object to persist into session or thread local, build from LoginSession object
 * ( Login session will be the remote object returned via jwt querie)
 */
@Accessors(prefix="_")
public class UserAuthorizationSession<A extends UserAuthorizationOnResource>
  implements Debuggable,
  			 SecurityObject {

	private static final long serialVersionUID = -8108435481219405867L;
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final UserOID _userOid;
	@Getter private final Collection<UserRoleInModule> _systemWideUserRoles;
	@Getter private final Collection<A> _userAuths;

/////////////////////////////////////////////////////////////////////////////////////////
// 	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public UserAuthorizationSession(final UserOID userOid,
									final Collection<UserRoleInModule> systemWideUserRoles,
									final Collection<A> userAuths) {
		_userOid = userOid;
		_systemWideUserRoles = systemWideUserRoles;
		_userAuths = userAuths;
	}
	
/////////////////////////////////////////////////////////////////////////////////////////
//	AUXILIAR METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	public boolean hasUserSystemWideRoleIn(final AppComponent module,
										   final UserRole userRole) {
		return CollectionUtils.hasData(_systemWideUserRoles) ? _systemWideUserRoles.stream()
																				   .anyMatch(role -> role.getAppComponent().is(module)
																									&& role.getRole().is(userRole))
															 : false;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	DEBUG
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CharSequence debugInfo() {
		StringBuilder dbg = new StringBuilder();
		dbg.append("[auth session] for user oid=").append(_userOid).append("\n");
		if (CollectionUtils.isNullOrEmpty(_userAuths)) { 
			dbg.append("\t- NO auths on any resource");
		} else {
			dbg.append("\t- ").append(_userAuths.size()).append(" auths\n");
			for (Iterator<A> authIt = _userAuths.iterator(); authIt.hasNext(); ) {
				A auth = authIt.next();
				dbg.append("\t\t- ").append(auth.getAuthorizationTargetResourceOid()).append(" (").append(auth.getRole()).append(")");
				if (authIt.hasNext()) dbg.append("\n");
			}
		}
		dbg.append("\n");
		if (CollectionUtils.isNullOrEmpty(_systemWideUserRoles)) { 
			dbg.append("\t- NO sys-wide role");
		} else {
			dbg.append("\t- ").append(_systemWideUserRoles.size()).append(" sys wide roles\n");
			for (Iterator<UserRoleInModule> role = _systemWideUserRoles.iterator(); role.hasNext(); ) {
				UserRoleInModule auth = role.next();
				dbg.append("\t\t- ").append(auth.getAppComponent()).append(" (").append(auth.getRole()).append(")");
				if (role.hasNext()) dbg.append("\n");
			}
		}
		return dbg;
	}
}
