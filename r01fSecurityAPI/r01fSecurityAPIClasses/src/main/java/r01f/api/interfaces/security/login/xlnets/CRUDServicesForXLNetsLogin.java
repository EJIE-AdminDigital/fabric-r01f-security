package r01f.api.interfaces.security.login.xlnets;

import r01f.api.interfaces.security.SecurityServiceInterface;
import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.guids.CommonOIDs.WorkPlaceCode;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.login.xlnets.XLNetsLogin;
import r01f.securitycontext.SecurityContext;

public interface CRUDServicesForXLNetsLogin<L extends XLNetsLogin>
		 extends CRUDServicesForLogin<L>,
		 		 SecurityServiceInterface {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns a login for the given login workplace code
	 * @param securityContext the user auth data & context info
	 * @param login the login
	 * @return a {@link CRUDResult} that encapsulates the entity if it was loaded successfully
	 */
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
								final WorkPlaceCode login);
}

