package r01f.model.security.login.userpassword.factors;

import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.contact.ContactMean;

@Accessors(prefix="_")
@MarshallType(as="loginAuthFactorUsingOTPBase") 
public abstract class LoginAuthFactorUsingOTPBase
  implements LoginAuthFactor {
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override @SuppressWarnings("unchecked")
	public <R extends LoginAuthFactor> R as(final Class<R> classType) {
		return (R) this;
	}
	@Override
	public boolean requiresDelivering() {	
		return true;
	}
	@Override
	public boolean isByOTP() {	
		return true;
	}
	public boolean isBySms() {		
		return this instanceof LoginAuthFactorUsingOTPBySMS;
	}	
	public boolean isByMail() {
		return this instanceof  LoginAuthFactorUsingOTPByEMail;
	} 
	public  ContactMean  contactMean() {
		if ( this.isByMail())
			return this.as(LoginAuthFactorUsingOTPByEMail.class).getMail();
		return this.as(LoginAuthFactorUsingOTPBySMS.class).getPhone();
	}
}
