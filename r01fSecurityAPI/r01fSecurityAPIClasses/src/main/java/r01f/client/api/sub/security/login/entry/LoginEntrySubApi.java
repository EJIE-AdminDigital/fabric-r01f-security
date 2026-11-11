package r01f.client.api.sub.security.login.entry;

import java.util.Map;

import jakarta.inject.Provider;
import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.login.entry.CRUDServicesForLoginEntry;
import r01f.api.interfaces.security.login.entry.FindServicesForLoginEntry;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.services.api.ClientSubAPIBase;
import r01f.services.interfaces.ServiceInterface;

/**
 * Client implementation of the User Login Config
 */
@Accessors(prefix="_")
public class LoginEntrySubApi
	 extends ClientSubAPIBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	DELEGATE APIS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final ClientAPIDelegateForUserLoginEntryCRUDServices _forCrud;
	@Getter private final ClientAPIDelegateForUserLoginEntryFindServices _forFind;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes" })
	public LoginEntrySubApi(final Provider<SecurityContext>  securityContextProvider,
						    final Marshaller modelObjectsMarshaller,
						    final Map<Class,ServiceInterface> serviceInterfaceTypesToImplOrProxy,
						    // services
						    final Class<? extends CRUDServicesForLoginEntry> loginEntryCrudType,final Class<? extends FindServicesForLoginEntry> loginEntryFindType) {

		super(securityContextProvider,
			  modelObjectsMarshaller,
			  // Reference to other client apis
			  serviceInterfaceTypesToImplOrProxy);
	    // Delegate for CRUD
		_forCrud = new ClientAPIDelegateForUserLoginEntryCRUDServices(securityContextProvider,
						                                   			  modelObjectsMarshaller,
						                                   			  (CRUDServicesForLoginEntry)serviceInterfaceTypesToImplOrProxy.get(loginEntryCrudType));
		// Delegate for Find.
		_forFind = new ClientAPIDelegateForUserLoginEntryFindServices(securityContextProvider,
								                           			  modelObjectsMarshaller,
								                           			  (FindServicesForLoginEntry)serviceInterfaceTypesToImplOrProxy.get(loginEntryFindType));
	}
}