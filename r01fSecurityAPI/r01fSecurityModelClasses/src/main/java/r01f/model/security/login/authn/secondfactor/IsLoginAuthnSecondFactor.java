package r01f.model.security.login.authn.secondfactor;

import r01f.model.security.IsSecurityObject;
import r01f.objectstreamer.annotations.MarshallPolymorphicTypeInfo;

@MarshallPolymorphicTypeInfo
public interface IsLoginAuthnSecondFactor
		 extends IsSecurityObject {
	/**
	 * An auth factor may require:
	 * 		- delivering some data to the uer (an OTP for example)
	 * 		- request the user a known data (f.e birthdate) that does NOT require delivery 
	 */
	public boolean requiresDelivering(); 
	
	public boolean usesOTP();
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public default <F extends IsLoginAuthnSecondFactor> F as(final Class<F> type) {
		return type.cast(this);
	}
	public default LoginAuthnSecondFactorUsingOTP asLoginAuthSecondFactorUsingOTP() {
		return this.as(LoginAuthnSecondFactorUsingOTP.class);
	}
}
