package r01f.hz.crud.security.otp.persistence;

import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.otp.persistence.CRUDServicesForOTPTask;
import r01f.cache.DistributedCacheService;
import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.hz.crud.base.security.otp.persistence.HZCRUDForModelObjectBase;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.CRUDResultBuilder;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.model.security.otp.task.OTPTask;
import r01f.securitycontext.SecurityContext;
import r01f.types.contact.EMail;
import r01f.types.contact.Phone;

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
	public CRUDResult<OTPTask> create(final SecurityContext securityContext, 
			                          final OTPTask task, final long ttl, final TimeUnit timeUnit) {		
		throw new IllegalStateException(" >>> Not implemented at this layer");
	}	
	
	@Override
	public CRUDResult<OTPTask> loadByPhone(final SecurityContext securityContext,
										   final Phone phone) {
		return loadByPhone(securityContext,null, null, phone);
	
	}

	@Override
	public CRUDResult<OTPTask> loadByEmail(final SecurityContext securityContext,
										   final EMail email) {				 
		return loadByEMail(securityContext,null, null, email);
	}

	@Override
	public CRUDResult<OTPTask> loadByEMail(final SecurityContext securityContext, 
			                               final AppCode appCode, final AppComponent appComp,
			                               final EMail eMail) {
		 log.warn("..load by email '{}' , appCode  '{}' , appComp '{}'", 
				 			eMail, appCode, appComp);
		 if (_cacheService.getOrCreateCacheFor(OTPTask.class)
		  		   .isNullOrEmpty()) {
			 return CRUDResultBuilder.using(securityContext)
										.on(_modelObjectType)
										.notLoaded()
										.becauseServerError("Not loaded entry for '{}' because of any error at hazelcast cache", eMail)
						 		.build();
		 }
		 OTPTask  task = _cacheService.getOrCreateCacheFor(OTPTask.class)
				  					  .getAll()
				  					  .values()
				  					  .stream()
		                              .filter(other -> (appCode != null ?  other.getAppCode().is(appCode) 
											                 		    : true)
													&& (appComp != null ?  appComp.is(other.getAppComponent())
																		 : true)	// ignore appComponent
													&& other.getEmail() != null
													&& other.getEmail().equals(eMail))
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
	@Override
	public CRUDResult<OTPTask> loadByPhone(final SecurityContext securityContext, 
			                               final AppCode appCode, final  AppComponent appComp,
			                               final Phone phone) {
		 if (_cacheService.getOrCreateCacheFor(OTPTask.class)
		  		   .isNullOrEmpty()) {
			 return CRUDResultBuilder.using(securityContext)
										.on(_modelObjectType)
										.notLoaded()
										.becauseServerError("Not loaded entry for '{}' because of any error at hazelcast cache", phone)
						 		.build();
		 }
		 OTPTask  task = _cacheService.getOrCreateCacheFor(OTPTask.class)
				  					  .getAll()
				  					  .values()
				  					  .stream()
		                              .filter(other -> (appCode != null ?  other.getAppCode().is(appCode) 
		                            		  							: true)
													&& (appComp != null ? appComp.is(other.getAppComponent())
																		: true)	// ignore appComponent
													&& other.getPhone() != null
													&& other.getPhone().equals(phone))
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

