package r01f.model.security.pushtoken;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.annotations.Immutable;
import r01f.enums.EnumWithCode;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.securitycontext.SecurityIDS.SecurityIDBase;

public class PushTokenTypeIDs {
///////////////////////////////////////////////////////////////////////////////////
// TYPE ID
/////////////////////////////////////////////////////////////////////////////////
	@Immutable
	@MarshallType(as="phoneTokenTypeID")
	@NoArgsConstructor
	public static class PushTokenTypeID
		extends SecurityIDBase {
	
		private static final long serialVersionUID = -2133539237433417021L;
		
		public PushTokenTypeID(final String typeAsString) {
			super(typeAsString);
		}
		public static PushTokenTypeID forId(final String typeAsString) {
			return new PushTokenTypeID(typeAsString);
		}
	}
	
	
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
