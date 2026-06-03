package r01f.model.security.pairing.otp;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.securitycontext.SecurityIDS.Password;
import r01f.types.contact.EMail;
import r01f.types.contact.Phone;

@Accessors(prefix="_")
@MarshallType(as="otpRequestMock")
public  class OTPRequestMock
	extends OTPRequestBase
  implements OTPRequest {

	private static final long serialVersionUID = 8501664132231161907L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	@MarshallField(as="phone",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Phone _phone;

	@MarshallField(as="mail",
			   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private EMail _mail;

	@MarshallField(as="fixedMockOTP",
			   whenXml=@MarshallFieldAsXml(attr=true))
    @Getter @Setter private Password _fixedMockOTP;

}
