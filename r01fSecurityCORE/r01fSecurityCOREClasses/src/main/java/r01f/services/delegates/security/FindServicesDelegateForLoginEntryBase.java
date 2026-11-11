package r01f.services.delegates.security;



import r01f.api.interfaces.security.login.entry.FindServicesForLoginEntry;
import r01f.eventbus.EventBus;
import r01f.model.security.login.entry.LoginEntry;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityOIDs.LoginEntryOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.FindServicesForModelObjectDelegateBase;

public abstract class FindServicesDelegateForLoginEntryBase
			  extends FindServicesForModelObjectDelegateBase<LoginEntryOID,LoginEntry>
		   implements FindServicesForLoginEntry {

/////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTOR   & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public FindServicesDelegateForLoginEntryBase(final ServicesCoreModuleConfig coreCfg,
												 final FindServicesForLoginEntry dbFind,
												 final Marshaller marshaller,
												 final EventBus eventBus) {
		super(coreCfg,
			  LoginEntry.class,
			  dbFind,
			  eventBus);
	}
}
