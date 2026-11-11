package r01f.services.delegates.security;



import r01f.api.interfaces.security.login.FindServicesForLogin;
import r01f.eventbus.EventBus;
import r01f.model.security.login.Login;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityOIDs.LoginOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.FindServicesForModelObjectDelegateBase;

public abstract class FindServicesDelegateForLoginBase
			  extends FindServicesForModelObjectDelegateBase<LoginOID,Login>
		   implements FindServicesForLogin {

/////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTOR   & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public FindServicesDelegateForLoginBase(final ServicesCoreModuleConfig coreCfg,
											final FindServicesForLogin dbFind,
											final Marshaller marshaller,
											final EventBus eventBus) {
		super(coreCfg,
			  Login.class,
			  dbFind,
			  eventBus);
	}
}
