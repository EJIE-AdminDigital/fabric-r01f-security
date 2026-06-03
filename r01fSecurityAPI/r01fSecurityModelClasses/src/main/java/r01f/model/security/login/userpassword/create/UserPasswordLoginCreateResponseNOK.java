package r01f.model.security.login.userpassword.create;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;

@Accessors(prefix="_")
public class UserPasswordLoginCreateResponseNOK 
	 extends UserPasswordLoginCreateResponseBase {

	private static final long serialVersionUID = -1844535635371508878L;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="errorType",
			   	   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private UserPasswordLoginCreateErrorType _errorType;
}
