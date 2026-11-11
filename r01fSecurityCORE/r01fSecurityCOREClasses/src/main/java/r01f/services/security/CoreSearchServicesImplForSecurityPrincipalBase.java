package r01f.services.security;



import lombok.experimental.Accessors;
import r01f.api.interfaces.security.principal.SearchServicesForPrincipal;
import r01f.eventbus.EventBus;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.principal.SearchFilterForSecurityPrincipalBase;
import r01f.model.security.principal.SearchResultItemForSecurityPrincipalBase;
import r01f.objectstreamer.Marshaller;
import r01f.persistence.search.SearcherProvider;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.persistence.CoreSearchServicesForModelObjectBase;

@Accessors(prefix="_")
public abstract class CoreSearchServicesImplForSecurityPrincipalBase<P extends IsSecurityPrincipal,
														 F extends SearchFilterForSecurityPrincipalBase<P,?>,I extends SearchResultItemForSecurityPrincipalBase<P>>
			  extends CoreSearchServicesForModelObjectBase<F,I>
		   implements SearchServicesForPrincipal<P,
		   										 F,I>,
					  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreSearchServicesImplForSecurityPrincipalBase(final ServicesCoreModuleConfig coreCfg,
														  final Marshaller modelObjectsMarshaller,
														  final EventBus eventBus,
														  final SearcherProvider<F,I> searcherProvider) {
		super(coreCfg,
			  modelObjectsMarshaller, 
			  eventBus, 
			  searcherProvider);
	}

}
