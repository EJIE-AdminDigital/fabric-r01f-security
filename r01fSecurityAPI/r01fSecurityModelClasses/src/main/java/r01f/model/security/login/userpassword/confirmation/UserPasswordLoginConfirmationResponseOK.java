package r01f.model.security.login.userpassword.confirmation;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.securitycontext.SecurityIDS.SecurityToken;

@Accessors(prefix="_")
public class UserPasswordLoginConfirmationResponseOK extends UserPasswordLoginConfirmationResponseBase {

	private static final long serialVersionUID = -6130794346932496419L;

/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="token",escape=true)
	@Getter @Setter private SecurityToken _token;
}
