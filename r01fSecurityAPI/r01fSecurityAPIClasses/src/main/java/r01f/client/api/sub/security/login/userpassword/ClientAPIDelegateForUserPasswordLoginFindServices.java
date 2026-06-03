package r01f.client.api.sub.security.login.userpassword;

import jakarta.inject.Provider;

import r01f.api.interfaces.security.login.userpassword.FindServicesForUserPasswordLogin;
import r01f.client.api.sub.security.login.ClientAPIDelegateForLoginFindServicesBase;
import r01f.model.security.login.userpassword.UserPasswordLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;

public class ClientAPIDelegateForUserPasswordLoginFindServices<L extends UserPasswordLogin>
	 extends ClientAPIDelegateForLoginFindServicesBase<L> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForUserPasswordLoginFindServices(final Provider<SecurityContext>  securityContextProvider,
														 	 final Marshaller marshaller,
														 	 final FindServicesForUserPasswordLogin<L> findServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  findServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////

}
