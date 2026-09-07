package r01f.model.security.pushtoken;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.guids.CommonOIDs.SystemID;
import r01f.model.PersistableModelObjectBase;
import r01f.model.metadata.annotations.ModelObjectData;
import r01f.model.security.pushtoken.IsPushTokenID.PushTokenIDByPhone;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.securitycontext.SecurityIDS.SecurityToken;
import r01f.types.contact.Phone;
import r01f.validation.ObjectValidationResult;
import r01f.validation.SelfValidates;

@ModelObjectData(MetaDataForPushToken.class)
@Accessors(prefix="_")
public abstract class PushTokenBase<SELF_TYPE extends PushTokenBase<SELF_TYPE>> 
     		  extends PersistableModelObjectBase<PushTokenOID,SELF_TYPE> 
  		   implements PushToken,
  		   			  SelfValidates<SELF_TYPE> {
	
	private static final long serialVersionUID = 1558766009933119379L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="systemId",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private SystemID _systemId;
	
	@MarshallField(as="tokenId",
			         whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private IsPushTokenID  _pushTokenId;
	
	@MarshallField(as="plataform",
	         whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private PushTokenDevicePlataform  _plataform;	
	
	@MarshallField(as="token")
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
						 final Phone phone, final SecurityToken token) {
		this(systemId,
			PushTokenIDByPhone.forPhone(phone),
			token);		
	}

	public PushTokenBase(final SystemID systemId,
					  	 final IsPushTokenID phone,  final SecurityToken token) {
		_systemId = systemId;
		_pushTokenId = phone;
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
