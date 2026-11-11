package r01f.model.security.pushtoken;

import lombok.Getter;
import r01f.enums.EnumExtended;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallFrom;
import r01f.objectstreamer.annotations.MarshallPolymorphicTypeInfo;
import r01f.objectstreamer.annotations.MarshallPolymorphicTypeInfo.MarshalTypeInfoIncludeCase;
import r01f.oids.SecurityIDs.IsSecurityObjectID;
import r01f.types.contact.Phone;

@MarshallPolymorphicTypeInfo(includeTypeInfo=MarshalTypeInfoIncludeCase.ALWAYS)
public interface IsPushDestinationID 
		 extends IsSecurityObjectID {

	public PushDestinationType getType();
	
	@SuppressWarnings("unchecked")
	public default <T extends IsPushDestinationID> T as(final Class<T> type) {
		return (T)this;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public enum PushDestinationType
	 implements EnumExtended<PushDestinationType> {
		PHONE,
		CLIENT_INSTALLMENT_OID;
	}
///////////////////////////////////////////////////////////////////////////////
//  PhoneTokenID Impls
///////////////////////////////////////////////////////////////////////////////
	public record PushTokenIDByPhone(@MarshallField(as="phone",
					   								whenXml=@MarshallFieldAsXml(attr=true))
									 @Getter Phone phone,
									 @MarshallField(as="type",
									 				whenXml=@MarshallFieldAsXml(attr=true))
									 @Getter PushDestinationType type)
	   implements IsPushDestinationID {
		
		public PushTokenIDByPhone(@MarshallFrom("phone") final Phone phone) {
			this(phone,
				 PushDestinationType.PHONE);
		}
		public PushTokenIDByPhone(final String phoneAsString) {
			this(Phone.of(phoneAsString));
		}
		public static PushTokenIDByPhone forPhone(final Phone phone) {
			return new PushTokenIDByPhone(phone);
		}
		@Override
		public String getId() {
			return this.phone.asString();
		}
		@Override
		public String asString() {
			return this.getId();
		}
		@Override
		public String toString() {
			return this.getId();
		}
	}
	
}
