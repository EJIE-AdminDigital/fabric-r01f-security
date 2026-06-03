package r01f.model.security.authorization;

import r01f.model.security.PersistableSecurityObject;
import r01f.securitycontext.SecurityIDS.UserRole;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.securitycontext.SecurityOIDs.UserAuthorizationOnResourceOID;
import r01f.securitycontext.SecurityOIDs.UserOID;

/**
 * Models an user's authorization on a target resource
 */
public interface UserAuthorizationOnResource
		 extends PersistableSecurityObject<UserAuthorizationOnResourceOID> {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	public UserOID getUserOid();
	public void setUserOid(final UserOID userOid);

	public AuthorizationTargetResourceOID getAuthorizationTargetResourceOid();
	public void setAuthorizationTargetResourceOid(final AuthorizationTargetResourceOID authorizationTargetResourceOid);

	public UserRole getRole();
	public void setRole(final UserRole role);
}
