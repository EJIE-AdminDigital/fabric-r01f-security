package r01f.services.security;



import lombok.experimental.Accessors;
import r01f.api.interfaces.security.user.SearchServicesForUser;
import r01f.eventbus.EventBus;
import r01f.model.security.user.SearchFilterForUserBase;
import r01f.model.security.user.SearchResultItemForUserBase;
import r01f.model.security.user.User;
import r01f.objectstreamer.Marshaller;
import r01f.persistence.search.SearcherProvider;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.persistence.CoreSearchServicesForModelObjectBase;

@Accessors(prefix="_")
public abstract class CoreSearchServicesImplForUser<U extends User,
													F extends SearchFilterForUserBase<U,?>,I extends SearchResultItemForUserBase<U>>
			  extends CoreSearchServicesForModelObjectBase<F,I>
		   implements SearchServicesForUser<U,
		   									F,I>,
					  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreSearchServicesImplForUser(final ServicesCoreModuleConfig coreCfg,
									   	 final Marshaller modelObjectsMarshaller,
									   	 final EventBus eventBus,
									   	 final SearcherProvider<F,I> searcherProvider) {
		super(coreCfg,
			  modelObjectsMarshaller, 
			  eventBus, 
			  searcherProvider);
	}

}
