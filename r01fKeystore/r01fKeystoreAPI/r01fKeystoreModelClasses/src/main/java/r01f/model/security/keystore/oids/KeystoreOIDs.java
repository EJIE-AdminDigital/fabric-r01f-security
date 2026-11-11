package r01f.model.security.keystore.oids;

import lombok.Getter;
import r01f.annotations.Immutable;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.JavaOIDDispenser;
import r01f.oids.OIDTyped;
import r01f.oids.PersistableObjectOID;

public abstract class KeystoreOIDs {

/////////////////////////////////////////////////////////////////////////////////////////
// Base marker interface
/////////////////////////////////////////////////////////////////////////////////////////

	public static interface IsKeystoreObjectOID
					extends PersistableObjectOID,OIDTyped<String> {
		public default String convertToDatabaseColumn(final IsKeystoreObjectOID attribute) {
			return attribute.asString();
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Immutable
	@MarshallType(as="keystoreOid")
	public record KeystoreOID(@Getter String id)
	   implements IsKeystoreObjectOID {
		
		public static KeystoreOID from(final String id) {
			return new KeystoreOID(id);
		}
		public static KeystoreOID forId(final String id) {
			return new KeystoreOID(id);
		}
		public static KeystoreOID valueOf(final String s) {
			return KeystoreOID.forId(s);
		}
		public static KeystoreOID fromString(final String s) {
			return KeystoreOID.forId(s);
		}
		public static KeystoreOID supply() {
			return KeystoreOID.forId(JavaOIDDispenser.supplyOid());
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
}
