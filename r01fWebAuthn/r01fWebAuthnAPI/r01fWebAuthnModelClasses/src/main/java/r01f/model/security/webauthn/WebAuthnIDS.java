package r01f.model.security.webauthn;

import lombok.NoArgsConstructor;
import r01f.annotations.Immutable;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.securitycontext.SecurityIDS.SecurityIDBase;
import r01f.types.StringBase64;

public abstract class WebAuthnIDS {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Immutable
	@MarshallType(as="credentialId") // the credential id is binary , so a base64 id will be used!!!
	@NoArgsConstructor
	public static final class CredentialID
					  extends SecurityIDBase {
		private static final long serialVersionUID = -2959560256371887489L;
		public CredentialID(final String str) {
			super(str);
		}
		public static CredentialID forId(final StringBase64 id) {
			return new CredentialID(id.asString());
		}
		public static CredentialID valueOf(final StringBase64 id) {
			return new CredentialID(id.asString());
		}
		public static CredentialID fromBase64String(final StringBase64 id) {
			return new CredentialID(id.asString());
		}
		public static CredentialID fromBase64RawString(final String id) {
			return new CredentialID(id);
		}

	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////

}
