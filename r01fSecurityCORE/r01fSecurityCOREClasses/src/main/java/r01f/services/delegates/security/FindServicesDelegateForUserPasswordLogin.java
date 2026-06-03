package r01f.services.delegates.security;

import r01f.api.interfaces.security.login.userpassword.FindServicesForUserPasswordLogin;
import r01f.eventbus.EventBus;
import r01f.model.security.login.userpassword.UserPasswordLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.FindServicesForModelObjectDelegateBase;

public abstract class FindServicesDelegateForUserPasswordLogin<L extends UserPasswordLogin>
			  extends FindServicesForModelObjectDelegateBase<LoginOID,L>
		   implements FindServicesForUserPasswordLogin<L> {

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR   & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public FindServicesDelegateForUserPasswordLogin(final ServicesCoreModuleConfig coreCfg,
												 	final Class<L> modelObjectType,
												 	final FindServicesForUserPasswordLogin<L> dbCrud,
												 	final Marshaller marshaller,
												 	final EventBus eventBus) {
		super(coreCfg,
			  modelObjectType,
			  dbCrud,
			  eventBus);
	} 
}
