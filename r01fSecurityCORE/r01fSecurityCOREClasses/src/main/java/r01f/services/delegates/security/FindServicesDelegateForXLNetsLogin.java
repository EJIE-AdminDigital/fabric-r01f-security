package r01f.services.delegates.security;


import r01f.api.interfaces.security.login.xlnets.FindServicesForXLNetsLogin;
import r01f.eventbus.EventBus;
import r01f.model.persistence.FindResult;
import r01f.model.security.login.xlnets.SearchFilterForXLNetsLoginBase;
import r01f.model.security.login.xlnets.XLNetsLogin;
import r01f.model.security.login.xlnets.XLNetsLoginWorkPlaceCodeSummary;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.FindServicesForModelObjectDelegateBase;

public abstract class FindServicesDelegateForXLNetsLogin<L extends XLNetsLogin,
                                                         F extends SearchFilterForXLNetsLoginBase<L,?>>
			  extends FindServicesForModelObjectDelegateBase<LoginOID,L>
		   implements FindServicesForXLNetsLogin<L,F> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR   & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public FindServicesDelegateForXLNetsLogin(final ServicesCoreModuleConfig coreCfg,
											  final Class<L> modelObjectType,
											  final FindServicesForXLNetsLogin<L,F> dbCrud,
											  final Marshaller marshaller,
											  final EventBus eventBus) {
		super(coreCfg,
			  modelObjectType,
			  dbCrud,
			  eventBus);
	}
/////////////////////////////////////////////////////////////////////////////////////////
// EXTENSION METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public FindResult<XLNetsLoginWorkPlaceCodeSummary> findWorkplaceSummaries(final SecurityContext securityContext) {
		FindResult<XLNetsLoginWorkPlaceCodeSummary> outResults = this.getServiceImplAs(FindServicesForXLNetsLogin.class)
																			.findWorkplaceSummaries(securityContext);
		return outResults;
	}
	@Override @SuppressWarnings("unchecked")
	public FindResult<XLNetsLoginWorkPlaceCodeSummary> findWorkplaceSummaries(final SecurityContext securityContext,
			                                                                   final F filter ) {
		FindResult<XLNetsLoginWorkPlaceCodeSummary> outResults = this.getServiceImplAs(FindServicesForXLNetsLogin.class)
																			.findWorkplaceSummaries(securityContext,
																					                filter);
		return outResults;
	}

}
