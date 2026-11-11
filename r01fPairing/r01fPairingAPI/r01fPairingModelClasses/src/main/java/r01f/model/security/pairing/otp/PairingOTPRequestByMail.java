package r01f.model.security.pairing.otp;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.contact.EMail;

@MarshallType(as="pairingOTPRequestByMail")
@Accessors(prefix="_") 
public class PairingOTPRequestByMail
	 extends PairingOTPRequestBase  
  implements IsPairingOTPRequest {
	
	private static final long serialVersionUID = 8501664132231161907L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	@MarshallField(as="mail",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private EMail _mail;

}
