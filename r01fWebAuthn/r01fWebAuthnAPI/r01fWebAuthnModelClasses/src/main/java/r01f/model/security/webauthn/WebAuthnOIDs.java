package r01f.model.security.webauthn;

import r01f.annotations.Immutable;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.patterns.FactoryFrom;
import r01f.securitycontext.SecurityOIDs.SecurityObjectOID;
import r01f.securitycontext.SecurityOIDs.SecurityObjectOIDBase;

public abstract class WebAuthnOIDs {

/////////////////////////////////////////////////////////////////////////////////////////
// 	WEB AUTHN Base ID's
/////////////////////////////////////////////////////////////////////////////////////////
	@Immutable
	@MarshallType(as="loginOid")
	public static final class CredentialOID
					  extends SecurityObjectOIDBase
				   implements SecurityObjectOID {

		private static final long serialVersionUID = -2162289370851262116L;

		public static final CredentialOID ALL = CredentialOID.forId("*");
		public CredentialOID() {
			super();
		}
		public CredentialOID(final String oid) {
			super(oid);
		}
		public static CredentialOID valueOf(final String s) {
			return CredentialOID.forId(s);
		}
		public static CredentialOID fromString(final String s) {
			return CredentialOID.forId(s);
		}
		public static CredentialOID forId(final String id) {
			return new CredentialOID(id);
		}
		public static CredentialOID supply() {
			return CredentialOID.forId(SecurityObjectOIDBase.supplyId());
		}
		public static FactoryFrom<String,CredentialOID> FACTORY_FROM_STRING = new FactoryFrom<>() {
																							@Override
																							public CredentialOID from(final String oid) {
																								return CredentialOID.forId(oid);
																							}
																		 };
	}
}
