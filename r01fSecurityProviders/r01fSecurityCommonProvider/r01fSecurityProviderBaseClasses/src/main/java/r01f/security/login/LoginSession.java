package r01f.security.login;

import java.time.Instant;
import java.util.Collection;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.model.security.HasSystemWideRoles;
import r01f.model.security.IsSecurityObject;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.SecurityRoleInModule;
import r01f.oids.CommonOIDs.AppComponent;
import r01f.oids.CommonOIDs.WebSessionID;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.SecurityPrincipalID;
import r01f.oids.SecurityIDs.SecurityRole;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;

@Accessors(prefix="_")
public class LoginSession<P extends IsSecurityPrincipal>
  implements HasSystemWideRoles,
  			 IsSecurityObject {
	
	private static final long serialVersionUID = 4322523635887085378L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Getter private final WebSessionID _sessionUID;
	@Getter private final IdentityProvider _identityProvider;
	@Getter private final AuthenticationMethod _authenticationMethod;
	@Getter private final P _securityPrincipal;	
	@Getter private final Instant _createdAt;
	
	@Getter private final Collection<SecurityRoleInModule> _systemWideRoles;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR																		  
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginSession(final WebSessionID sessionOid,
						final IdentityProvider identityProvider,final AuthenticationMethod authenticationMethod,
						final P securityPrincipal,
						final Instant createDate,
						final Collection<SecurityRoleInModule> systemWideRoles) {
		_sessionUID = sessionOid;
		_identityProvider = identityProvider;
		_authenticationMethod = authenticationMethod;
		_securityPrincipal = securityPrincipal;
		_createdAt = createDate;
		_systemWideRoles = systemWideRoles;
	}
	public LoginSession(final WebSessionID sessionOid,	// use: WebSessionOID.forId(request.getSession(true).getId()),	// get the web session id
						final IdentityProvider securityProviderId,final AuthenticationMethod authenticationMethod,
						final P securityPrincipal,
						final Collection<SecurityRoleInModule> systemWideRoles) {
		this(sessionOid,
			 securityProviderId,authenticationMethod,
			 securityPrincipal,
			 Instant.now(),
			 systemWideRoles);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CLONE
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Creates a NEW {@link LoginSession} with an updated [principal]
	 * @param principal
	 * @return
	 */
	public LoginSession<P> cloneForUpdatedSecurityPrincipal(final P principal) {
		if (principal == null) throw new IllegalArgumentException("[security principal] is NOT valid!");
		if (principal.getOid() == null
		 || principal.getOid().isNOT(_securityPrincipal.getOid())) throw new IllegalArgumentException("The [principal]=" + principal.getOid() + " DOES NOT MATCH the actual [principal]=" + _securityPrincipal.getOid());
		return new LoginSession<>(_sessionUID,
								  _identityProvider,_authenticationMethod,
								  principal,
								  _createdAt,
								  _systemWideRoles);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SECURITY PRINCIPAL																	  
/////////////////////////////////////////////////////////////////////////////////////////
	public SecurityPrincipalOID getSecurityPrincipalOid() {
		return _securityPrincipal != null ? _securityPrincipal.getOid()
							 : null;
	}
	public SecurityPrincipalID getSecurityPrincipalId() {
		return _securityPrincipal != null ? _securityPrincipal.getId()
							 			  : null;
	}
	public IdentityProvider getSecurityPrincipalSourceIdentityProvider() {
		return _securityPrincipal != null ? _securityPrincipal.getSourceIdentityProvider()
							 	  : null;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public Collection<SecurityRoleInModule> getSecurityPrincipalSystemWideRoles() {
		return _systemWideRoles;
	}
	public Collection<SecurityRole> getSecurityPrincipalSystemWideRolesIn(final AppComponent module) {
		return this.getSystemWideRolesIn(module);
	}
	public boolean hasSecurityPrincipalAnySystemWideRoleIn(final AppComponent module) {
		return this.hasAnySystemWideRoleIn(module);
	}
	public boolean hasSecurityPrincipalSystemWideRoleIn(final AppComponent module,final SecurityRole userRole) {
		return this.hasSystemWideRoleIn(module,userRole);
	}
}
