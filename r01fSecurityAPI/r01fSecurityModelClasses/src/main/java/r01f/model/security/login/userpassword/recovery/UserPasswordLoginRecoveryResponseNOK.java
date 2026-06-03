package r01f.model.security.login.userpassword.recovery;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="userPasswordRecoveryResponseNOK")
@Accessors(prefix="_")
public class UserPasswordLoginRecoveryResponseNOK
     extends UserPasswordLoginRecoveryResponseBase {

	private static final long serialVersionUID = 5657620044763515411L;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="errorType",
			   	   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private UserPasswordLoginRecoveryErrorType _errorType;
}
