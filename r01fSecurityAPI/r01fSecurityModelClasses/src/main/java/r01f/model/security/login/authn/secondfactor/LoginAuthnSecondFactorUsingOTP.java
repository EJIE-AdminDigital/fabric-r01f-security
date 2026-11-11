package r01f.model.security.login.authn.secondfactor;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.contact.EMail;
import r01f.types.contact.IsContactMean;
import r01f.types.contact.Phone;

@MarshallType(as="loginAuthFactorUsingOTP")
@Accessors(prefix="_") 
public class LoginAuthnSecondFactorUsingOTP
  implements IsLoginAuthnSecondFactor {

	private static final long serialVersionUID = -2612106921535610510L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="contactMean")
	@Getter @Setter private IsContactMean _contactMean;
	
	@MarshallField(as="contactMeanType")
	@Getter @Setter private Class<? extends IsContactMean> _contactMeanType;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginAuthnSecondFactorUsingOTP(final Class<? extends IsContactMean> contactMeanType) {
		_contactMeanType = contactMeanType;
	}
	public LoginAuthnSecondFactorUsingOTP(final Class<? extends IsContactMean> contactMeanType,final IsContactMean contactMean) {
		if (contactMean == null) throw new IllegalArgumentException("The contact mean is mandatory");
		_contactMeanType = contactMeanType;
		_contactMean = contactMean;
	}
	public static LoginAuthnSecondFactorUsingOTP forEMail(final EMail email) {
		return new LoginAuthnSecondFactorUsingOTP(EMail.class,email);
	}
	public static LoginAuthnSecondFactorUsingOTP forPhone(final Phone phone) {
		return new LoginAuthnSecondFactorUsingOTP(Phone.class,phone);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public boolean requiresDelivering() {	
		return true;
	}
	@Override
	public boolean usesOTP() {	
		return true;
	}
	public boolean isOTPDeliveredBySms() {		
		return _contactMean != null
			&& _contactMean instanceof Phone;
	}	
	public boolean isOTPDeliveredByMail() {
		return _contactMean != null
			&& _contactMean instanceof EMail;
	} 
/////////////////////////////////////////////////////////////////////////////////////////
//	CASTING
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override @SuppressWarnings("unchecked")
	public <F extends IsLoginAuthnSecondFactor> F as(final Class<F> type) {
		return (F)this;
	}
}