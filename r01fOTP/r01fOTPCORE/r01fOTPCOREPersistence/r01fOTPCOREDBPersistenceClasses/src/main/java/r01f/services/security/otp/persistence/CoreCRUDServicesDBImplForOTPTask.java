package r01f.services.security.otp.persistence;

import java.util.concurrent.TimeUnit;


import com.google.inject.Singleton;

import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.otp.persistence.CRUDServicesForOTPTask;
import r01f.client.api.otp.internal.OTPModuleCodes;
import r01f.eventbus.EventBus;
import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.model.annotations.ModelObjectsMarshaller;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.model.security.otp.task.OTPTask;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.bootstrap.annotations.core.CoreService;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.annotations.ServicesCoreConfigFor;
import r01f.services.core.persistence.CoreCRUDServicesForModelObjectBase;
import r01f.services.core.persistence.ServiceDelegateProvider;
import r01f.services.core.persistence.db.annotations.EntityManagerFor;
import r01f.services.delegate.security.otp.persistence.CRUDServicesDelegateForOTPTask;
import r01f.types.contact.EMail;
import r01f.types.contact.Phone;

@CoreService @Singleton
@Accessors(prefix="_")
public class CoreCRUDServicesDBImplForOTPTask
	extends CoreCRUDServicesForModelObjectBase<OTPTaskOID,OTPTask>
		implements CRUDServicesForOTPTask,
		           CoreOTPDBPersistenceService {
/////////////////////////////////////////////////////////////////////////////////////////
// DELEGATE PROVIDER :
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final ServiceDelegateProvider<CRUDServicesDelegateForOTPTask> _delegateProvider;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTRO
/////////////////////////////////////////////////////////////////////////////////////////	
	@Inject
	public CoreCRUDServicesDBImplForOTPTask(@ServicesCoreConfigFor(OTPModuleCodes.SECURITY_SERVICES_OTP_PERSISTENCE_FQN)  	final ServicesCoreModuleConfig coreCfg,
									    	@ModelObjectsMarshaller                                                         final Marshaller modelObjectsMarshaller,
																                                                            final EventBus eventBus,
										    @EntityManagerFor(OTPModuleCodes.SECURITY_SERVICES_OTP_PERSISTENCE_FQN) 	    final Provider<EntityManager> entityManagerProvider ) {

		super(coreCfg,
				  modelObjectsMarshaller,
				  eventBus,
				  entityManagerProvider);

		_delegateProvider =  securityContext -> new CRUDServicesDelegateForOTPTask(_coreConfig,
																				   CoreCRUDServicesDBImplForOTPTask.this.getFreshNewEntityManager(securityContext),
																                   _modelObjectsMarshaller,
																                   _eventBus);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Transactional
	@Override
	public CRUDResult<OTPTask> create(final SecurityContext securityContext,
			                          final OTPTask task, final  long ttl, final  TimeUnit timeUnit) {
		return this.forSecurityContext(securityContext)
						.createDelegateAs(CRUDServicesForOTPTask.class)
								.create(securityContext,
										task,ttl,timeUnit);
	}
	//@Transactional
	@Override
	public CRUDResult<OTPTask> loadByEMail(final SecurityContext securityContext,
			                               final AppCode appCode, final AppComponent appComponent,
			                               final EMail eMail) {
		return this.forSecurityContext(securityContext)
					.createDelegateAs(CRUDServicesForOTPTask.class)
						.loadByEMail(securityContext,
								     appCode,appComponent,eMail);
	}
	//@Transactional
	@Override
	public CRUDResult<OTPTask> loadByPhone(final SecurityContext securityContext,
			                               final AppCode appCode, final AppComponent appComponent,
			                               final Phone phone) {
		return this.forSecurityContext(securityContext)
					.createDelegateAs(CRUDServicesForOTPTask.class)
						.loadByPhone(securityContext,
								     appCode,appComponent,phone);
	}
	//@Transactional
	@Override
	public CRUDResult<OTPTask> loadByPhone(final SecurityContext securityContext,
			                               final Phone phone) {
		return this.forSecurityContext(securityContext)
					.createDelegateAs(CRUDServicesForOTPTask.class)
						.loadByPhone(securityContext,
								     phone);
	}
	//@Transactional
	@Override
	public CRUDResult<OTPTask> loadByEmail(final SecurityContext securityContext,
			                               final EMail email) {
		return this.forSecurityContext(securityContext)
					.createDelegateAs(CRUDServicesForOTPTask.class)
						.loadByEmail(securityContext,
								      email);
	}
}
