package r01f.model.security.login.userpassword;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.login.userpassword.factors.LoginAuthFactor;
import r01f.model.security.user.User;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.securitycontext.SecurityIDS.LoginID;

@MarshallType(as="userPasswordLoginResponsePendingOnAuthFactor")
@Accessors(prefix="_")
public class UserPasswordLoginResponsePendingOnAuthFactor<U extends User>
  	 extends UserPasswordLoginResponseBase {

	private static final long serialVersionUID = 3781076322206964020L;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="loginId",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter private LoginID _loginId;	
		
	@MarshallField(as="pendingAuthFactor")
    @Getter @Setter private LoginAuthFactor _authFactor;	
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean isPendingOnAuthFactor() {	
		return true;
	}
}
