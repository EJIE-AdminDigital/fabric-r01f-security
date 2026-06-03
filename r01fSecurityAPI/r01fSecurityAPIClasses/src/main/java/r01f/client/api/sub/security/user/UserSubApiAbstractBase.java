package r01f.client.api.sub.security.user;

import java.util.Map;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import lombok.experimental.Accessors;
import r01f.model.security.user.User;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.api.ClientSubAPIBase;
import r01f.services.interfaces.ServiceInterface;

/**
 * Client implementation of User. Just CRUD /Finder.
 */
@Singleton
@Accessors(prefix="_")
public abstract class UserSubApiAbstractBase<U extends User,
						                     C extends ClientAPIDelegateForUserCRUDServices<U>,
						                     F extends ClientAPIDelegateForUserFindServices<U>>

	 		 extends ClientSubAPIBase {		// a sub API uses the proxy to access the services layer
/////////////////////////////////////////////////////////////////////////////////////////
//	DELEGATE APIS
/////////////////////////////////////////////////////////////////////////////////////////
	private final C _forCrud;
	private final F _forFind;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes" })
	public UserSubApiAbstractBase(final Provider<SecurityContext>  securityContextProvider,
							      final Marshaller modelObjectsMarshaller,
							      final Map<Class,ServiceInterface> usersServiceInterfaceTypesToImplOrProxy,
							      final C crudDelegate,
							      final F findDelegate) {

		super(securityContextProvider,
			  modelObjectsMarshaller,
			  usersServiceInterfaceTypesToImplOrProxy);	// reference to other client apis
		_forCrud = crudDelegate;
		_forFind = findDelegate;

	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public C getForCrud() {
		return _forCrud;
	}
	public F getForFind() {
		return _forFind;
	}
}
