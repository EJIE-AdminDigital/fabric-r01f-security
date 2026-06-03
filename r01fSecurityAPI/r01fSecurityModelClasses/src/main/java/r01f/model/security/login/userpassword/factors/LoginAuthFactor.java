package r01f.model.security.login.userpassword.factors;

import r01f.objectstreamer.annotations.MarshallPolymorphicTypeInfo;

@MarshallPolymorphicTypeInfo
public interface LoginAuthFactor {

	<F extends LoginAuthFactor> F as(final Class<F> classType);

	public boolean isByOTP();

	public boolean requiresDelivering(); // An auth factor may require delivering some data ( like an otp ) ,
	                                     // .......or not ( just an user known data, f.e birthdata)

}
