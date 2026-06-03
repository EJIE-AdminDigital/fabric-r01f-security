package r01f.model.security.login.userpassword;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="userPasswordResponseOK")
@Accessors(prefix="_")
public class UserPasswordLoginResponseNOK
	 extends UserPasswordLoginResponseBase {

	private static final long serialVersionUID = -4759318960830510701L;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="errorType",
			   	   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private UserPasswordLoginErrorType _errorType;

}
