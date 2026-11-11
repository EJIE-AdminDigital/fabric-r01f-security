package r01f.model.security.webauthn;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.SecurityPersistableObjectBase;
import r01f.model.security.keystore.key.PublicKey;
import r01f.model.security.webauthn.WebAuthnIDS.CredentialID;
import r01f.model.security.webauthn.WebAuthnOIDs.CredentialOID;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;

/**
 * Authenticator Credential
 */
@NoArgsConstructor
@Accessors(prefix="_")
public abstract class AuthenticatorCredentialBase<SELF_TYPE extends AuthenticatorCredentialBase<SELF_TYPE>>
	 		  extends SecurityPersistableObjectBase<CredentialOID,SELF_TYPE>
		implements AuthenticatorCredential {

	private static final long serialVersionUID = 5459928097421233043L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * The credential id
	 */
	@MarshallField(as="credentialId",
			   	   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter protected CredentialID _credentialId;
	/**
	 * The [user] linked to this [credentials]
	 */
	@MarshallField(as="userOid",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter protected SecurityPrincipalOID _userOid;

	////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="publickey",escape=true)
	@Getter @Setter protected PublicKey  _publicKey;

	@MarshallField(as="signatureCount",
			       whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Long _signatureCount;

	@MarshallField(as="backMappedSecurityProviderOnRegistration", // the security provider used by the user before registration
		           whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter public IdentityProvider _backMappedSecurityProviderOnRegistration;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public AuthenticatorCredentialBase(final CredentialOID loginOid) {
		_oid = loginOid;
	}
	public AuthenticatorCredentialBase(final SecurityPrincipalOID userOid,final CredentialID loginId) {
		this(null,
			 userOid,loginId);
	}
	public AuthenticatorCredentialBase(final CredentialOID loginOid,
					                   final SecurityPrincipalOID userOid,final CredentialID loginId) {
		_oid = loginOid;
		_userOid = userOid;
		_credentialId = loginId;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
}
