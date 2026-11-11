package r01f.services.security;



import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.pushtoken.CRUDServicesForPushToken;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.pushtoken.IsPushDestinationID;
import r01f.model.security.pushtoken.IsPushToken;
import r01f.model.security.pushtoken.PushTokenOID;
import r01f.objectstreamer.Marshaller;
import r01f.oids.CommonOIDs.SystemID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreCRUDServicesForModelObjectBase;

@Accessors(prefix="_")
public abstract class CoreCRUDServicesImplFoPushTokenBase<T extends IsPushToken>
	 		  extends CoreCRUDServicesForModelObjectBase<PushTokenOID,T>
  		   implements CRUDServicesForPushToken<T>,
			 		  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreCRUDServicesImplFoPushTokenBase(final ServicesCoreModuleConfig coreCfg,
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
								final SystemID srcSystemId,final IsPushDestinationID destId) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(CRUDServicesForPushToken.class)
				   .loadBy(securityContext,
						   srcSystemId,destId);
	}

}
