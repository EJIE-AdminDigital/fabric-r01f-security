package r01f.client.api.sub.security.login.pairedphone;

import jakarta.inject.Provider;

import r01f.api.interfaces.security.login.pairedphone.FindServicesForPairedPhoneLogin;
import r01f.client.api.sub.security.login.ClientAPIDelegateForLoginFindServicesBase;
import r01f.model.security.login.pairedphone.PairedPhoneLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;

public class ClientAPIDelegateForUserPairedPhoneLoginFindServices<L extends PairedPhoneLogin>
	 extends ClientAPIDelegateForLoginFindServicesBase<L> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForUserPairedPhoneLoginFindServices(final Provider<SecurityContext>  securityContextProvider,
														 	    final Marshaller marshaller,
														 	    final FindServicesForPairedPhoneLogin<L> crudServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  crudServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////

}
