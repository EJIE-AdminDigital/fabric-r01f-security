package r01f.model.security.login.userpassword.create;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.securitycontext.SecurityIDS.SecurityToken;

@Accessors(prefix="_")
public class UserPasswordLoginCreateResponseOK 
	 extends UserPasswordLoginCreateResponseBase {

	private static final long serialVersionUID = -1399257424268066767L;
	/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="token",escape=true)
	@Getter @Setter private SecurityToken _token;
}
