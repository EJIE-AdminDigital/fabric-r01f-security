package r01f.client.api.sub.security.login.pairedphone;

import java.util.Map;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import lombok.experimental.Accessors;
import r01f.api.interfaces.security.login.pairedphone.CRUDServicesForPairedPhoneLogin;
import r01f.api.interfaces.security.login.pairedphone.FindServicesForPairedPhoneLogin;
import r01f.api.interfaces.security.login.pairedphone.PairedPhoneLoginConsentServices;
import r01f.model.security.login.pairedphone.PairedPhoneLogin;
import r01f.model.security.login.pairedphone.PairedPhoneLoginConsent;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.interfaces.ServiceInterface;

/**
 * Client implementation of the Paired Phone Login Sub API.
 */
@Singleton
@Accessors(prefix="_")
public class PairedPhoneLoginSubApi<L extends PairedPhoneLogin,C extends PairedPhoneLoginConsent>
	 extends PairedPhoneLoginSubApiBase<L,C,ClientAPIDelegateForUserPairedPhoneLoginCRUDServices<L>, ClientAPIDelegateForUserPairedPhoneLoginFindServices<L>,ClientAPIDelegateForUserPairedPhoneLoginConsentServices<C>> {
	// a sub API uses the proxy to access the services layer
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PairedPhoneLoginSubApi(final Provider<SecurityContext>  securityContextProvider,
							   	  final Marshaller modelObjectsMarshaller,
							   	  final Map<Class,ServiceInterface> serviceInterfaceTypesToImplOrProxy,
							   	  final Class<? extends CRUDServicesForPairedPhoneLogin<L>> crudServicesType,
							      final Class<? extends FindServicesForPairedPhoneLogin<L>> findServicesType,
							   	  final Class<? extends PairedPhoneLoginConsentServices<C>> consentServicesype)  {
		super(securityContextProvider,
		      modelObjectsMarshaller,
		     // Reference to other client apis
		      serviceInterfaceTypesToImplOrProxy,
		     // Delegate for CRUD
		     new ClientAPIDelegateForUserPairedPhoneLoginCRUDServices<L>(securityContextProvider,
								                         			    modelObjectsMarshaller,
								                                        (CRUDServicesForPairedPhoneLogin<L>) serviceInterfaceTypesToImplOrProxy.get(crudServicesType)),
		     new ClientAPIDelegateForUserPairedPhoneLoginFindServices<L>(securityContextProvider,
      			                                                         modelObjectsMarshaller,
                                                                         (FindServicesForPairedPhoneLogin<L>) serviceInterfaceTypesToImplOrProxy.get(findServicesType)),
		      // Delegate for Find.
			 new ClientAPIDelegateForUserPairedPhoneLoginConsentServices<C>(securityContextProvider,
										                     		       modelObjectsMarshaller,
										                                 (PairedPhoneLoginConsentServices<C>) serviceInterfaceTypesToImplOrProxy.get(consentServicesype)));

		;
	}
}
