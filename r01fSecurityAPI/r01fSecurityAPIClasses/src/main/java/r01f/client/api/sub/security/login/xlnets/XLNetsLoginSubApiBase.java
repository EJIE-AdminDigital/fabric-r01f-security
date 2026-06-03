package r01f.client.api.sub.security.login.xlnets;

import java.util.Map;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.client.api.sub.security.login.SecurityProviderLoginSubApiBase;
import r01f.model.security.login.xlnets.XLNetsLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.interfaces.ServiceInterface;

/**
 * Client implementation of the User Login Config
 */
@Singleton
@Accessors(prefix="_")
public class XLNetsLoginSubApiBase<L extends XLNetsLogin,
							   	   C  extends ClientAPIDelegateForXLNetsLoginCRUDServices<L> ,
							   	   F  extends ClientAPIDelegateForXLNetsLoginFindServices<L> >
	 extends SecurityProviderLoginSubApiBase<L> {		// a sub API uses the proxy to access the services layer
/////////////////////////////////////////////////////////////////////////////////////////
//	DELEGATE APIS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final ClientAPIDelegateForXLNetsLoginCRUDServices<L> _forCrud;
	@Getter private final ClientAPIDelegateForXLNetsLoginFindServices<L> _forFind;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes" })
	public XLNetsLoginSubApiBase(final Provider<SecurityContext>  securityContextProvider,
							     final Marshaller modelObjectsMarshaller,
							     final Map<Class,ServiceInterface> serviceInterfaceTypesToImplOrProxy,
							     final C crudTypeDelegate,
							     final F findTypeDelegate)  {
		super(securityContextProvider,
			  modelObjectsMarshaller,
			  serviceInterfaceTypesToImplOrProxy);	// reference to other client apis
		_forCrud = crudTypeDelegate;
		_forFind = findTypeDelegate;
	}
}
