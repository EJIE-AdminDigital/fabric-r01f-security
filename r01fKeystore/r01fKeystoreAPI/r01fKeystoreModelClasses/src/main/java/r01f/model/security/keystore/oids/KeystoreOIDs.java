package r01f.model.security.keystore.oids;

import java.util.UUID;

import lombok.NoArgsConstructor;
import r01f.annotations.Immutable;
import r01f.guids.OIDBaseMutable;
import r01f.guids.OIDTyped;
import r01f.guids.PersistableObjectOID;
import r01f.model.security.keystore.KeystoreGUIDDispenser;
import r01f.objectstreamer.annotations.MarshallType;

public abstract class KeystoreOIDs {

/////////////////////////////////////////////////////////////////////////////////////////
// Base marker interface
/////////////////////////////////////////////////////////////////////////////////////////

	public static interface KeystoreObjectOID
					extends PersistableObjectOID,OIDTyped<String> {
		// just a marker interface
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	OID base
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	* Base for every User oid objects
	*/
	@Immutable
	@NoArgsConstructor
	public static abstract class KeystoreObjectOIDBase
						 extends OIDBaseMutable<String>
					  implements KeystoreObjectOID {

		private static final long serialVersionUID = 7778880230670929915L;

		public KeystoreObjectOIDBase(final String id) {
			super(id);
		}
		/**
		* Generates an oid
		* @return
		*/
		protected static String supplyId() {
			UUID uuid = UUID.randomUUID();
			return uuid.toString();
		}
		public String convertToDatabaseColumn(final KeystoreObjectOID attribute) {
			return attribute.asString();
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Immutable
	@NoArgsConstructor
	@MarshallType(as="keystoreOid")
	public static final class KeystoreOID
					  extends KeystoreObjectOIDBase {
		private static final long serialVersionUID = -2162289370851262116L;
		public KeystoreOID(final String oid) {
			super(oid);
		}
		public static KeystoreOID valueOf(final String s) {
			return KeystoreOID.forId(s);
		}
		public static KeystoreOID fromString(final String s) {
			return KeystoreOID.forId(s);
		}
		public static KeystoreOID forId(final String id) {
			return new KeystoreOID(id);
		}
		public static KeystoreOID supply() {
			return KeystoreOID.forId(KeystoreGUIDDispenser.generateGUID());
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////

}
