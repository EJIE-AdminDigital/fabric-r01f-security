package r01f.client.api.sub.security.login.entry;

import java.util.Map;

import jakarta.inject.Provider;

import lombok.experimental.Accessors;
import r01f.api.interfaces.security.login.entry.CRUDServicesForUserLoginEntry;
import r01f.api.interfaces.security.login.entry.FindServicesForUserLoginEntry;
import r01f.model.security.login.entry.UserLoginEntry;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.interfaces.ServiceInterface;

/**
 * Client implementation of the User Login Config
 */
@Accessors(prefix="_")
public class UserLoginEntrySubApi<E extends UserLoginEntry>
	 extends UserLoginEntrySubApiBase<E,ClientAPIDelegateForUserLoginEntryCRUDServices<E>,ClientAPIDelegateForUserLoginEntryFindServices<E>> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public UserLoginEntrySubApi(final Provider<SecurityContext>  securityContextProvider,
							    final Marshaller modelObjectsMarshaller,
							    final Map<Class,ServiceInterface> serviceInterfaceTypesToImplOrProxy,
							    final Class<? extends CRUDServicesForUserLoginEntry<E>> crudType,
							    final Class<? extends FindServicesForUserLoginEntry<E>> findType) {

		super(securityContextProvider,
			  modelObjectsMarshaller,
			  // Reference to other client apis
			  serviceInterfaceTypesToImplOrProxy,
			    // Delegate for CRUD
			  new ClientAPIDelegateForUserLoginEntryCRUDServices<>(securityContextProvider,
								                                   modelObjectsMarshaller,
								                                   (CRUDServicesForUserLoginEntry<E>) serviceInterfaceTypesToImplOrProxy.get(crudType)),
			   // Delegate for Find.
			  new ClientAPIDelegateForUserLoginEntryFindServices<>(securityContextProvider,
										                           modelObjectsMarshaller,
										                           (FindServicesForUserLoginEntry<E>) serviceInterfaceTypesToImplOrProxy.get(findType)));
		}

}
