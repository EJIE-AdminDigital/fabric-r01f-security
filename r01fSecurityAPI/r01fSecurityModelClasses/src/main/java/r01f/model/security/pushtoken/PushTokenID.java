package r01f.model.security.pushtoken;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.guids.CommonOIDs.SystemID;
import r01f.model.security.pushtoken.PushTokenTypeIDs.PushTokenTypeID;
import r01f.model.security.pushtoken.PushTokenTypeIDs.PhoneTokenTypes;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallFrom;
import r01f.objectstreamer.annotations.MarshallPolymorphicTypeInfo;
import r01f.objectstreamer.annotations.MarshallPolymorphicTypeInfo.MarshalTypeInfoIncludeCase;
import r01f.securitycontext.SecurityOIDs.SecurityObjectOID;
import r01f.securitycontext.SecurityOIDs.SecurityObjectOIDBase;
import r01f.types.contact.Phone;

@MarshallPolymorphicTypeInfo(includeTypeInfo=MarshalTypeInfoIncludeCase.ALWAYS)
public interface PushTokenID 
		 extends SecurityObjectOID {

	public PushTokenTypeID getTypeId();
	
	public <T extends PushTokenID> T as(final Class<T> type) ;
///////////////////////////////////////////////////////////////////////////////
// BASE CLASS
///////////////////////////////////////////////////////////////////////////////
	
	@Accessors(prefix="_")
	public abstract class PushTokenIDBase 
			extends SecurityObjectOIDBase
	  	implements PushTokenID {
		
		private static final long serialVersionUID = 3688527706291266889L;

		@MarshallField(as="type",
						whenXml=@MarshallFieldAsXml(attr=true))
		@Getter private final PushTokenTypeID _typeId;
		
		
		public PushTokenIDBase(final String baseId,
			              	   final PushTokenTypeID typeId) {
			super(baseId);
			_typeId = typeId;
		}
		
		@Override @SuppressWarnings("unchecked")
		public <T extends PushTokenID> T as(final Class<T> type) {
			return (T) this;
		}	
	
	}
///////////////////////////////////////////////////////////////////////////////
//  PhoneTokenID Impls
///////////////////////////////////////////////////////////////////////////////
	@Accessors(prefix="_")
	public class PushTokenIDByPhone
		 extends PushTokenIDBase
	  implements PushTokenID {

		private static final long serialVersionUID = 3860396344655279156L;
		
		@MarshallField(as="phone",
					   whenXml=@MarshallFieldAsXml(attr=true))
		@Getter private final Phone _phone;
		///////////////////////////////////////////////////////
		// CONSTRUCTOR
		///////////////////////////////////////////////////////
		public PushTokenIDByPhone(@MarshallFrom("phone") final Phone phone) {
			super(  phone.asString(),
					PhoneTokenTypes.PHONE.getCode());
			_phone = phone;
		}
		
		public PushTokenIDByPhone(final String phoneAsString) {
			this(Phone.of(phoneAsString));
		
		}
		public static PushTokenIDByPhone forPhone(final Phone phone) {
			return new PushTokenIDByPhone(phone);
		}
		
		@Override
		public String toString() {
			return _phone.asString();
		}
	}
	
}
