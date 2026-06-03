package r01f.client.api.sub.security.authorization;

import java.util.Map;

import jakarta.inject.Provider;

import lombok.experimental.Accessors;
import r01f.api.interfaces.security.authorization.CRUDServicesForAuthorizationTargetResource;
import r01f.api.interfaces.security.authorization.CountServicesForAuthorizationTargetResource;
import r01f.api.interfaces.security.authorization.FindServicesForAuthorizationTargetResource;
import r01f.model.security.authorization.AuthorizationTargetResource;
import r01f.model.security.summaries.SummarizedAuthorizationResource;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.interfaces.ServiceInterface;

@Accessors(prefix="_")
public class AuthorizationTargetResourceSubApi<R extends AuthorizationTargetResource,
											   S extends SummarizedAuthorizationResource<R>>
	 extends AuthorizationTargetResourceSubApiBase<R,S,
	 											   ClientAPIDelegateForAuthorizationTargetResourceCRUDServices<R>,
	 											   ClientAPIDelegateForAuthorizationTargetResourceFindServices<R,S>,
	 											   ClientAPIDelegateForAuthorizationTargetResourceCountServices<R>> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public AuthorizationTargetResourceSubApi(final Provider<SecurityContext>  securityContextProvider,
							   		  		 final Marshaller marshaller,
							   		  		 final Map<Class,ServiceInterface> serviceInterfaceTypesToImplOrProxy,
							   		  		 //	services
							   		  		 final Class<? extends CRUDServicesForAuthorizationTargetResource<R>> crudForResource,
							   		  		 final Class<? extends FindServicesForAuthorizationTargetResource<R>> findForResource,
							   		  		 final Class<? extends CountServicesForAuthorizationTargetResource<R>> countForResource) {

		super(securityContextProvider,
		      marshaller,
			  // Reference to other client apis
		      serviceInterfaceTypesToImplOrProxy,
			  // Delegate for CRUD
		      new ClientAPIDelegateForAuthorizationTargetResourceCRUDServices<>(securityContextProvider,
						                                            		    marshaller,
						                                            		    (CRUDServicesForAuthorizationTargetResource<R>)serviceInterfaceTypesToImplOrProxy.get(crudForResource)),
		      // Delegate for Find.
			  new ClientAPIDelegateForAuthorizationTargetResourceFindServices<>(securityContextProvider,
						  													    marshaller,
						  													    (FindServicesForAuthorizationTargetResource<R>)serviceInterfaceTypesToImplOrProxy.get(findForResource)),
		      // Delegate for count.
			  new ClientAPIDelegateForAuthorizationTargetResourceCountServices<>(securityContextProvider,
						  													     marshaller,
						  													     (CountServicesForAuthorizationTargetResource<R>)serviceInterfaceTypesToImplOrProxy.get(countForResource)));

	}
}
