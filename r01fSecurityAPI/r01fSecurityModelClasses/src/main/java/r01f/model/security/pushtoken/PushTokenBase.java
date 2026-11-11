package r01f.model.security.pushtoken;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.PersistableModelObjectBase;
import r01f.model.security.pushtoken.IsPushDestinationID.PushTokenIDByPhone;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.oids.CommonOIDs.SystemID;
import r01f.oids.SecurityIDs.SecurityToken;
import r01f.types.contact.Phone;
import r01f.validation.ObjectValidationResult;
import r01f.validation.SelfValidates;

@Accessors(prefix="_")
public abstract class PushTokenBase<SELF_TYPE extends PushTokenBase<SELF_TYPE>> 
     		  extends PersistableModelObjectBase<PushTokenOID,SELF_TYPE> 
  		   implements IsPushToken,
  		   			  SelfValidates<SELF_TYPE> {
	
	private static final long serialVersionUID = 1558766009933119379L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="sourceSystemId",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private SystemID _sourceSystemId;
	
	@MarshallField(as="destinationId",
			       whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private IsPushDestinationID  _destinationId;
	
	@MarshallField(as="destinationPlatform",
	         	   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private PushDestinationPlatform _destinationPlatform;
	
	@MarshallField(as="token",escape = true)
	@Getter @Setter private SecurityToken _token;	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public PushTokenBase() {
		// default no-args constructor
	}
	public PushTokenBase(final SystemID systemId,
						 final Phone phone) {
		this(systemId,
			 PushTokenIDByPhone.forPhone(phone),
		     null);		// no token
	}
	public PushTokenBase(final SystemID systemId,
						 final Phone phone, 
						 final SecurityToken token) {
		this(systemId,
			 PushTokenIDByPhone.forPhone(phone),
			 token);		
	}

	public PushTokenBase(final SystemID systemId,
					  	 final IsPushDestinationID phone, 
					  	 final SecurityToken token) {
		_sourceSystemId = systemId;
		_destinationId = phone;
		_token = token;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	VALIDATION
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public ObjectValidationResult<SELF_TYPE> validate() {
		return PushTokenValidators.<SELF_TYPE>createPhoneTokenObjectValidator()
								  .validate((SELF_TYPE)this);
	}
}
