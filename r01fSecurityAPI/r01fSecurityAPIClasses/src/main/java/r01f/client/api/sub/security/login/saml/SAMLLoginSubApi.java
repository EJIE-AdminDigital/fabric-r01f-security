package r01f.client.api.sub.security.login.saml;

import java.util.Map;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import lombok.experimental.Accessors;
import r01f.api.interfaces.security.login.saml.CRUDServicesForSAMLLogin;
import r01f.api.interfaces.security.login.saml.FindServicesForSAMLLogin;
import r01f.model.security.login.saml.SAMLLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.interfaces.ServiceInterface;

/**
 * Client implementation of the User Login Config
 */
@Singleton
@Accessors(prefix="_")
public class SAMLLoginSubApi<L extends SAMLLogin>
	 extends SAMLLoginSubApiBase<L,ClientAPIDelegateForSAMLLoginCRUDServices<L>,ClientAPIDelegateForSAMLLoginFindServices<L>> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes","unchecked" })
	public SAMLLoginSubApi(final Provider<SecurityContext>  securityContextProvider,
						   final Marshaller modelObjectsMarshaller,
						   final Map<Class,ServiceInterface> serviceInterfaceTypesToImplOrProxy,
						   final Class<? extends CRUDServicesForSAMLLogin<L>> crudType,
						   final Class<? extends FindServicesForSAMLLogin<L>> findType)  {
		super(securityContextProvider,
			  modelObjectsMarshaller,
			  // Reference to other client apis
			  serviceInterfaceTypesToImplOrProxy,
			  // Delegate for CRUD
			  new ClientAPIDelegateForSAMLLoginCRUDServices<>(securityContextProvider,
							                                  modelObjectsMarshaller,
							                                  (CRUDServicesForSAMLLogin<L>) serviceInterfaceTypesToImplOrProxy.get(crudType)),
			  // Delegate for Find.
		      new ClientAPIDelegateForSAMLLoginFindServices<>(securityContextProvider,
									                          modelObjectsMarshaller,
									                          (FindServicesForSAMLLogin<L>) serviceInterfaceTypesToImplOrProxy.get(findType)));
	}
}
