package r01f.services.delegates.security.otp;


import r01f.api.interfaces.security.otp.OTPServiceBase;
import r01f.api.interfaces.security.otp.persistence.CRUDServicesForOTPTask;
import r01f.eventbus.EventBus;
import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.model.security.otp.task.OTPTask;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;

import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.delegates.sub.security.otp.OTPServicesSubDelegateForTaskLoading;
import r01f.services.interfaces.ServiceInterface;
import r01f.types.contact.EMail;
import r01f.types.contact.Phone;

 public abstract class OTPServicesDelegateBase
			implements OTPServiceBase,
		           	   ServiceInterface {	
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	 
	protected final OTPServicesSubDelegateForTaskLoading _servicesSubDelegateForTaskLoading;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public OTPServicesDelegateBase(final ServicesCoreModuleConfig coreConfig,
			                       final Marshaller modelObjectsMarshaller,final EventBus eventBus,
			                       final CRUDServicesForOTPTask taskPersistenceService ) {
		_servicesSubDelegateForTaskLoading = new OTPServicesSubDelegateForTaskLoading(coreConfig,
																					  modelObjectsMarshaller,
																					  eventBus,
																					  taskPersistenceService);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public OTPTask getTaskByOID(final SecurityContext securityContext,
			                    final OTPTaskOID taskOid) {	
		return _servicesSubDelegateForTaskLoading.loadTaskBy(securityContext,
				                                             null,null,	// appCode & appComponent
				                                             taskOid);
	}
	@Override
	public OTPTask getTaskByPhone(final SecurityContext securityContext,
			                      final AppCode appCode,final AppComponent appComponent,
			                      final Phone phone) {
		return _servicesSubDelegateForTaskLoading.loadTaskBy(securityContext,
				                                             appCode,appComponent,
				                                             phone);
	}
	@Override
	public OTPTask getTaskByEMail(final SecurityContext securityContext,
			                      final AppCode appCode,final AppComponent appComponent, 
			                      final EMail eMail) {
		return _servicesSubDelegateForTaskLoading.loadTaskBy(securityContext,
				                                             appCode,appComponent,
				                                             eMail);
	}
}
