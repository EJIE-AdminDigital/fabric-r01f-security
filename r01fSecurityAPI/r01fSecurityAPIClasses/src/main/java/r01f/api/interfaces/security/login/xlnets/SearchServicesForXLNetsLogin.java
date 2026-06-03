package r01f.api.interfaces.security.login.xlnets;

import r01f.api.interfaces.security.SecurityServiceInterface;
import r01f.model.security.login.SearchFilterForLoginBase;
import r01f.model.security.login.xlnets.SearchResultItemForXLNetsLogin;
import r01f.model.security.login.xlnets.XLNetsLogin;
import r01f.services.interfaces.SearchServices;

public interface SearchServicesForXLNetsLogin<L extends XLNetsLogin,
                                              F extends SearchFilterForLoginBase<L,?>>
		 extends SearchServices<F,SearchResultItemForXLNetsLogin>,
		         SecurityServiceInterface {
	// just extend
}

