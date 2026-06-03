package r01f.services.delegates.security;


import r01f.api.interfaces.security.login.xlnets.SearchServicesForXLNetsLogin;
import r01f.eventbus.EventBus;
import r01f.model.security.login.SearchFilterForLoginBase;
import r01f.model.security.login.xlnets.SearchResultItemForXLNetsLogin;
import r01f.model.security.login.xlnets.XLNetsLogin;
import r01f.persistence.search.Searcher;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.delegates.persistence.search.SearchServicesForModelObjectDelegateBase;

public abstract class SearchServicesDelegateForXLNetsLogin<L extends XLNetsLogin,
                                                           F extends SearchFilterForLoginBase<L,?>>
			  extends SearchServicesForModelObjectDelegateBase<F,SearchResultItemForXLNetsLogin>
		   implements SearchServicesForXLNetsLogin<L,F> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public SearchServicesDelegateForXLNetsLogin(final ServicesCoreModuleConfig coreCfg,
			                                    final Searcher<F,SearchResultItemForXLNetsLogin> searcher,
										        final EventBus eventBus) {
		super(coreCfg,
			  searcher,
			  eventBus);
	}
}
