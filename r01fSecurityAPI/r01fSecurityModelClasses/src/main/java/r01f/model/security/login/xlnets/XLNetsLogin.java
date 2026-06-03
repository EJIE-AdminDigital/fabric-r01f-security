package r01f.model.security.login.xlnets;

import r01f.guids.CommonOIDs.WorkPlaceCode;
import r01f.model.security.login.Login;

/**
 * XLNets login
 */
public interface XLNetsLogin
		 extends Login {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public WorkPlaceCode getWorkPlaceCode();
	public void setWorkPlaceCode(final WorkPlaceCode wp);
}
