package r01f.client.api.sub.security.login.google;

import java.util.Map;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import lombok.experimental.Accessors;
import r01f.api.interfaces.security.login.google.CRUDServicesForGoogleLogin;
import r01f.api.interfaces.security.login.google.FindServicesForGoogleLogin;
import r01f.model.security.login.google.GoogleLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.interfaces.ServiceInterface;

/**
 * Client implementation of the User Login Config
 */
@Singleton
@Accessors(prefix="_")
public class GoogleLoginSubApi<L extends GoogleLogin>
	 extends GoogleLoginSubApiBase<L,ClientAPIDelegateForGoogleLoginCRUDServices<L>, ClientAPIDelegateForGoogleLoginFindServices<L>> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public GoogleLoginSubApi(final Provider<SecurityContext>  securityContextProvider,
							 final Marshaller modelObjectsMarshaller,
							 final Map<Class,ServiceInterface> serviceInterfaceTypesToImplOrProxy,
							 final Class<? extends CRUDServicesForGoogleLogin<L>> crudType,
							 final Class<? extends FindServicesForGoogleLogin<L>> findType)  {
		super(securityContextProvider,
			  modelObjectsMarshaller,
			  // Reference to other client apis
			  serviceInterfaceTypesToImplOrProxy,
			  // Delegate for CRUD
			  new ClientAPIDelegateForGoogleLoginCRUDServices<>(securityContextProvider,
							                                     modelObjectsMarshaller,
							                                    (CRUDServicesForGoogleLogin<L>) serviceInterfaceTypesToImplOrProxy.get(crudType)),
			  // Delegate for Find.
		      new ClientAPIDelegateForGoogleLoginFindServices<>(securityContextProvider,
									                            modelObjectsMarshaller,
									                            (FindServicesForGoogleLogin<L>) serviceInterfaceTypesToImplOrProxy.get(findType)));
	}
}
