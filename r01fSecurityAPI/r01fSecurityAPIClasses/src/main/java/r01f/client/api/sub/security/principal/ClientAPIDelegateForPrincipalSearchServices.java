package r01f.client.api.sub.security.principal;

import jakarta.inject.Provider;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.principal.SearchFilterForSecurityPrincipalBase;
import r01f.model.security.principal.SearchResultItemForSecurityPrincipalBase;
import r01f.objectstreamer.Marshaller;
import r01f.services.api.delegates.ClientAPIDelegateForModelObjectSearchServices;
import r01f.services.interfaces.SearchServices;

public class ClientAPIDelegateForPrincipalSearchServices<P extends IsSecurityPrincipal,
														 F extends SearchFilterForSecurityPrincipalBase<P,?>,I extends SearchResultItemForSecurityPrincipalBase<P>>
	 extends ClientAPIDelegateForModelObjectSearchServices<F,I> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForPrincipalSearchServices(final Provider<SecurityContext> securityContextProvider,
												  final Marshaller modelObjectsMarshaller,
												  final SearchServices<F,I> services) {
		super(securityContextProvider,
			  modelObjectsMarshaller,
			  services);
	}
}
