package r01f.model.security.login.authn.secondfactor.messages.validation;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.model.security.IsSecurityObject;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="loginAuthFactorValidationResponse")
@Accessors(prefix="_")
public class LoginAuthnSecondFactorValidationResponse 
  implements IsSecurityObject {

	private static final long serialVersionUID = -7363588795242452569L;
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
	public LoginAuthnSecondFactorValidationResponse(final boolean secretValidated,final long remainingRetryNum) {		
		_secretValidated = secretValidated;
		_remainingRetryNum = remainingRetryNum;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public boolean wasSecretValidated() {
		return _secretValidated;
	}
}
