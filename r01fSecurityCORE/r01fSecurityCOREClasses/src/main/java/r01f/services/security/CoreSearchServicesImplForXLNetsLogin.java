package r01f.services.security;



import lombok.experimental.Accessors;
import r01f.api.interfaces.security.login.xlnets.SearchServicesForXLNetsLogin;
import r01f.eventbus.EventBus;
import r01f.model.security.login.SearchFilterForLoginBase;
import r01f.model.security.login.xlnets.SearchResultItemForXLNetsLogin;
import r01f.model.security.login.xlnets.XLNetsLogin;
import r01f.objectstreamer.Marshaller;
import r01f.persistence.search.SearcherProvider;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.persistence.CoreSearchServicesForModelObjectBase;

@Accessors(prefix="_")
public abstract class CoreSearchServicesImplForXLNetsLogin<L extends XLNetsLogin,
                                                           F extends SearchFilterForLoginBase<L,?>>
			  extends CoreSearchServicesForModelObjectBase<F,SearchResultItemForXLNetsLogin>
		   implements SearchServicesForXLNetsLogin<L,F>,
					  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreSearchServicesImplForXLNetsLogin(final ServicesCoreModuleConfig coreCfg,
											   	 final Marshaller modelObjectsMarshaller,
											   	 final EventBus eventBus,
											   	 final SearcherProvider<F,SearchResultItemForXLNetsLogin> searcherProvider) {
		super(coreCfg,
			  modelObjectsMarshaller,
			  eventBus,
			  searcherProvider);
	}

}

