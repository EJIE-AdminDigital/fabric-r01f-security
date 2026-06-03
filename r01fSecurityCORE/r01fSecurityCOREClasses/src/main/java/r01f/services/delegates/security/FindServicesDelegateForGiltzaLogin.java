package r01f.services.delegates.security;



import r01f.api.interfaces.security.login.giltza.FindServicesForGiltzaLogin;
import r01f.eventbus.EventBus;
import r01f.model.security.login.giltza.GiltzaLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.FindServicesForModelObjectDelegateBase;

public abstract class FindServicesDelegateForGiltzaLogin<L extends GiltzaLogin>
			  extends FindServicesForModelObjectDelegateBase<LoginOID,L>
		   implements FindServicesForGiltzaLogin<L> {

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR   & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public FindServicesDelegateForGiltzaLogin(final ServicesCoreModuleConfig coreCfg,
											  final Class<L> modelObjectType,
											  final FindServicesForGiltzaLogin<L> dbCrud,
											  final Marshaller marshaller,
											  final EventBus eventBus) {
		super(coreCfg,
			  modelObjectType,
			  dbCrud,
			  eventBus);
	} 
}
