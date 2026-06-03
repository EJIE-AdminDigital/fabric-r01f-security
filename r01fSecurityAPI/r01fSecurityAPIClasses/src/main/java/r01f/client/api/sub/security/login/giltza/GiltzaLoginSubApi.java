package r01f.client.api.sub.security.login.giltza;

import java.util.Map;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import lombok.experimental.Accessors;
import r01f.api.interfaces.security.login.giltza.CRUDServicesForGiltzaLogin;
import r01f.api.interfaces.security.login.giltza.FindServicesForGiltzaLogin;
import r01f.model.security.login.giltza.GiltzaLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.interfaces.ServiceInterface;

/**
 * Client implementation of the User Login Config
 */
@Singleton
@Accessors(prefix="_")
public class GiltzaLoginSubApi<L extends GiltzaLogin>
	 extends GiltzaLoginSubApiBase<L,ClientAPIDelegateForGiltzaLoginCRUDServices<L>, ClientAPIDelegateForGiltzaLoginFindServices<L>> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public GiltzaLoginSubApi(final Provider<SecurityContext>  securityContextProvider,
							 final Marshaller modelObjectsMarshaller,
							 final Map<Class,ServiceInterface> serviceInterfaceTypesToImplOrProxy,
							 final Class<? extends CRUDServicesForGiltzaLogin<L>> crudType,
							 final Class<? extends FindServicesForGiltzaLogin<L>> findType)  {
		super(securityContextProvider,
			  modelObjectsMarshaller,
			  // Reference to other client apis
			  serviceInterfaceTypesToImplOrProxy,
			  // Delegate for CRUD
			  new ClientAPIDelegateForGiltzaLoginCRUDServices<>(securityContextProvider,
							                                     modelObjectsMarshaller,
							                                    (CRUDServicesForGiltzaLogin<L>) serviceInterfaceTypesToImplOrProxy.get(crudType)),
			  // Delegate for Find.
		      new ClientAPIDelegateForGiltzaLoginFindServices<>(securityContextProvider,
									                            modelObjectsMarshaller,
									                            (FindServicesForGiltzaLogin<L>) serviceInterfaceTypesToImplOrProxy.get(findType)));
	}
}
