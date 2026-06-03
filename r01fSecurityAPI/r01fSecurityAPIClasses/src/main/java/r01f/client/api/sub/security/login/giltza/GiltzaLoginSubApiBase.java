package r01f.client.api.sub.security.login.giltza;

import java.util.Map;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.client.api.sub.security.login.SecurityProviderLoginSubApiBase;
import r01f.model.security.login.giltza.GiltzaLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.interfaces.ServiceInterface;

/**
 * Client implementation GiltzaLoginSubApiBase
 */
@Singleton
@Accessors(prefix="_")
public class GiltzaLoginSubApiBase<L extends GiltzaLogin,
                                   C extends ClientAPIDelegateForGiltzaLoginCRUDServices<L> ,
                                   F extends ClientAPIDelegateForGiltzaLoginFindServices<L> >
	 extends SecurityProviderLoginSubApiBase<L> {		// a sub API uses the proxy to access the services layer
/////////////////////////////////////////////////////////////////////////////////////////
//	DELEGATE APIS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final C _forCrud;
	@Getter private final F _forFind;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes" })
	public GiltzaLoginSubApiBase(final Provider<SecurityContext>  securityContextProvider,
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
