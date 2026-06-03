package r01f.client.api.otp;

import java.util.Map;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Provider;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.otp.OTPService;
import r01f.client.api.delegate.otp.OTPClientAPIDelegateForOTPServices;
import r01f.internal.R01FAppCodes;
import r01f.model.annotations.ModelObjectsMarshaller;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.api.ClientAPIImplBase;
import r01f.services.interfaces.ServiceInterface;


@Accessors(prefix="_")
public  class OTPServiceAPI
			  extends ClientAPIImplBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final OTPClientAPIDelegateForOTPServices _otpServices;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@Inject @SuppressWarnings("rawtypes")
	public OTPServiceAPI(                                          final Provider<SecurityContext> securityContextProvider,
			                    @ModelObjectsMarshaller               final Marshaller modelObjectsMarshaller,
							    @Named(R01FAppCodes.R01_APP_CODE_STR) final Map<Class,ServiceInterface> usersServiceInterfaceTypesToImplOrProxy ) {
		// Services proxy
		super(securityContextProvider,
			  modelObjectsMarshaller,
			  usersServiceInterfaceTypesToImplOrProxy);

		// Create  sub or delegate api
		_otpServices = new OTPClientAPIDelegateForOTPServices(this.getSecurityContextProvider(),
								 	                          this.getModelObjectsMarshaller(),
								 	                          this.getServiceInterfaceCoreImplOrProxy(OTPService.class));

	}
}