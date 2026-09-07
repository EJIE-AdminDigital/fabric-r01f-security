package r01f.model.security.otp.validation;

import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.model.security.otp.OTPObject;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.securitycontext.SecurityIDS.Password;

public interface OTPValidationRequest<T>
		 extends OTPObject {

	public AppCode getAppCode();
	public AppComponent getAppComponent();
	public OTPTaskOID getTaskOid();

	public T getKey();
	public OTPValidationRequestKey getKeyType();

	public Password getSecret();

	public <V extends OTPValidationRequest<?>> V as(final Class<V> type);
}