package r01f.model.security.login.data.pairedphone;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.IsLoginData;
import r01f.model.security.login.pairedphone.messages.login.PairedPhoneLoginConsentRequest;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="loginDataForPairedPhone")
@Accessors(prefix="_")
public class LoginDataForPairedPhone 
  implements IsLoginData {
	
	private static final long serialVersionUID = 7865130049797635353L;
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="request")
	@Getter @Setter private PairedPhoneLoginConsentRequest _loginConsentRequest;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginDataForPairedPhone() {
		// default constructor
	}
	public LoginDataForPairedPhone(final PairedPhoneLoginConsentRequest loginConsentRequest) {
		_loginConsentRequest = loginConsentRequest;
	}
}
