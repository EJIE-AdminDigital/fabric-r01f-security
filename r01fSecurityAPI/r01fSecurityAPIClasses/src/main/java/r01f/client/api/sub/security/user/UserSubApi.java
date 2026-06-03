package r01f.client.api.sub.security.user;

import java.util.Map;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import lombok.experimental.Accessors;
import r01f.api.interfaces.security.user.CRUDServicesForUser;
import r01f.api.interfaces.security.user.FindServicesForUser;
import r01f.api.interfaces.security.user.SearchServicesForUser;
import r01f.model.security.user.SearchFilterForUserBase;
import r01f.model.security.user.SearchResultItemForUserBase;
import r01f.model.security.user.User;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.interfaces.ServiceInterface;

/**
 * Client implementation of the Users
 */
@Singleton
@Accessors(prefix="_")
public class UserSubApi<U extends User>
	 extends UserSubApiBase<U,
	 						ClientAPIDelegateForUserCRUDServices<U>,ClientAPIDelegateForUserFindServices<U>,ClientAPIDelegateForUserSearchServices<U,? extends SearchFilterForUserBase<U,?>,? extends SearchResultItemForUserBase<U>>> {
	// a sub API uses the proxy to access the services layer
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes" })
	public UserSubApi(final Provider<SecurityContext>  securityContextProvider,
					  final Marshaller modelObjectsMarshaller,
					  final Map<Class,ServiceInterface> usersServiceInterfaceTypesToImplOrProxy,
					  final Class<? extends CRUDServicesForUser<U>> crudType,
					  final Class<? extends FindServicesForUser<U>> findType) {

		this(securityContextProvider,
			 modelObjectsMarshaller,
			 // Reference to other client apis
			 usersServiceInterfaceTypesToImplOrProxy,
			 crudType,findType,null);	// no search
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public UserSubApi(final Provider<SecurityContext>  securityContextProvider,
					  final Marshaller modelObjectsMarshaller,
					  final Map<Class,ServiceInterface> usersServiceInterfaceTypesToImplOrProxy,
					  final Class<? extends CRUDServicesForUser<U>> crudType,
					  final Class<? extends FindServicesForUser<U>> findType,
					  final Class<? extends SearchServicesForUser<U,?,?>> searchType) {

		super(securityContextProvider,
			  modelObjectsMarshaller,
			  // Reference to other client apis
			  usersServiceInterfaceTypesToImplOrProxy,
			  // Delegate for CRUD
			  new ClientAPIDelegateForUserCRUDServices<>(securityContextProvider,
									                     modelObjectsMarshaller,
									                     (CRUDServicesForUser<U>)usersServiceInterfaceTypesToImplOrProxy.get(crudType)),
			  // Delegate for Find.
			  new ClientAPIDelegateForUserFindServices<>(securityContextProvider,
											             modelObjectsMarshaller,
											             (FindServicesForUser<U>)usersServiceInterfaceTypesToImplOrProxy.get(findType)),
			  // Delegate for Search
			  new ClientAPIDelegateForUserSearchServices<>(securityContextProvider,
											               modelObjectsMarshaller,
											               (SearchServicesForUser<U,?,?>)usersServiceInterfaceTypesToImplOrProxy.get(searchType)));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  API DELEGATE FOR
/////////////////////////////////////////////////////////////////////////////////////////
}
