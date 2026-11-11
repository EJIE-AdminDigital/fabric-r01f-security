package r01f.client.api.sub.security.authorization;

import java.util.Map;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;
import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.authorization.CRUDServicesForAuthorizationOnResource;
import r01f.api.interfaces.security.authorization.CountServicesForAuthorizationOnResource;
import r01f.api.interfaces.security.authorization.FindServicesForAuthorizationOnResource;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.services.api.ClientSubAPIBase;
import r01f.services.interfaces.ServiceInterface;

@Singleton
@Accessors(prefix="_")
public class AuthOnResourceSubApi
	 extends ClientSubAPIBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final ClientAPIDelegateForAuthorizationOnResourceCRUDServices _forCrud;
	@Getter private final ClientAPIDelegateForAuthorizationOnResourceFindServices _forFind;
	@Getter private final ClientAPIDelegateForAuthorizationOnResourceCountServices _forCount;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes" })
	public AuthOnResourceSubApi(final Provider<SecurityContext>  securityContextProvider,
						   		final Marshaller marshaller,
						   		final Map<Class,ServiceInterface> serviceInterfaceTypesToImplOrProxy,
						   		// services
						   		final Class<? extends CRUDServicesForAuthorizationOnResource> crudServicesType,final Class<? extends FindServicesForAuthorizationOnResource> findServicesType,final Class<? extends CountServicesForAuthorizationOnResource> countServicesType) {
		super(securityContextProvider,
			  marshaller,
		      // Reference to other client apis
			  serviceInterfaceTypesToImplOrProxy);
		// Delegate for CRUD
		_forCrud = new ClientAPIDelegateForAuthorizationOnResourceCRUDServices(securityContextProvider,
			                                            					   marshaller,
			                                            					   (CRUDServicesForAuthorizationOnResource) serviceInterfaceTypesToImplOrProxy.get(crudServicesType));
		// Delegate for Find.
		_forFind = new ClientAPIDelegateForAuthorizationOnResourceFindServices(securityContextProvider,
			  													    		   marshaller,
			  													    		   (FindServicesForAuthorizationOnResource) serviceInterfaceTypesToImplOrProxy.get(findServicesType));
		// Delegate for count.
		_forCount = new ClientAPIDelegateForAuthorizationOnResourceCountServices(securityContextProvider,
			  													     			 marshaller,
			  													     			 (CountServicesForAuthorizationOnResource) serviceInterfaceTypesToImplOrProxy.get(countServicesType));
	}
}
