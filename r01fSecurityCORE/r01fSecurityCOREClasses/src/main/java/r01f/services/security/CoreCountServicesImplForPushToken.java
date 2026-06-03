package r01f.services.security;

import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.pushtoken.CountServicesForPushToken;
import r01f.eventbus.EventBus;
import r01f.model.security.pushtoken.PushToken;
import r01f.model.security.pushtoken.PushTokenOID;
import r01f.objectstreamer.Marshaller;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreCountServicesForModelObjectBase;

@Accessors(prefix="_")
public abstract class CoreCountServicesImplForPushToken<T extends PushToken>
			  extends CoreCountServicesForModelObjectBase<PushTokenOID,T>
		   implements CountServicesForPushToken<T>,
					  IsSecurityCoreServiceImpl {

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreCountServicesImplForPushToken(final ServicesCoreModuleConfig coreCfg,
											  final Marshaller modelObjectsMarshaller,
											  final EventBus eventBus,
											  final Provider<EntityManager> entityManagerProvider) {
		super(coreCfg,
			  modelObjectsMarshaller,
			  eventBus,
			  entityManagerProvider);
	}
}
