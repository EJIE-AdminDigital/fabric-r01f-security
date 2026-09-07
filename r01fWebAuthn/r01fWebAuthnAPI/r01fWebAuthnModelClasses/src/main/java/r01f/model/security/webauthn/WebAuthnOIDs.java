package r01f.model.security.webauthn;

import lombok.Getter;
import r01f.annotations.Immutable;
import r01f.guids.JavaOIDDispenser;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.patterns.FactoryFrom;
import r01f.securitycontext.SecurityOIDs.IsSecurityObjectOID;

public abstract class WebAuthnOIDs {

/////////////////////////////////////////////////////////////////////////////////////////
// 	WEB AUTHN Base ID's
/////////////////////////////////////////////////////////////////////////////////////////
	@Immutable
	@MarshallType(as="loginOid")
	public record CredentialOID(@Getter String id)
	   implements IsSecurityObjectOID {

		public static final CredentialOID ALL = CredentialOID.forId("*");

		public static CredentialOID from(final String id) {
			return new CredentialOID(id);
		}
		public static CredentialOID forId(final String id) {
			return new CredentialOID(id);
		}
		public static CredentialOID valueOf(final String s) {
			return CredentialOID.forId(s);
		}
		public static CredentialOID fromString(final String s) {
			return CredentialOID.forId(s);
		}

		public static CredentialOID supply() {
			return CredentialOID.forId(JavaOIDDispenser.generateGUID());
		}
		public static FactoryFrom<String,CredentialOID> FACTORY_FROM_STRING = CredentialOID::forId;
		
		@Override
		public String asString() {
			return this.id;
		}
		@Override
		public String toString() {
			return this.id;
		}
	}
}
