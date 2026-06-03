package r01f.api.interfaces.security.login.google;

import r01f.api.interfaces.security.SecurityServiceInterface;
import r01f.api.interfaces.security.login.FindServicesForLogin;
import r01f.model.security.login.google.GoogleLogin;

public interface FindServicesForGoogleLogin<L extends GoogleLogin>
		 extends FindServicesForLogin<L>,
		 		 SecurityServiceInterface {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
}

