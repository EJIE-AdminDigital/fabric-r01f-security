package r01f.client.api.sub.security.login.google;

import jakarta.inject.Provider;

import r01f.api.interfaces.security.login.google.FindServicesForGoogleLogin;
import r01f.client.api.sub.security.login.ClientAPIDelegateForLoginFindServicesBase;
import r01f.model.security.login.google.GoogleLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;

public class ClientAPIDelegateForGoogleLoginFindServices<L extends GoogleLogin>
	 extends ClientAPIDelegateForLoginFindServicesBase<L> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForGoogleLoginFindServices(final Provider<SecurityContext>  securityContextProvider,
													   final Marshaller marshaller,
													   final FindServicesForGoogleLogin<L> findServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  findServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////

}
