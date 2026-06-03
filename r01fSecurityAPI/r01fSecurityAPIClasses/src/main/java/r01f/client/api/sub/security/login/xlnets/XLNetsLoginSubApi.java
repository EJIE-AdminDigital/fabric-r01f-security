package r01f.client.api.sub.security.login.xlnets;

import java.util.Map;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import lombok.experimental.Accessors;
import r01f.api.interfaces.security.login.xlnets.CRUDServicesForXLNetsLogin;
import r01f.api.interfaces.security.login.xlnets.FindServicesForXLNetsLogin;
import r01f.model.security.login.xlnets.XLNetsLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.interfaces.ServiceInterface;

/**
 * Client implementation of the User Login Config
 */
@Singleton
@Accessors(prefix="_")
public class XLNetsLoginSubApi<L extends XLNetsLogin>
	 extends XLNetsLoginSubApiBase<L,ClientAPIDelegateForXLNetsLoginCRUDServices<L>,ClientAPIDelegateForXLNetsLoginFindServices<L>> {
	// a sub API uses the proxy to access the services layer
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public XLNetsLoginSubApi(final Provider<SecurityContext>  securityContextProvider,
							 final Marshaller modelObjectsMarshaller,
							 final Map<Class,ServiceInterface> serviceInterfaceTypesToImplOrProxy,
							 final Class<? extends CRUDServicesForXLNetsLogin<L>> crudType,
							 final Class<? extends FindServicesForXLNetsLogin<L,?>> findType) {

		super(securityContextProvider,
			  modelObjectsMarshaller,
			  // Reference to other client apis
			  serviceInterfaceTypesToImplOrProxy,
			    // Delegate for CRUD
			  new ClientAPIDelegateForXLNetsLoginCRUDServices<>(securityContextProvider,
								                                modelObjectsMarshaller,
								                                (CRUDServicesForXLNetsLogin<L>) serviceInterfaceTypesToImplOrProxy.get(crudType)),
			   // Delegate for Find.
			  new ClientAPIDelegateForXLNetsLoginFindServices<>(securityContextProvider,
										                        modelObjectsMarshaller,
										                        (FindServicesForXLNetsLogin<L,?>) serviceInterfaceTypesToImplOrProxy.get(findType)));
	}
}
