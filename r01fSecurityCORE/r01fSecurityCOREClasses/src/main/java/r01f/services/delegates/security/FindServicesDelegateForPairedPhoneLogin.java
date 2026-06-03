package r01f.services.delegates.security;


import r01f.api.interfaces.security.login.pairedphone.FindServicesForPairedPhoneLogin;
import r01f.eventbus.EventBus;
import r01f.model.security.login.pairedphone.PairedPhoneLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.FindServicesForModelObjectDelegateBase;

public abstract class FindServicesDelegateForPairedPhoneLogin<L extends PairedPhoneLogin>
			  extends FindServicesForModelObjectDelegateBase<LoginOID,L>
		   implements FindServicesForPairedPhoneLogin<L> {

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR   & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public FindServicesDelegateForPairedPhoneLogin(final ServicesCoreModuleConfig coreCfg,
											       final Class<L> modelObjectType,
											       final FindServicesForPairedPhoneLogin<L> dbCrud,
											       final Marshaller marshaller,
											       final EventBus eventBus) {
		super(coreCfg,
			  modelObjectType,
			  dbCrud,
			  eventBus);
	} 
}
