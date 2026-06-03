package r01f.services.delegates.security;



import r01f.api.interfaces.security.user.SearchServicesForUser;
import r01f.eventbus.EventBus;
import r01f.model.security.user.SearchFilterForUserBase;
import r01f.model.security.user.SearchResultItemForUserBase;
import r01f.model.security.user.User;
import r01f.persistence.search.Searcher;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.delegates.persistence.search.SearchServicesForModelObjectDelegateBase;

public abstract class SearchServicesDelegateForUser<U extends User,
													F extends SearchFilterForUserBase<U,?>,I extends SearchResultItemForUserBase<U>>
			  extends SearchServicesForModelObjectDelegateBase<F,I> 
		   implements SearchServicesForUser<U,F,I> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public SearchServicesDelegateForUser(final ServicesCoreModuleConfig coreCfg, 
										 final Searcher<F,I> searcher,
										 final EventBus eventBus) {
		super(coreCfg,
			  searcher,
			  eventBus);
	}
}
