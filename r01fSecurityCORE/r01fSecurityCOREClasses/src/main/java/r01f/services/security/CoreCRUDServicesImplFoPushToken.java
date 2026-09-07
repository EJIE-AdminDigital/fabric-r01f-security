package r01f.services.security;



import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.pushtoken.CRUDServicesForPushToken;
import r01f.eventbus.EventBus;
import r01f.guids.CommonOIDs.SystemID;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.pushtoken.PushToken;
import r01f.model.security.pushtoken.IsPushTokenID;
import r01f.model.security.pushtoken.PushTokenOID;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreCRUDServicesForModelObjectBase;

@Accessors(prefix="_")
public abstract class CoreCRUDServicesImplFoPushToken<T extends PushToken>
	 		  extends CoreCRUDServicesForModelObjectBase<PushTokenOID,T>
  		   implements CRUDServicesForPushToken<T>,
			 		  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreCRUDServicesImplFoPushToken(final ServicesCoreModuleConfig coreCfg,
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
	public CRUDResult<T> loadBy(final SecurityContext securityContext,
								final IsPushTokenID phone,final SystemID systemOid) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(CRUDServicesForPushToken.class)
				   .loadBy(securityContext,
						   phone,systemOid);
	}

}
