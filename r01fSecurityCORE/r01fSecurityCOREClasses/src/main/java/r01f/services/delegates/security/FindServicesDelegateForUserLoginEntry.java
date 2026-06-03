package r01f.services.delegates.security;



import r01f.api.interfaces.security.login.entry.FindServicesForUserLoginEntry;
import r01f.eventbus.EventBus;
import r01f.model.security.login.entry.UserLoginEntry;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityOIDs.UserLoginEntryOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.FindServicesForModelObjectDelegateBase;

public abstract class FindServicesDelegateForUserLoginEntry<L extends UserLoginEntry>
			  extends FindServicesForModelObjectDelegateBase<UserLoginEntryOID,L>
		   implements FindServicesForUserLoginEntry<L> {

/////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTOR   & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public FindServicesDelegateForUserLoginEntry(final ServicesCoreModuleConfig coreCfg,
												 final Class<L> modelObjectType,
												 final FindServicesForUserLoginEntry<L> dbCrud,
												 final Marshaller marshaller,
												 final EventBus eventBus) {
		super(coreCfg,
			  modelObjectType,
			  dbCrud,
			  eventBus);
	}
}
