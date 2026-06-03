package r01f.services.security;



import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import lombok.experimental.Accessors;
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
import r01f.services.core.persistence.CoreFindServicesForModelObjectBase;

@Accessors(prefix="_")
public abstract class CoreFindServicesImplForXLNetsLogin<L extends XLNetsLogin,
                                                         F extends SearchFilterForXLNetsLoginBase<L,?>>
			  extends CoreFindServicesForModelObjectBase<LoginOID,L>
		   implements FindServicesForXLNetsLogin<L,F>,
		   			  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreFindServicesImplForXLNetsLogin(final ServicesCoreModuleConfig coreCfg,
										      final Marshaller modelObjectsMarshaller,
											  final EventBus eventBus,
											  final Provider<EntityManager> entityManagerProvider) {
		super(coreCfg,
			  modelObjectsMarshaller,
			  eventBus,
			  entityManagerProvider);
	}
/////////////////////////////////////////////////////////////////////////////////////////
// EXTENSION METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public FindResult<XLNetsLoginWorkPlaceCodeSummary> findWorkplaceSummaries(final SecurityContext securityContext) {
		return this.forSecurityContext(securityContext)
					.createDelegateAs(FindServicesForXLNetsLogin.class)
						.findWorkplaceSummaries(securityContext);
	}

	@Override @SuppressWarnings("unchecked")
	public FindResult<XLNetsLoginWorkPlaceCodeSummary> findWorkplaceSummaries(final SecurityContext securityContext,
			                                                                  final F filter) {
		return this.forSecurityContext(securityContext)
					.createDelegateAs(FindServicesForXLNetsLogin.class)
						.findWorkplaceSummaries(securityContext,filter);
	}



}

