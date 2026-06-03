package r01f.db.entities.security;

import r01f.services.core.persistence.db.DBEntityHasModelObjectDescriptor;

public interface DBEntityForLogin
	     extends DBEntityForSecurityObject,
		   		 DBEntityHasModelObjectDescriptor {

	public String getUserOid();
	public void setUserOid(final String userOid);
	
	public String getLoginId();
	public void setLoginId(final String loginId);
}
