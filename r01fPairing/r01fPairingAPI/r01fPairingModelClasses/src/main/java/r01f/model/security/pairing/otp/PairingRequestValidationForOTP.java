package r01f.model.security.pairing.otp;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.model.security.otp.task.validation.OTPValidationResponse;
import r01f.model.security.pairing.IsPairingRequestValidationResult;
import r01f.model.security.pairing.PairingRequestValidationResultBase;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.validation.ObjectValidationResultBuilder;
import r01f.validation.ObjectValidationResults.ObjectValidationResultNOK;
import r01f.validation.ObjectValidationResults.ObjectValidationResultOK;

@Accessors(prefix="_")
@MarshallType(as="pairingRequestValidationForOTP") 
public class PairingRequestValidationForOTP<P extends IsPairingRequestUsingOTP>
	 extends PairingRequestValidationResultBase<P>
  implements IsPairingRequestValidationResult<P> {
	
/////////////////////////////////////////////////////////////////////////////////////////
// 	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final P _pairingRequest;
	@Getter private final OTPValidationResponse _otpValidationResponse;
	
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public PairingRequestValidationForOTP(final Class<P> pariringRequesModelObjectType,
										  final P pairingRequest,
										  final OTPValidationResponse response) {
		super(pariringRequesModelObjectType);
		_otpValidationResponse = response;
		_pairingRequest = pairingRequest;
	}
	
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public P getValidatedObject() {	
		return _pairingRequest;
	}
	@Override
	public boolean isValid() {		//
		return _otpValidationResponse.isSecretValidated();
	}
	@Override
	public boolean isNOTValid() {
		return ! isValid();
	}
	@Override
	public ObjectValidationResultOK<P> asOKValidationResult() {
		return ObjectValidationResultBuilder.on(_pairingRequest)
											.isValid()
											.asOKValidationResult();
	}
	@Override
	public ObjectValidationResultNOK<P> asNOKValidationResult() {
		return ObjectValidationResultBuilder.on(_pairingRequest)
											.isNotValidBecause("OTP no valid")
											.asNOKValidationResult();
	}

}
