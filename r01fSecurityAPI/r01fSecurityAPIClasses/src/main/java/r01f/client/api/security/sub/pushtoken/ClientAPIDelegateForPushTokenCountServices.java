package r01f.client.api.security.sub.pushtoken;

import jakarta.inject.Provider;
import r01f.api.interfaces.security.pushtoken.CountServicesForPushToken;
import r01f.model.security.pushtoken.PushToken;
import r01f.model.security.pushtoken.PushTokenOID;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.api.delegates.ClientAPIDelegateForModelObjectCountServices;

public class ClientAPIDelegateForPushTokenCountServices<T extends PushToken>
	 extends ClientAPIDelegateForModelObjectCountServices<PushTokenOID,T> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForPushTokenCountServices(final Provider<SecurityContext>  securityContextProvider,
													   final Marshaller marshaller,
													   final CountServicesForPushToken<T> countServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  countServicesProxy);
	}

}
