package r01f.services.delegates.sub.security.otp;

import r01f.api.interfaces.security.otp.persistence.CRUDServicesForOTPTask;
import r01f.eventbus.EventBus;
import r01f.model.persistence.PersistenceException;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.otp.OTPTask;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.objectstreamer.Marshaller;
import r01f.oids.CommonOIDs.AppCode;
import r01f.oids.CommonOIDs.AppComponent;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.types.contact.IsContactMean;

abstract class OTPServicesSubDelegateBase 	 {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final ServicesCoreModuleConfig _coreConfig;
	protected final Marshaller _modelObjectsMarshaller;
	protected final EventBus _eventBus;
	protected final CRUDServicesForOTPTask  _taskPersistenceService;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public OTPServicesSubDelegateBase(final ServicesCoreModuleConfig coreConfig,
			                          final Marshaller modelObjectsMarshaller,
			                          final EventBus eventBus,
			                          final CRUDServicesForOTPTask  taskPersistenceService) {
		_coreConfig = coreConfig;
		_modelObjectsMarshaller = modelObjectsMarshaller;
		_eventBus = eventBus;
		_taskPersistenceService  = taskPersistenceService;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////

	public <K> OTPTask loadTask(final SecurityContext securityContext,
								final OTPTaskOID taskOid) {
		try {
			return _taskPersistenceService.load(securityContext,
												taskOid).getOrThrow();
		} catch ( final PersistenceException perx) {
			return null;
		}
	}

	public <K> OTPTask loadTaskBy(final SecurityContext securityContext,
			                      final AppCode appCode,final AppComponent appComp,
		   				          final K key) {
	
		if (key instanceof OTPTaskOID) {
			OTPTaskOID taskOid = (OTPTaskOID)key;
			try {
			    return _taskPersistenceService.load(securityContext,
			    		                            taskOid).getOrThrow();
			} catch ( final PersistenceException perx) {
				return null;
			}
	    }
		else if (key instanceof IsContactMean) {
			IsContactMean contactMean = (IsContactMean)key;
			try {
				return _taskPersistenceService.loadByContactMean(securityContext,
						                                   		 appCode,appComp,
						                                   		 contactMean)
											  .getOrThrow();
			} catch ( final PersistenceException perx) {
				return null;
			}
		}
		throw new IllegalArgumentException(key.getClass() + " is NOT an instance of " +
										   OTPTaskOID.class + " or " + IsContactMean.class);
	}
	public <K> boolean validateRequest(final K key,
									   final OTPTask task) {
		if (key instanceof OTPTaskOID) {
			return key.equals(task.getOid());
		}
		else if (key instanceof IsContactMean) {
			IsContactMean contactMean = (IsContactMean)key;
			return contactMean.equals(task.getContactMean());
		}
		return false;
	}
	
}
