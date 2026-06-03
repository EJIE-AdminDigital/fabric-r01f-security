
package r01f.services.delegate.security.otp.persistence;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.otp.persistence.CRUDServicesForOTPTask;
import r01f.db.crud.security.otp.persistence.DBCRUDForOTPTask;
import r01f.eventbus.EventBus;
import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.model.security.otp.task.OTPTask;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.CRUDServicesForModelObjectDelegateBase;
import r01f.services.core.persistence.db.config.DBModuleConfigBuilderForHibernate;
import r01f.services.security.otp.persistence.CoreOTPDBPersistenceService;
import r01f.types.contact.EMail;
import r01f.types.contact.Phone;

/**
 * Service layer delegated type for CRUD (Create/Read/Update/Delete) operations
 */
@Slf4j
public class CRUDServicesDelegateForOTPTask
	 extends CRUDServicesForModelObjectDelegateBase<OTPTaskOID,OTPTask>
  implements CRUDServicesForOTPTask,
  			 CoreOTPDBPersistenceService {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public CRUDServicesDelegateForOTPTask(final ServicesCoreModuleConfig cfg,
										  final EntityManager entityManager,
										  final Marshaller marshaller,
										  final EventBus eventBus) {
		super(cfg,
			  OTPTask.class,
			  new DBCRUDForOTPTask(DBModuleConfigBuilderForHibernate.dbModuleConfigFrom(cfg),
								   entityManager,
								   marshaller),
			  eventBus);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EXTENSION METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CRUDResult<OTPTask> loadByPhone(final SecurityContext securityContext,
										   final Phone phone) {
		return this.getServiceImplAs(CRUDServicesForOTPTask.class)
				   .loadByPhone(securityContext,
						   		phone);
	}

	@Override
	public CRUDResult<OTPTask> loadByEmail(final SecurityContext securityContext,
										   final EMail email) {
		return this.getServiceImplAs(CRUDServicesForOTPTask.class)
				   .loadByEmail(securityContext,
						   		email);
	}

	@Override
	public CRUDResult<OTPTask> create(final SecurityContext securityContext,
									  final OTPTask task, final  long ttl, final TimeUnit timeUnit) {
		log.info(" ... creating otp task with ' {} ttl  {} ' ", ttl, timeUnit);
		task.setExpirationDate(Instant.now().plus(Duration.ofMillis(timeUnit.toMillis(ttl))));
		return create(securityContext, task);
	}

	@Override
	public CRUDResult<OTPTask> loadByEMail(final SecurityContext securityContext,
										   final AppCode appCode, final AppComponent appComponent,
										   final EMail eMail) {
		log.info("> todo :: ignores appCode/ appComponent, email:{}", eMail);
		return loadByEmail(securityContext,eMail);
	}

	@Override
	public CRUDResult<OTPTask> loadByPhone(final SecurityContext securityContext,
										   final AppCode appCode, final AppComponent appComponent,
										   final Phone phone) {
		log.info("> todo :: ignores appCode/ appComponent, phone:{}", phone);
		return loadByPhone(securityContext,phone);
	}
}