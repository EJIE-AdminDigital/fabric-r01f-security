package r01f.client.api.sub.security.login.entry;

import java.util.Map;

import jakarta.inject.Provider;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.model.security.login.entry.UserLoginEntry;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.api.ClientSubAPIBase;
import r01f.services.interfaces.ServiceInterface;

/**
 * Client implementation of the User Login Config
 */
@Accessors(prefix="_")
public class UserLoginEntrySubApiBase<E extends UserLoginEntry,
                                      C extends ClientAPIDelegateForUserLoginEntryCRUDServices<E>,
                                      F extends ClientAPIDelegateForUserLoginEntryFindServices<E>>
	 extends ClientSubAPIBase {		// a sub API uses the proxy to access the services layer
/////////////////////////////////////////////////////////////////////////////////////////
//	DELEGATE APIS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final C _forCrud;
	@Getter private final F _forFind;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes" })
	public UserLoginEntrySubApiBase(final Provider<SecurityContext>  securityContextProvider,
						    	    final Marshaller modelObjectsMarshaller,
						    	    final Map<Class,ServiceInterface> _usersServiceInterfaceTypesToImplOrProxy,
						    	    final C crudTypeDelegate,
						    	    final F findTypeDelegate)  {

		super(securityContextProvider,
			   modelObjectsMarshaller,
			   _usersServiceInterfaceTypesToImplOrProxy);	// reference to other client apis
		_forCrud = crudTypeDelegate;
		_forFind = findTypeDelegate;
	}

}
