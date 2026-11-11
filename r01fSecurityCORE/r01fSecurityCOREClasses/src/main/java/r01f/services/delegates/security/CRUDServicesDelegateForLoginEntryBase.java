package r01f.services.delegates.security;



import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.entry.CRUDServicesForLoginEntry;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.entry.LoginEntry;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.LoginEntryEvent;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityOIDs.LoginEntryOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.CRUDServicesForModelObjectDelegateBase;

@Slf4j
public abstract class CRUDServicesDelegateForLoginEntryBase
			  extends CRUDServicesForModelObjectDelegateBase<LoginEntryOID,LoginEntry>
		   implements CRUDServicesForLoginEntry {

/////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTOR   & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public CRUDServicesDelegateForLoginEntryBase(final ServicesCoreModuleConfig coreCfg,
												 final CRUDServicesForLoginEntry dbCrud,
												 final Marshaller marshaller,
												 final EventBus eventBus) {
		super(coreCfg,
			  LoginEntry.class,
			  dbCrud,
			  eventBus);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override 
	public CRUDResult<LoginEntry> loadLastLoginEntryFor(final SecurityContext securityContext,
											   			final IdentityProvider providerId,final LoginID loginId,
											   			final LoginEntryEvent entryId) {
		if (loginId == null) throw new IllegalArgumentException("The loginId is mandatory to load last [login entry]");
		LoginEntryEvent theEntryId = entryId != null ? entryId
													  : LoginEntryEvent.LOGIN_OK;	// login ok by default
		return this.getServiceImplAs(CRUDServicesForLoginEntry.class)
				   .loadLastLoginEntryFor(securityContext,
						   				  providerId,loginId,
						   				  theEntryId);
	}
}
