package r01f.model.security.login.pairedphone;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.PersistableModelObjectBase;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.securitycontext.SecurityOIDs.PairedPhoneLoginConsentOID;
import r01f.types.contact.Phone;


@Accessors(prefix = "_")
public abstract class PairedPhoneLoginConsentBase
			  extends PersistableModelObjectBase<PairedPhoneLoginConsentOID,PairedPhoneLoginConsentBase> 
   		   implements PairedPhoneLoginConsent  {

	private static final long serialVersionUID = -6533166207979719203L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	@MarshallField(as="pairedPhone", whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Phone  _pairedPhone;
	
	@MarshallField(as="request")
	@Getter	@Setter	private PairedPhoneLoginConsentRequest _request;

	@MarshallField(as="response")
	@Getter	@Setter	private PairedPhoneLoginConsentResponse _response;

	@MarshallField(as="status",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter	@Setter	private PairedPhoneLoginConsentStatus _status;
}
