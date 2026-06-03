package r01f.api.interfaces.security.otp.persistence;

import java.util.concurrent.TimeUnit;

import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.model.security.otp.task.OTPTask;
import r01f.securitycontext.SecurityContext;
import r01f.services.interfaces.CRUDServicesForModelObject;
import r01f.services.interfaces.ExposedServiceInterface;
import r01f.types.contact.EMail;
import r01f.types.contact.Phone;

/**
 * A CRUD Service for OTPTasks based on persistent store interface that abstracts the underlying implementation
 *        1. DB Based :
 *        2. Hazelcast Based
 */
@ExposedServiceInterface
public interface CRUDServicesForOTPTask 
	extends CRUDServicesForModelObject<OTPTaskOID,OTPTask>,
		    OTPPersistenceServiceInterface {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////

	public CRUDResult<OTPTask> create(final SecurityContext securityContext, 
			                          final OTPTask task,
						              final long ttl,final TimeUnit timeUnit);


	public CRUDResult<OTPTask> loadByEMail( final SecurityContext securityContext, 
			                                final AppCode appCode,final AppComponent appComponent,
							                final EMail eMail);
	
	public CRUDResult<OTPTask> loadByPhone(final SecurityContext securityContext, 
			                               final AppCode appCode,final AppComponent appComponent,
							               final Phone phone);

	public CRUDResult<OTPTask> loadByPhone(final SecurityContext securityContext, 
			                               final Phone phone);
	
	public CRUDResult<OTPTask> loadByEmail(final SecurityContext securityContext,
			                               final EMail email);
	
}
