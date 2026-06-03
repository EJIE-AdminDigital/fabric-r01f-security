package r01f.api.interfaces.security.login.google;

import r01f.api.interfaces.security.SecurityServiceInterface;
import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.login.google.GoogleLogin;
import r01f.securitycontext.SecurityContext;
import r01f.types.contact.EMail;

public interface CRUDServicesForGoogleLogin<L extends GoogleLogin>
		 extends CRUDServicesForLogin<L>,
		 		 SecurityServiceInterface {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns a login for the given google email
	 * @param securityContext the user auth data & context info
	 * @param email the email
	 * @return a {@link CRUDResult} that encapsulates the entity if it was loaded successfully
	 */
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
								final EMail email);

}

