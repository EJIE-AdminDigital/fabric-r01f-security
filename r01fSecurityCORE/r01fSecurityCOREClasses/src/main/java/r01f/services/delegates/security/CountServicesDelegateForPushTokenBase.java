package r01f.services.delegates.security;



import r01f.api.interfaces.security.pushtoken.CountServicesForPushToken;
import r01f.eventbus.EventBus;
import r01f.model.security.pushtoken.IsPushToken;
import r01f.model.security.pushtoken.PushTokenOID;
import r01f.objectstreamer.Marshaller;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.CountServicesForModelObjectDelegateBase;

public abstract class CountServicesDelegateForPushTokenBase<T extends IsPushToken>
			  extends CountServicesForModelObjectDelegateBase<PushTokenOID,T>
		   implements CountServicesForPushToken<T> {

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public CountServicesDelegateForPushTokenBase(final ServicesCoreModuleConfig coreCfg,
												 final Class<T> modelObjType,
												 final CountServicesForPushToken<T> dbCount,
												 final Marshaller marshaller,
												 final EventBus eventBus) {
		super(coreCfg,
			  modelObjType,
			  dbCount,
			  eventBus);
	}
}
