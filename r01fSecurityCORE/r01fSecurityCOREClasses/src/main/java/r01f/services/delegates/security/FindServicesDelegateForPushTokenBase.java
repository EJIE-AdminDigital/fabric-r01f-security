package r01f.services.delegates.security;



import jakarta.persistence.EntityManager;
import r01f.api.interfaces.security.pushtoken.FindServicesForPushToken;
import r01f.eventbus.EventBus;
import r01f.model.persistence.FindResult;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.pushtoken.IsPushToken;
import r01f.model.security.pushtoken.PushTokenOID;
import r01f.objectstreamer.Marshaller;
import r01f.oids.CommonOIDs.SystemID;
import r01f.oids.SecurityIDs.SecurityToken;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.FindServicesForModelObjectDelegateBase;
import r01f.types.contact.Phone;

public abstract class FindServicesDelegateForPushTokenBase<T extends IsPushToken>
	 		  extends FindServicesForModelObjectDelegateBase<PushTokenOID,T>
  		   implements FindServicesForPushToken<T> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public FindServicesDelegateForPushTokenBase(final ServicesCoreModuleConfig coreCfg,
												final Class<T> modelObjType,
												final FindServicesForPushToken<T> dbFind,
												final EntityManager entityManager,
											    final Marshaller marshaller,
					  			   		   	   	final EventBus eventBus) {
		super(coreCfg,
			  modelObjType,
			  dbFind,
			  eventBus);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EXTENSION METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public FindResult<T> findByPhone(final SecurityContext securityContext,
									 final Phone phoneNumber) {
		if (phoneNumber == null) throw new IllegalArgumentException("Phone number is null");
		return this.getServiceImplAs(FindServicesForPushToken.class)
					.findByPhone(securityContext,
								 phoneNumber);
	}
	@Override @SuppressWarnings("unchecked")
	public FindResult<T> findByPhone(final SecurityContext securityContext,
									 final SystemID systemId,
									 final Phone phoneNumber) {
		if (systemId == null) throw new IllegalArgumentException("System ID is null");
		if (phoneNumber == null) throw new IllegalArgumentException("Phone number is null");
		return this.getServiceImplAs(FindServicesForPushToken.class)
					.findByPhone(securityContext,
								 systemId,
								 phoneNumber);
	}
	@Override @SuppressWarnings("unchecked")
	public FindResult<T> findByPhoneNumberAndToken(final SecurityContext securityContext,
												   final SystemID systemId,
												   final Phone phoneNumber,
												   final SecurityToken token) {
		if (systemId == null) throw new IllegalArgumentException("System ID is null");
		if (phoneNumber == null) throw new IllegalArgumentException("Phone number is null");
		if (token == null) throw new IllegalArgumentException("Token is null");
		return this.getServiceImplAs(FindServicesForPushToken.class)
					.findByPhoneNumberAndToken(securityContext,
											   systemId,
											   phoneNumber,
											   token);
	}
	@Override @SuppressWarnings("unchecked")
	public FindResult<T> findByToken(final SecurityContext securityContext,
									 final SystemID systemId,
									 final SecurityToken token) {
		if (systemId == null) throw new IllegalArgumentException("System ID is null");
		if (token == null) throw new IllegalArgumentException("Token is null");
		return this.getServiceImplAs(FindServicesForPushToken.class)
				   	.findByToken(securityContext,
					   		     systemId,
					   		     token);
	}
}
