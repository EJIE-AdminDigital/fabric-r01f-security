package r01f.services.delegates.sub.security.otp;


import r01f.api.interfaces.security.otp.persistence.CRUDServicesForOTPTask;
import r01f.core.services.mail.model.EMailRFC822Address;
import r01f.eventbus.EventBus;
import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.model.persistence.PersistenceException;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.model.security.otp.task.OTPTask;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;

import r01f.services.core.ServicesCoreModuleConfig;
import r01f.types.contact.EMail;
import r01f.types.contact.Phone;


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
	public <K> OTPTask loadTaskBy(final SecurityContext scx,
			                      final AppCode appCode,final AppComponent appComp,
		   				          final K key) {
	
		if (key instanceof OTPTaskOID) {
			OTPTaskOID taskOid = (OTPTaskOID)key;
			try {
			    return _taskPersistenceService.load(scx,
			    		                            taskOid).getOrThrow();
			} catch ( final PersistenceException perx) {
				return null;
			}
	    }
		else if (key instanceof Phone) {
			Phone phone = (Phone)key;
			try {
				return _taskPersistenceService.loadByPhone(scx,
						                                   appCode,appComp,
												   		   phone).getOrThrow();
			} catch ( final PersistenceException perx) {
				return null;
			}
		}
		else if (key instanceof EMail) {
			EMail email = (EMail)key;
			try {
				return _taskPersistenceService.loadByEMail(scx,
						                                   appCode,appComp,
												   		   email).getOrThrow();
			} catch ( final PersistenceException perx) {
				return null;
			}

		}
		else if (key instanceof EMailRFC822Address) {
			EMail email = ((EMailRFC822Address)key).getEmail();
			try {
				return _taskPersistenceService.loadByEMail(scx,
						                                   appCode,appComp,
											   		       email).getOrThrow();
			} catch ( final PersistenceException perx) {
				return null;
			}
		}
		throw new IllegalArgumentException(key.getClass() + " is NOT an instance of " +
										   OTPTaskOID.class + " or " +
										   EMail.class + " or " + EMailRFC822Address.class + " or " +
										   Phone.class);
	}
}
