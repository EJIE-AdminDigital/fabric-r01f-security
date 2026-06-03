package r01f.client.api.sub.security.login.userpassword;

import java.util.Map;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.client.api.sub.security.login.SecurityProviderLoginSubApiBase;
import r01f.model.security.login.userpassword.UserPasswordLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.interfaces.ServiceInterface;

/**
 * Client implementation of the User Login Config
 */
@Singleton
@Accessors(prefix="_")
public class UserPasswordLoginSubApiBase<L extends UserPasswordLogin,
                                         CR extends ClientAPIDelegateForUserPasswordLoginCRUDServices<L>,
                                         CF extends ClientAPIDelegateForUserPasswordLoginFindServices<L>,
                                         CL extends ClientAPIDelegateForUserPasswordLoginServices<?,L>>
	 extends SecurityProviderLoginSubApiBase<L> {		// a sub API uses the proxy to access the services layer
/////////////////////////////////////////////////////////////////////////////////////////
//	DELEGATE APIS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final CR _forCrud;
	@Getter private final CF _forFind;
	@Getter private final CL _forLogin;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes" })
	public UserPasswordLoginSubApiBase(final Provider<SecurityContext>  securityContextProvider,
							   	       final Marshaller modelObjectsMarshaller,
							   	       final Map<Class,ServiceInterface> serviceInterfaceTypesToImplOrProxy,
							   	       final CR crudTypeDelegate,
							   	       final CF findTypeDelegate,
							   	       final CL loginTypeDelegate)  {
		super(securityContextProvider,
			  modelObjectsMarshaller,
			  serviceInterfaceTypesToImplOrProxy);	// reference to other client apis
		_forCrud = crudTypeDelegate;
		_forFind = findTypeDelegate;
		_forLogin = loginTypeDelegate;
	}
}
