package r01f.client.api.sub.security.user;

import jakarta.inject.Provider;

import r01f.model.security.user.SearchFilterForUserBase;
import r01f.model.security.user.SearchResultItemForUserBase;
import r01f.model.security.user.User;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.api.delegates.ClientAPIDelegateForModelObjectSearchServices;
import r01f.services.interfaces.SearchServices;

public class ClientAPIDelegateForUserSearchServices<U extends User,
													F extends SearchFilterForUserBase<U,?>,I extends SearchResultItemForUserBase<U>>
	 extends ClientAPIDelegateForModelObjectSearchServices<F,I> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForUserSearchServices(final Provider<SecurityContext> securityContextProvider,
												  final Marshaller modelObjectsMarshaller,
												  final SearchServices<F,I> services) {
		super(securityContextProvider,
			  modelObjectsMarshaller,
			  services);
	}
}
