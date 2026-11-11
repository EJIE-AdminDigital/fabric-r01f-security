package r01f.services.security;


import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import lombok.experimental.Accessors;
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
import r01f.services.core.persistence.CoreCRUDServicesForModelObjectBase;

@Accessors(prefix="_")
public abstract class CoreCRUDServicesImplForLoginEntryBase
			  extends CoreCRUDServicesForModelObjectBase<LoginEntryOID,LoginEntry>
		   implements CRUDServicesForLoginEntry,
					  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreCRUDServicesImplForLoginEntryBase(final ServicesCoreModuleConfig coreCfg,
												 final Marshaller modelObjectsMarshaller,
												 final EventBus eventBus,
												 final Provider<EntityManager> entityManagerProvider) {
		super(coreCfg,
			  modelObjectsMarshaller,
			  eventBus,
			  entityManagerProvider);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override 
	public CRUDResult<LoginEntry> loadLastLoginEntryFor(final SecurityContext securityContext,
													    final IdentityProvider providerId,final LoginID loginId,
													    final LoginEntryEvent entryId) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(CRUDServicesForLoginEntry.class)
				   .loadLastLoginEntryFor(securityContext,
						   				  providerId,loginId,
						   				  entryId);
	}
}