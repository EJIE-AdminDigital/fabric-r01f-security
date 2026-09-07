package r01f.model.security.otp.validation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.securitycontext.SecurityIDS.Password;



@Accessors(prefix="_")
@RequiredArgsConstructor
abstract class OTPValidationRequestBase<T>
    implements OTPValidationRequest<T> {

	private static final long serialVersionUID = 1206292544725125704L;
///////////////////////////////////////////////////////////////////////////////////////
// 	FIELDS
///////////////////////////////////////////////////////////////////////////////////////
	@Getter protected final AppCode _appCode;
	@Getter protected final AppComponent _appComponent;
	@Getter protected final OTPTaskOID _taskOid;

	@Getter protected final T _key;
	@Getter protected final Password _secret;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public <V extends OTPValidationRequest<?>> V as(final Class<V> type) {
		return (V)this;
	}
}