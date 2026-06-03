package r01f.client.api.sub.security.login.apikey;

import java.util.Map;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import lombok.experimental.Accessors;
import r01f.api.interfaces.security.login.apikey.CRUDServicesForApiKeyLogin;
import r01f.api.interfaces.security.login.apikey.FindServicesForApiKeyLogin;
import r01f.model.security.login.apikey.ApiKeyLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.interfaces.ServiceInterface;

/**
 * Client implementation of the User Login Config
 */
@Singleton
@Accessors(prefix="_")
public class ApiKeyLoginSubApi<L extends ApiKeyLogin>
	 extends ApiKeyLoginSubApiBase<L,ClientAPIDelegateForApiKeyLoginCRUDServices<L>, ClientAPIDelegateForApiKeyLoginFindServices<L>> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ApiKeyLoginSubApi(final Provider<SecurityContext>  securityContextProvider,
							 final Marshaller modelObjectsMarshaller,
							 final Map<Class,ServiceInterface> serviceInterfaceTypesToImplOrProxy,
							 final Class<? extends CRUDServicesForApiKeyLogin<L>> crudType,
							 final Class<? extends FindServicesForApiKeyLogin<L>> findType)  {
		super(securityContextProvider,
			  modelObjectsMarshaller,
			  // Reference to other client apis
			  serviceInterfaceTypesToImplOrProxy,
			  // Delegate for CRUD
			  new ClientAPIDelegateForApiKeyLoginCRUDServices<>(securityContextProvider,
							                                    modelObjectsMarshaller,
							                                    (CRUDServicesForApiKeyLogin<L>) serviceInterfaceTypesToImplOrProxy.get(crudType)),
			  // Delegate for Find.
		      new ClientAPIDelegateForApiKeyLoginFindServices<>(securityContextProvider,
									                            modelObjectsMarshaller,
									                            (FindServicesForApiKeyLogin<L>) serviceInterfaceTypesToImplOrProxy.get(findType)));
	}
}
