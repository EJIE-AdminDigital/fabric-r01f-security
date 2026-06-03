package r01f.client.api.sub.security.login.apikey;

import java.util.Map;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.model.security.login.apikey.ApiKeyLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.api.ClientSubAPIBase;
import r01f.services.interfaces.ServiceInterface;

/**
 * Client implementation ApiKeyLoginSubApiBase
 */
@Singleton
@Accessors(prefix="_")
public class ApiKeyLoginSubApiBase<L extends ApiKeyLogin,
                                   C extends ClientAPIDelegateForApiKeyLoginCRUDServices<L> ,
                                   F extends ClientAPIDelegateForApiKeyLoginFindServices<L> >
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
	public ApiKeyLoginSubApiBase(final Provider<SecurityContext>  securityContextProvider,
							     final Marshaller modelObjectsMarshaller,
							     final Map<Class,ServiceInterface> serviceInterfaceTypesToImplOrProxy,
							     final C crudDelegate,
							     final F findDelgate)  {
		super(securityContextProvider,
			  modelObjectsMarshaller,
			  serviceInterfaceTypesToImplOrProxy);	// reference to other client apis
		_forCrud = crudDelegate;
		_forFind = findDelgate;
	}
}
