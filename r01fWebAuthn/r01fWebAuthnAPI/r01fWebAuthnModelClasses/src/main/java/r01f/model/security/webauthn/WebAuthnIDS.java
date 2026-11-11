package r01f.model.security.webauthn;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import r01f.annotations.Immutable;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.SecurityIDs.IsSecurityObjectID;
import r01f.types.StringBase64;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class WebAuthnIDS {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Immutable
	@MarshallType(as="credentialId") // the credential id is binary , so a base64 id will be used!!!
	public record CredentialID(@Getter String id)
	   implements IsSecurityObjectID {

		public static CredentialID from(final StringBase64 id) {
			return new CredentialID(id.asString());
		}
		public static CredentialID forId(final StringBase64 id) {
			return new CredentialID(id.asString());
		}
		public static CredentialID valueOf(final StringBase64 id) {
			return new CredentialID(id.asString());
		}
		public static CredentialID fromString(final StringBase64 id) {
			return new CredentialID(id.asString());
		}
		public static CredentialID fromBase64String(final StringBase64 id) {
			return new CredentialID(id.asString());
		}
		public static CredentialID fromBase64RawString(final String id) {
			return new CredentialID(id);
		}

		@Override
		public String asString() {
			return this.id;
		}
		@Override
		public String toString() {
			return this.id;
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////

}
