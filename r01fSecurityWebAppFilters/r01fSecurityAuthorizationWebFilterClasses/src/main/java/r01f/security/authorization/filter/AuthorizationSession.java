package r01f.security.authorization.filter;

import java.util.Collection;
import java.util.Iterator;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.debug.Debuggable;
import r01f.model.security.IsSecurityObject;
import r01f.model.security.SecurityRoleInModule;
import r01f.model.security.authorization.SecurityPrincipalAuthorizationOnResource;
import r01f.oids.CommonOIDs.AppComponent;
import r01f.oids.SecurityIDs.SecurityRole;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.util.types.collections.CollectionUtils;

/**
 * Just a object to persist into session or thread local, build from LoginSession object
 * ( Login session will be the remote object returned via jwt querie)
 */
@Accessors(prefix="_")
public class AuthorizationSession
  implements Debuggable,
  			 IsSecurityObject {

	private static final long serialVersionUID = -8108435481219405867L;
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final SecurityPrincipalOID _securityPrincipalOid;
	@Getter private final Collection<SecurityRoleInModule> _systemWideRoles;
	@Getter private final Collection<SecurityPrincipalAuthorizationOnResource> _authorizations;

/////////////////////////////////////////////////////////////////////////////////////////
// 	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public AuthorizationSession(final SecurityPrincipalOID securityPrincipalOid,
								final Collection<SecurityRoleInModule> systemWideRoles,
								final Collection<SecurityPrincipalAuthorizationOnResource> authzs) {
		_securityPrincipalOid = securityPrincipalOid;
		_systemWideRoles = systemWideRoles;
		_authorizations = authzs;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	AUXILIAR METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	public boolean hasUserSystemWideRoleIn(final AppComponent module,
										   final SecurityRole userRole) {
		return CollectionUtils.hasData(_systemWideRoles) ? _systemWideRoles.stream()
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
		dbg.append("[auth session] for [principal]=").append(_securityPrincipalOid).append("\n");
		if (CollectionUtils.isNullOrEmpty(_authorizations)) { 
			dbg.append("\t- NO auths on any resource");
		} else {
			dbg.append("\t- ").append(_authorizations.size()).append(" auths\n");
			for (Iterator<SecurityPrincipalAuthorizationOnResource> authIt = _authorizations.iterator(); authIt.hasNext(); ) {
				SecurityPrincipalAuthorizationOnResource auth = authIt.next();
				dbg.append("\t\t- ").append(auth.getAuthorizationTargetResourceOid()).append(" (").append(auth.getRole()).append(")");
				if (authIt.hasNext()) dbg.append("\n");
			}
		}
		dbg.append("\n");
		if (CollectionUtils.isNullOrEmpty(_systemWideRoles)) { 
			dbg.append("\t- NO sys-wide role");
		} else {
			dbg.append("\t- ").append(_systemWideRoles.size()).append(" sys wide roles\n");
			for (Iterator<SecurityRoleInModule> role = _systemWideRoles.iterator(); role.hasNext(); ) {
				SecurityRoleInModule auth = role.next();
				dbg.append("\t\t- ").append(auth.getAppComponent()).append(" (").append(auth.getRole()).append(")");
				if (role.hasNext()) dbg.append("\n");
			}
		}
		return dbg;
	}
}
