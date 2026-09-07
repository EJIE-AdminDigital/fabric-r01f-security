package r01f.model.security.pushtoken;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.annotations.Immutable;
import r01f.enums.EnumWithCode;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.securitycontext.SecurityIDS.IsSecurityObjectID;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class PushTokenTypeIDs {
///////////////////////////////////////////////////////////////////////////////////
// TYPE ID
/////////////////////////////////////////////////////////////////////////////////
	@Immutable
	@MarshallType(as="phoneTokenTypeID")
	public record PushTokenTypeID(@Getter String id)
	   implements IsSecurityObjectID {
	   
		public static PushTokenTypeID from(final String typeAsString) {
			return new PushTokenTypeID(typeAsString);
		}
		public static PushTokenTypeID forId(final String typeAsString) {
			return new PushTokenTypeID(typeAsString);
		}
		public static PushTokenTypeID valueOf(final String typeAsString) {
			return new PushTokenTypeID(typeAsString);
		}
		public static PushTokenTypeID fromString(final String typeAsString) {
			return new PushTokenTypeID(typeAsString);
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
	@Accessors(prefix="_")
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public enum PhoneTokenTypes
	 implements EnumWithCode<PushTokenTypeID,PhoneTokenTypes> {

		PHONE (PushTokenTypeID.forId("phone")),
		CLIENT_INSTALLATION_OID(PushTokenTypeID.forId("client-intallation-oid"));

		@Getter private final PushTokenTypeID  _code;

		@Getter private final Class<PushTokenTypeID> _codeType = PushTokenTypeID.class;

	}


}
