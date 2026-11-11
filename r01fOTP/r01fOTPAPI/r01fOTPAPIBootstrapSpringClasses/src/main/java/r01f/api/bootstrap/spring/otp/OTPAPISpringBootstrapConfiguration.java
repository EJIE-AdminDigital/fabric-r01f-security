package r01f.api.bootstrap.spring.otp;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.inject.Provider;
import r01f.api.interfaces.security.otp.IsOTPServiceInterface;
import r01f.api.interfaces.security.otp.persistence.IsOTPPersistenceServiceInterface;
import r01f.client.api.otp.OTPServiceAPI;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.services.bootstrap.api.spring.APISpringBeanFinder;
import r01f.services.interfaces.ServiceInterface;
import r01f.spring.IsSpringConfiguration;


@Configuration 
public class OTPAPISpringBootstrapConfiguration 
  implements IsSpringConfiguration {
////////////////////////////////////////////////////////////////////////
///  API
////////////////////////////////////////////////////////////////////////
	@Bean @SuppressWarnings("rawtypes")
	public OTPServiceAPI otpAPI(final ApplicationContext springContext) {  
		// [1] - marshaller
		Marshaller modelObjectsMarshaller = APISpringBeanFinder.findMarshallerAt(springContext);
		
		
		// [2] - find the service interface mappings for the base service interface
		@SuppressWarnings("unchecked")
		Map<Class,ServiceInterface> srvcIfaceMappings = APISpringBeanFinder.serviceInterfaceCOREImplsFinderAt(springContext)
													   						.findServiceInterfaceCOREImplsOFBaseServiceInterfaces(IsOTPServiceInterface.class,
													   								                                              IsOTPPersistenceServiceInterface.class);
		// [3] - find the security context provider
		Provider<SecurityContext> securityContextProvider = APISpringBeanFinder.findSecurityContextProviderAt(springContext);
		
		return new OTPServiceAPI(securityContextProvider,
											  modelObjectsMarshaller,
											  srvcIfaceMappings);
	}
}