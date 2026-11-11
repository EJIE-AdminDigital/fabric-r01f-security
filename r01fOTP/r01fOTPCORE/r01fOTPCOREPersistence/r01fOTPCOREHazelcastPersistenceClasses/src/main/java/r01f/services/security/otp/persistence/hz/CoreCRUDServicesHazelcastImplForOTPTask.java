package r01f.services.security.otp.persistence.hz;

import com.google.inject.Singleton;

import jakarta.inject.Inject;
import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.otp.persistence.CRUDServicesForOTPTask;
import r01f.cache.DistributedCacheService;
import r01f.eventbus.EventBus;
import r01f.model.annotations.ModelObjectsMarshaller;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.otp.OTPTask;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.objectstreamer.Marshaller;
import r01f.oids.CommonOIDs.AppCode;
import r01f.oids.CommonOIDs.AppComponent;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.ServiceDelegateProvider;
import r01f.services.delegate.security.otp.persistence.hz.CRUDServicesDelegateForOTPTask;
import r01f.services.security.otp.persistence.base.hz.CoreCRUDServicesForModelObjectBase;
import r01f.types.contact.IsContactMean;

@Singleton
@Accessors(prefix="_")
public class CoreCRUDServicesHazelcastImplForOTPTask
	 extends CoreCRUDServicesForModelObjectBase<OTPTaskOID,OTPTask>
  implements CRUDServicesForOTPTask,
	         OTPHazelcastPersistenceService {
/////////////////////////////////////////////////////////////////////////////////////////
//DELEGATE PROVIDER :
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final ServiceDelegateProvider<CRUDServicesDelegateForOTPTask> _delegateProvider;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	@Inject
	public CoreCRUDServicesHazelcastImplForOTPTask( 				final ServicesCoreModuleConfig coreCfg,
											@ModelObjectsMarshaller final Marshaller modelObjectsMarshaller,
																	final EventBus eventBus,
																	final DistributedCacheService cacheService) {

		super(coreCfg,
				  modelObjectsMarshaller,
				  eventBus,
				  cacheService);
		_delegateProvider =  securityContext -> new CRUDServicesDelegateForOTPTask(_coreConfig,
				                                    cacheService,
								                  _modelObjectsMarshaller,
								                  _eventBus);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public <C extends IsContactMean> CRUDResult<OTPTask> loadByContactMean(final SecurityContext securityContext, 
			                                	 						   final AppCode appCode,final AppComponent appComponent,
			                                	 						   final C contactMean) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(CRUDServicesForOTPTask.class)
				   .loadByContactMean(securityContext,
								 	   appCode,appComponent,
								 	   contactMean);
	}
}
