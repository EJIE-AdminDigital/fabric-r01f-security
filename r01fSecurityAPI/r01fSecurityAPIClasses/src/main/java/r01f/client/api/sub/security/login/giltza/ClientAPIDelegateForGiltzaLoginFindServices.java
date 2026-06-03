package r01f.client.api.sub.security.login.giltza;

import jakarta.inject.Provider;

import r01f.api.interfaces.security.login.giltza.FindServicesForGiltzaLogin;
import r01f.client.api.sub.security.login.ClientAPIDelegateForLoginFindServicesBase;
import r01f.model.security.login.giltza.GiltzaLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;

public class ClientAPIDelegateForGiltzaLoginFindServices<L extends GiltzaLogin>
	 extends ClientAPIDelegateForLoginFindServicesBase<L> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForGiltzaLoginFindServices(final Provider<SecurityContext>  securityContextProvider,
													   final Marshaller marshaller,
													   final FindServicesForGiltzaLogin<L> findServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  findServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////

}
