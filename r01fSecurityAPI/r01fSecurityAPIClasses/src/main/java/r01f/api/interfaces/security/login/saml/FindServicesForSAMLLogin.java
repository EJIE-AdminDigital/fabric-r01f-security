package r01f.api.interfaces.security.login.saml;

import r01f.api.interfaces.security.SecurityServiceInterface;
import r01f.api.interfaces.security.login.FindServicesForLogin;
import r01f.model.security.login.saml.SAMLLogin;

public interface FindServicesForSAMLLogin<L extends SAMLLogin>
		 extends FindServicesForLogin<L>,
		 		 SecurityServiceInterface {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
}

