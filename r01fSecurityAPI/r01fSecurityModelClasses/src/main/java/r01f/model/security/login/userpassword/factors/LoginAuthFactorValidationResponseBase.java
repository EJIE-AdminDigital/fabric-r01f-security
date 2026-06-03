package r01f.model.security.login.userpassword.factors;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="loginAuthFactorValidationResponseBase")
@Accessors(prefix="_")
public abstract class LoginAuthFactorValidationResponseBase
     implements LoginAuthFactorValidationResponse {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="secretValidated",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter public final boolean _secretValidated;

	@MarshallField(as="remainingRetryNum",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter public final long _remainingRetryNum;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginAuthFactorValidationResponseBase(final boolean secretValidated,final long remainingRetryNum) {		
		_secretValidated = secretValidated;
		_remainingRetryNum = remainingRetryNum;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean wasSecretValidated() {
		return _secretValidated;
	}
}