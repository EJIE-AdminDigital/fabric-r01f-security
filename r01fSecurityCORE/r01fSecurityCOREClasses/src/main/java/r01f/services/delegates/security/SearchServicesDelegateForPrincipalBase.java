package r01f.services.delegates.security;



import r01f.api.interfaces.security.principal.SearchServicesForPrincipal;
import r01f.eventbus.EventBus;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.principal.SearchFilterForSecurityPrincipalBase;
import r01f.model.security.principal.SearchResultItemForSecurityPrincipalBase;
import r01f.persistence.search.Searcher;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.delegates.persistence.search.SearchServicesForModelObjectDelegateBase;

public abstract class SearchServicesDelegateForPrincipalBase<P extends IsSecurityPrincipal,
														 	 F extends SearchFilterForSecurityPrincipalBase<P,?>,I extends SearchResultItemForSecurityPrincipalBase<P>>
			  extends SearchServicesForModelObjectDelegateBase<F,I> 
		   implements SearchServicesForPrincipal<P,F,I> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public SearchServicesDelegateForPrincipalBase(final ServicesCoreModuleConfig coreCfg, 
											      final Searcher<F,I> searcher,
											      final EventBus eventBus) {
		super(coreCfg,
			  searcher,
			  eventBus);
	}
}
