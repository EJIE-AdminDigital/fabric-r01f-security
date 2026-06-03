package r01f.client.api.delegate.otp;

import jakarta.inject.Provider;
import r01f.api.interfaces.security.otp.OTPService;
import r01f.model.security.otp.dispatch.OTPDispatchRequest;
import r01f.model.security.otp.dispatch.OTPDispatchResponse;
import r01f.model.security.otp.generation.OTPGenerationRequest;
import r01f.model.security.otp.generation.OTPGenerationResponse;
import r01f.model.security.otp.operations.OTPOperationExecResult;
import r01f.model.security.otp.validation.OTPValidationRequest;
import r01f.model.security.otp.validation.OTPValidationResponse;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.api.delegates.ClientAPIServiceDelegateBase;


public class OTPClientAPIDelegateForOTPServices
	 extends ClientAPIServiceDelegateBase<OTPService> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public OTPClientAPIDelegateForOTPServices(final Provider<SecurityContext> securityContextProvider,
											  final Marshaller modelObjectsMarshaller,
										      final OTPService otpServicesProxy) {
		super(securityContextProvider,
			  modelObjectsMarshaller,
			  otpServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//EXPOSED METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	public OTPOperationExecResult<OTPGenerationResponse> generate(	final OTPGenerationRequest otpRequest) {
		return this.getServiceProxy()
				   .generate(this.getSecurityContext(),
				             otpRequest);
	}

	public <R extends OTPDispatchRequest<?, ?, ?>> OTPOperationExecResult<OTPDispatchResponse> dispatch(final R otpRequest) {
		return this.getServiceProxy()
				   .dispatch(this.getSecurityContext(),
				             otpRequest);
	}
	public <R extends OTPValidationRequest<?>> OTPOperationExecResult<OTPValidationResponse> validate(final R validationReq) {
		return this.getServiceProxy()
				   .validate(this.getSecurityContext(),
						   	 validationReq);
	}
}

