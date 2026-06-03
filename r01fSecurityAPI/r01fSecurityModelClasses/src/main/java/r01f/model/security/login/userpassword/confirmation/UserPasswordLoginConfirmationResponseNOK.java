package r01f.model.security.login.userpassword.confirmation;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;

@Accessors(prefix="_")
public class UserPasswordLoginConfirmationResponseNOK extends UserPasswordLoginConfirmationResponseBase {

	private static final long serialVersionUID = 5808855626721675744L;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="errorType",
			   	   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private UserPasswordLoginConfirmationErrorType _errorType;
}
