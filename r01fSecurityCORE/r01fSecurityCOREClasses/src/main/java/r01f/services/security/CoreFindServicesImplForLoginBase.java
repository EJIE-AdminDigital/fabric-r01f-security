package r01f.services.security;

import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.login.FindServicesForLogin;
import r01f.eventbus.EventBus;
import r01f.model.security.login.Login;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityOIDs.LoginOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreFindServicesForModelObjectBase;

@Accessors(prefix="_")
public abstract class CoreFindServicesImplForLoginBase
			  extends CoreFindServicesForModelObjectBase<LoginOID,Login>
		   implements FindServicesForLogin,
		   			  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreFindServicesImplForLoginBase(final ServicesCoreModuleConfig coreCfg,
											final Marshaller modelObjectsMarshaller,
											final EventBus eventBus,
											final Provider<EntityManager> entityManagerProvider) {
		super(coreCfg,
			  modelObjectsMarshaller,
			  eventBus,
			  entityManagerProvider);
	}
}

