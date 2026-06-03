package r01f.services.security;


import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.pushtoken.FindServicesForPushToken;
import r01f.eventbus.EventBus;
import r01f.guids.CommonOIDs.SystemID;
import r01f.model.persistence.FindResult;
import r01f.model.security.pushtoken.PushToken;
import r01f.model.security.pushtoken.PushTokenOID;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.SecurityToken;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreFindServicesForModelObjectBase;
import r01f.types.contact.Phone;

@Accessors(prefix="_")
public abstract class CoreFindServicesImplForPushToken<T extends PushToken>
	 		  extends CoreFindServicesForModelObjectBase<PushTokenOID,T>
  		   implements FindServicesForPushToken<T>,
  		   			  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreFindServicesImplForPushToken(final ServicesCoreModuleConfig coreCfg,
											 final Marshaller modelObjectsMarshaller,
											 final EventBus eventBus,
											 final Provider<EntityManager> entityManagerProvider) {
		super(coreCfg,
			  modelObjectsMarshaller,
			  eventBus,
			  entityManagerProvider);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SERVICES EXTENSION
//	IMPORTANT!!! Do NOT put any logic in these methods ONLY DELEGATE!!!
/////////////////////////////////////////////////////////////////////////////////////////
	@Transactional
	@Override @SuppressWarnings("unchecked")
	public FindResult<T> findByPhone(final SecurityContext securityContext,
									 final Phone phoneNumber) {
		return this.forSecurityContext(securityContext)
					.createDelegateAs(FindServicesForPushToken.class)
						.findByPhone(securityContext,
									 phoneNumber);
	}
	@Transactional
	@Override @SuppressWarnings("unchecked")
	public FindResult<T> findByPhone(final SecurityContext securityContext,
									 final SystemID systemId,
									 final Phone phoneNumber) {
		return this.forSecurityContext(securityContext)
					.createDelegateAs(FindServicesForPushToken.class)
						.findByPhone(securityContext,
									 systemId,
									 phoneNumber);
	}
	@Transactional
	@Override @SuppressWarnings("unchecked")
	public FindResult<T> findByPhoneNumberAndToken(final SecurityContext securityContext,
												   final SystemID systemId,
												   final Phone phoneNumber,
												   final SecurityToken token) {
		return this.forSecurityContext(securityContext)
						.createDelegateAs(FindServicesForPushToken.class)
							.findByPhoneNumberAndToken(securityContext,
													   systemId,
													   phoneNumber,
													   token);
	}
	@Transactional
	@Override @SuppressWarnings("unchecked")
	public FindResult<T> findByToken(final SecurityContext securityContext,
									 final SystemID systemId,
									 final SecurityToken token) {
		return this.forSecurityContext(securityContext)
					.createDelegateAs(FindServicesForPushToken.class)
					.	findByToken(securityContext,
									systemId,
									token);
	}
}
