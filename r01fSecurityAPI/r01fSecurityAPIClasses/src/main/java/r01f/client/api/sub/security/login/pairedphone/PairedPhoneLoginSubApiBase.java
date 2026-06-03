package r01f.client.api.sub.security.login.pairedphone;

import java.util.Map;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.client.api.sub.security.login.SecurityProviderLoginSubApiBase;
import r01f.model.security.login.pairedphone.PairedPhoneLogin;
import r01f.model.security.login.pairedphone.PairedPhoneLoginConsent;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.interfaces.ServiceInterface;

/**
 * Client implementation of the User Login Config
 */
@Singleton
@Accessors(prefix="_")
public class PairedPhoneLoginSubApiBase<L extends PairedPhoneLogin, P extends PairedPhoneLoginConsent,
                                        C extends ClientAPIDelegateForUserPairedPhoneLoginCRUDServices<L>, F extends ClientAPIDelegateForUserPairedPhoneLoginFindServices<L>,
                                        S extends ClientAPIDelegateForUserPairedPhoneLoginConsentServices<P>>
	 extends SecurityProviderLoginSubApiBase<L> {		// a sub API uses the proxy to access the services layer
/////////////////////////////////////////////////////////////////////////////////////////
//	DELEGATE APIS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final C _forCrud;
	@Getter private final F _forFind;
	@Getter private final S _forConsentServices;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes" })
	public PairedPhoneLoginSubApiBase(final Provider<SecurityContext>  securityContextProvider,
							   	      final Marshaller modelObjectsMarshaller,
							   	      final Map<Class,ServiceInterface> serviceInterfaceTypesToImplOrProxy,
							   	      final C forCrud,final F forFind,
							   	      final S forConsentServices)  {
		super(securityContextProvider,
			  modelObjectsMarshaller,
			  serviceInterfaceTypesToImplOrProxy);	// reference to other client apis

		_forCrud = forCrud;
		_forFind = forFind;
		_forConsentServices = forConsentServices;

	}
}
