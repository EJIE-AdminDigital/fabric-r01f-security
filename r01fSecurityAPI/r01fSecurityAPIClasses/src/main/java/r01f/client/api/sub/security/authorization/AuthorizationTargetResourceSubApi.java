package r01f.client.api.sub.security.authorization;

import java.util.Map;

import jakarta.inject.Provider;
import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.authorization.CRUDServicesForAuthorizationTargetResource;
import r01f.api.interfaces.security.authorization.CountServicesForAuthorizationTargetResource;
import r01f.api.interfaces.security.authorization.FindServicesForAuthorizationTargetResource;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.services.api.ClientSubAPIBase;
import r01f.services.interfaces.ServiceInterface;

@Accessors(prefix="_")
public class AuthorizationTargetResourceSubApi
	 extends ClientSubAPIBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final ClientAPIDelegateForAuthorizationTargetResourceCRUDServices _forCrud;
	@Getter private final ClientAPIDelegateForAuthorizationTargetResourceFindServices _forFind;
	@Getter private final ClientAPIDelegateForAuthorizationTargetResourceCountServices _forCount;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes" })
	public AuthorizationTargetResourceSubApi(final Provider<SecurityContext>  securityContextProvider,
							   		  		 final Marshaller marshaller,
							   		  		 final Map<Class,ServiceInterface> serviceInterfaceTypesToImplOrProxy,
							   		  		 // services
							   		  		 final Class<? extends CRUDServicesForAuthorizationTargetResource> crudServicesType,final Class<? extends FindServicesForAuthorizationTargetResource> findServicesType,final Class<? extends CountServicesForAuthorizationTargetResource> countServicesType) {

		super(securityContextProvider,
		      marshaller,
			  // Reference to other client apis
		      serviceInterfaceTypesToImplOrProxy);
		// Delegate for CRUD
		_forCrud = new ClientAPIDelegateForAuthorizationTargetResourceCRUDServices(securityContextProvider,
				                                            		    		   marshaller,
				                                            		    		   (CRUDServicesForAuthorizationTargetResource)serviceInterfaceTypesToImplOrProxy.get(crudServicesType));
		// Delegate for Find.
		_forFind = new ClientAPIDelegateForAuthorizationTargetResourceFindServices(securityContextProvider,
				  													    		   marshaller,
				  													    		   (FindServicesForAuthorizationTargetResource)serviceInterfaceTypesToImplOrProxy.get(findServicesType));
		// Delegate for count.
		_forCount = new ClientAPIDelegateForAuthorizationTargetResourceCountServices(securityContextProvider,
				  													     			 marshaller,
				  													     			 (CountServicesForAuthorizationTargetResource)serviceInterfaceTypesToImplOrProxy.get(countServicesType));
	}
}
