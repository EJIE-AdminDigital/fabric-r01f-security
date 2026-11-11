
package r01f.services.delegate.security.otp.persistence.hz;

import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.otp.persistence.CRUDServicesForOTPTask;
import r01f.cache.DistributedCacheService;
import r01f.eventbus.EventBus;
import r01f.hz.crud.security.otp.persistence.HZCRUDForOTPTask;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.otp.OTPTask;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.objectstreamer.Marshaller;
import r01f.oids.CommonOIDs.AppCode;
import r01f.oids.CommonOIDs.AppComponent;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.delegate.security.otp.persistence.base.hz.CRUDHazelcastServicesForModelObjectDelegateBase;
import r01f.types.contact.IsContactMean;

/**
 * Service layer delegated type for CRUD (Create/Read/Update/Delete) operations under a Hazelcast layer.
 */
@Slf4j
public class CRUDServicesDelegateForOTPTask
	 extends CRUDHazelcastServicesForModelObjectDelegateBase<OTPTaskOID,OTPTask> 
  implements CRUDServicesForOTPTask {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public CRUDServicesDelegateForOTPTask(final ServicesCoreModuleConfig cfg,
			                              final DistributedCacheService distibutedCacheService,
			                              final Marshaller marshaller,
			                              final EventBus eventBus) {
		super(cfg,
			  OTPTask.class,
			  new HZCRUDForOTPTask(distibutedCacheService),
			  eventBus);
	}

/////////////////////////////////////////////////////////////////////////////////////////
//	EXTENSION METHODS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public <C extends IsContactMean> CRUDResult<OTPTask> loadByContactMean(final SecurityContext securityContext, 
			                                	 						   final AppCode appCode,final AppComponent appComponent,
			                                	 						   final C contactMean) {
		return this.getServiceImplAs(CRUDServicesForOTPTask.class)
				   .loadByContactMean(securityContext,
						   			  appCode,appComponent,
						   			  contactMean);
	}

}