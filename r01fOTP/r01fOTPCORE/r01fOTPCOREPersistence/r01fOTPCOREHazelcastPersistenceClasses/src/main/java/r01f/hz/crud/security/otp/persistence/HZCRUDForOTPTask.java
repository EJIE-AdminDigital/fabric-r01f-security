package r01f.hz.crud.security.otp.persistence;

import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.otp.persistence.CRUDServicesForOTPTask;
import r01f.cache.DistributedCacheService;
import r01f.hz.crud.base.security.otp.persistence.HZCRUDForModelObjectBase;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.CRUDResultBuilder;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.otp.OTPTask;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.oids.CommonOIDs.AppCode;
import r01f.oids.CommonOIDs.AppComponent;
import r01f.types.contact.IsContactMean;

/**
 * Hazelcast Persistence layer
 */
@Slf4j
public class HZCRUDForOTPTask
	 extends HZCRUDForModelObjectBase<OTPTaskOID,OTPTask>
  implements CRUDServicesForOTPTask {
/////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public HZCRUDForOTPTask(final DistributedCacheService cacheService) {
		super(OTPTask.class, cacheService);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	API METHODS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public <C extends IsContactMean> CRUDResult<OTPTask> loadByContactMean(final SecurityContext securityContext, 
			                                	 						   final AppCode appCode,final AppComponent appComponent,
			                                	 						   final C contactMean) {
		 log.warn("...load by contact mean={}, appCode={} / appComp={}", 
				  contactMean,
				  appCode,appComponent);
		 if (_cacheService.getOrCreateCacheFor(OTPTask.class)
		  		   		  .isNullOrEmpty()) {
			 return CRUDResultBuilder.using(securityContext)
									 .on(_modelObjectType)
									 .notLoaded()
									 .becauseServerError("Not loaded entry for '{}' ({}/{}) because of any error at hazelcast cache",
											 			 contactMean,
											 			 appCode,appComponent)
									 .build();
		 }
		 OTPTask  task = _cacheService.getOrCreateCacheFor(OTPTask.class)
				  					  .getAll()
				  					  .values()
				  					  .stream()
		                              .filter(other -> (appCode != null ?  other.getAppCode().is(appCode) 
											                 		    : true)
													&& (appComponent != null ?  appComponent.is(other.getAppComponent())
																		 : true)	// ignore appComponent
													&& other.getContactMean() != null
													&& other.getContactMean().equals(contactMean))
		                              .findFirst().orElse(null);
		 if (task == null) {
			return CRUDResultBuilder.using(securityContext)
								    .on(_modelObjectType)
									.notLoaded()
									.becauseClientRequestedEntityWasNOTFound()
									.build();
						 		
		 }
		 return CRUDResultBuilder.using(securityContext)
								 .on(_modelObjectType)
								 .loaded()
								 .entity(task);
	}
}

