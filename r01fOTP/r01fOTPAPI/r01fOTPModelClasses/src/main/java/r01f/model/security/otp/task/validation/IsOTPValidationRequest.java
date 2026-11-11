package r01f.model.security.otp.task.validation;

import r01f.model.security.otp.IsOTPObject;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.oids.CommonOIDs.AppCode;
import r01f.oids.CommonOIDs.AppComponent;
import r01f.oids.SecurityIDs.Secret;

public interface IsOTPValidationRequest<T>
		 extends IsOTPObject {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public AppCode getAppCode();
	public AppComponent getAppComponent();
	public OTPTaskOID getTaskOid();

	public T getKey();
	public OTPValidationRequestKey getKeyType();

	public Secret getSecret();

/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	public default <V extends IsOTPValidationRequest<?>> V as(final Class<V> type) {
		return (V)this;
	}
}