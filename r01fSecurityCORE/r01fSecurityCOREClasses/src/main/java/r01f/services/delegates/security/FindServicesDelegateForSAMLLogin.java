package r01f.services.delegates.security;


import r01f.api.interfaces.security.login.saml.FindServicesForSAMLLogin;
import r01f.eventbus.EventBus;
import r01f.model.security.login.saml.SAMLLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.FindServicesForModelObjectDelegateBase;

public abstract class FindServicesDelegateForSAMLLogin<L extends SAMLLogin>
			  extends FindServicesForModelObjectDelegateBase<LoginOID,L>
		   implements FindServicesForSAMLLogin<L> {

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR   & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public FindServicesDelegateForSAMLLogin(final ServicesCoreModuleConfig coreCfg,
											final Class<L> modelObjectType,
											final FindServicesForSAMLLogin<L> dbCrud,
											final Marshaller marshaller,
											final EventBus eventBus) {
		super(coreCfg,
			  modelObjectType,
			  dbCrud,
			  eventBus);
	} 
}
