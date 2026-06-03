package r01f.model.security.login.userpassword.reset;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="userPasswordLoginResetResponseNOK")
@Accessors(prefix="_")
public class UserPasswordLoginResetResponseNOK
	 extends UserPasswordLoginResetResponseBase {

	private static final long serialVersionUID = -6088053849496707570L;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="errorType",
			   	   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private UserPasswordLoginResetResponseErrorType _errorType;

}
