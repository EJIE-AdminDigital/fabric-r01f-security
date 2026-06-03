package r01f.model.security.login.apikey;

import java.time.Instant;

import r01f.model.security.login.Login;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.types.AppVersion;
import r01f.types.Range;

/**
 /* APIKeyLogin
 *---------------------------------------------------------------------------------------------------------------
 *  The ApiKey has a public key and a private key.
    What identifies the user is the Public key. But the public key cannot be used as an ID (due to its large size),
    so a keccak256 hash (see @ ApiKeyPairBuilder)  is done to get a FingerPrint, so this way the fingerprint becomes the' LoginID'

    A user who uses the apikey when generating the JWT, in his payload [public claims] has two options:
	   1.  Indicate the LoginID (which is the fingerprint, which can save it, or calculate it, etcc )
	   2.  If you do not have the login ID (fingerprint), in the apyload you can indicate a user data (ej. the mail). *
 *
 * @param <SELF_TYPE>
 */
public interface ApiKeyLogin
		 extends Login {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginOID getApiKeyOid();
	public void setApiKeyOid(final LoginOID oid);

	public UserOID getApiKeyOwnerOid();
	public void setApiKeyOwnerOid(final UserOID userOid);

	public AppVersion getApiKeyVersion();
	public void setApiKeyVersion(final AppVersion id);

	public boolean isReadOnly();
	public void setReadOnly(final boolean readOnly);

	public Range<Instant> getApiKeyUsageRange();
	public void setApiKeyUsageRange(final Range<Instant> range);

	public ApiKeyPair getApiKeyPair();
	public void setApiKeyPair(final ApiKeyPair pair);

	public boolean isActiveAt(final Instant date);
	public boolean isActiveNow();

	public void notActiveFrom(final Instant date);
	public void notActiveFromNowOn();

	public default void removePrivateKeyInfo() {
		if (this.getApiKeyPair() != null) this.setApiKeyPair(new ApiKeyPair(this.getApiKeyPair().getPublicKey()));
	}
	public <SELF_TYPE extends ApiKeyLogin> SELF_TYPE cloneWithoutPrivateKeyInfo();
}
