package r01f.api.interfaces.security.otp.persistence;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import r01f.model.persistence.CRUDResult;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.otp.OTPTask;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.oids.CommonOIDs.AppCode;
import r01f.oids.CommonOIDs.AppComponent;
import r01f.services.interfaces.CRUDServicesForModelObject;
import r01f.services.interfaces.ExposedServiceInterface;
import r01f.types.TimeLapse;
import r01f.types.contact.IsContactMean;

/**
 * A CRUD Service for OTPTasks based on persistent store interface that abstracts the underlying implementation
 *        1. DB Based :
 *        2. Hazelcast Based
 */
@ExposedServiceInterface
public interface CRUDServicesForOTPTask 
		 extends CRUDServicesForModelObject<OTPTaskOID,OTPTask>,
		    	 IsOTPPersistenceServiceInterface {
/////////////////////////////////////////////////////////////////////////////////////////
//	LOAD
/////////////////////////////////////////////////////////////////////////////////////////
	public <C extends IsContactMean> CRUDResult<OTPTask> loadByContactMean(final SecurityContext securityContext, 
			                                	 						   final AppCode appCode,final AppComponent appComponent,
			                                	 						   final C contactMean);
/////////////////////////////////////////////////////////////////////////////////////////
//	CREATE
/////////////////////////////////////////////////////////////////////////////////////////
	public default CRUDResult<OTPTask> create(final SecurityContext securityContext,
											  final OTPTask task,
											  final long ttl,final TimeUnit timeUnit) {
		return this.create(securityContext, 
						  task,
						  Duration.ofMillis(timeUnit.toMillis(ttl)));
	}

	public default CRUDResult<OTPTask> create(final SecurityContext securityContext,
											  final OTPTask task,
											  final TimeLapse taskExpiresIn) {
		task.setExpiresAt(Instant.now().plus(taskExpiresIn.asDuration()));
		return this.create(securityContext, 
						  task);
	}
	public default CRUDResult<OTPTask> create(final SecurityContext securityContext,
											  final OTPTask task,
											  final Duration taskExpiresIn) {
		task.setExpiresAt(Instant.now().plus(taskExpiresIn));
		return this.create(securityContext, 
						  task);
	}
}
