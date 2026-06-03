package r01f.api.interfaces.security.login.xlnets;

import r01f.api.interfaces.security.SecurityServiceInterface;
import r01f.api.interfaces.security.login.FindServicesForLogin;
import r01f.model.persistence.FindResult;
import r01f.model.security.login.xlnets.SearchFilterForXLNetsLoginBase;
import r01f.model.security.login.xlnets.XLNetsLogin;
import r01f.model.security.login.xlnets.XLNetsLoginWorkPlaceCodeSummary;
import r01f.securitycontext.SecurityContext;


public interface FindServicesForXLNetsLogin<L extends XLNetsLogin,
                                            F extends SearchFilterForXLNetsLoginBase<L,?>>
		 extends FindServicesForLogin<L>,
		 		 SecurityServiceInterface {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public FindResult<XLNetsLoginWorkPlaceCodeSummary> findWorkplaceSummaries(final SecurityContext securityContext);

	public FindResult<XLNetsLoginWorkPlaceCodeSummary> findWorkplaceSummaries(final SecurityContext securityContext,
			                                                                  final F filter);

}

