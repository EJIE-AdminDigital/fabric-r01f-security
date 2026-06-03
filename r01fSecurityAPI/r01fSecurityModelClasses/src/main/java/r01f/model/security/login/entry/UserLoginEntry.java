package r01f.model.security.login.entry;

import java.time.Instant;

import r01f.model.security.PersistableSecurityObject;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.securitycontext.SecurityIDS.SecurityToken;
import r01f.securitycontext.SecurityIDS.UserLoginEntryID;
import r01f.securitycontext.SecurityOIDs.UserLoginEntryOID;
import r01f.securitycontext.SecurityOIDs.UserOID;

/**
 * A login entry
 */
public interface UserLoginEntry
		 extends PersistableSecurityObject<UserLoginEntryOID> {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
////////// Common fields
	public Instant getDate();
	public void setDate(final Instant date);

	public UserLoginEntryID getId();
	public void setId(final UserLoginEntryID id);

	public SecurityProviderID getSecurityProviderId();
	public void setSecurityProviderId(final SecurityProviderID id);

	public LoginID getLoginId();
	public void setLoginId(final LoginID id);

////////// User
	public UserOID getUserOid();
	public void setUserOid(final UserOID userOid);

////////// Security token used when reseting user-password login's password or when impersonating
	public SecurityToken getToken();
	public void setToken(final SecurityToken token);

////////// Impersonation
	public UserOID getImpersonatedUserOid();
	public void setImpersonatedUserOid(final UserOID userOid);
}
