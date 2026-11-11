package r01f.model.security.pairing.otp;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.contact.Phone;

@MarshallType(as="pairingOTPRequestBySms")
@Accessors(prefix="_") 
public  class PairingOTPRequestBySms
	extends PairingOTPRequestBase 
  implements IsPairingOTPRequest {
	
	private static final long serialVersionUID = 8501664132231161907L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	@MarshallField(as="phone",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Phone _phone;
}
