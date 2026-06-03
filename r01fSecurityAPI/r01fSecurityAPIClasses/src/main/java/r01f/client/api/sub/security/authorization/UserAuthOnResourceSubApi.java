package r01f.client.api.sub.security.authorization;

import java.util.Map;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import lombok.experimental.Accessors;
import r01f.api.interfaces.security.authorization.CRUDServicesForUserAuthorizationOnResource;
import r01f.api.interfaces.security.authorization.CountServicesForUserAuthorizationOnResource;
import r01f.api.interfaces.security.authorization.FindServicesForUserAuthorizationOnResource;
import r01f.model.security.authorization.UserAuthorizationOnResource;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.interfaces.ServiceInterface;

@Singleton
@Accessors(prefix="_")
public class UserAuthOnResourceSubApi<A extends UserAuthorizationOnResource>
	 extends UserAuthOnResourceSubApiBase<A,ClientAPIDelegateForUserAuthorizationOnResourceCRUDServices<A>,
	 										ClientAPIDelegateForUserAuthorizationOnResourceFindServices<A>,
	 										ClientAPIDelegateForUserAuthorizationOnResourceCountServices<A>> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public UserAuthOnResourceSubApi(final Provider<SecurityContext>  securityContextProvider,
							   		final Marshaller marshaller,
							   		final Map<Class,ServiceInterface> serviceInterfaceTypesToImplOrProxy,
							   		// services
							   		final Class<? extends CRUDServicesForUserAuthorizationOnResource<A>> crudForUserAuthOnResource,
							   		final Class<? extends FindServicesForUserAuthorizationOnResource<A>> findForUserAuthOnResource,
							   		final Class<? extends CountServicesForUserAuthorizationOnResource<A>> countForUserAuthOnResource) {
		super(securityContextProvider,
			  marshaller,
		      // Reference to other client apis
			  serviceInterfaceTypesToImplOrProxy,
			  // Delegate for CRUD
			  new ClientAPIDelegateForUserAuthorizationOnResourceCRUDServices<>(securityContextProvider,
					                                            				marshaller,
					                                            				(CRUDServicesForUserAuthorizationOnResource<A>) serviceInterfaceTypesToImplOrProxy.get(crudForUserAuthOnResource)),
			  // Delegate for Find.
			  new ClientAPIDelegateForUserAuthorizationOnResourceFindServices<>(securityContextProvider,
					  														    marshaller,
					  														    (FindServicesForUserAuthorizationOnResource<A>) serviceInterfaceTypesToImplOrProxy.get(findForUserAuthOnResource)),
			  // Delegate for count.
			  new ClientAPIDelegateForUserAuthorizationOnResourceCountServices<>(securityContextProvider,
					  														     marshaller,
					  														     (CountServicesForUserAuthorizationOnResource<A>) serviceInterfaceTypesToImplOrProxy.get(countForUserAuthOnResource)));
	}

}
