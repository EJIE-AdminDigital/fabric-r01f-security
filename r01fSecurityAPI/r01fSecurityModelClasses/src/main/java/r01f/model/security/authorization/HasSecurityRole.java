package r01f.model.security.authorization;

import r01f.oids.SecurityIDs.SecurityRole;

public interface HasSecurityRole {
	
	public SecurityRole getRole();
	
	public default void setRole(final SecurityRole role) {
		throw new UnsupportedOperationException();
	}
}
