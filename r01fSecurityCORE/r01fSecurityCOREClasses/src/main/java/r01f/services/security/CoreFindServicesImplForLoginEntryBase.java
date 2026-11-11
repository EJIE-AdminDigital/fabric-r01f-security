package r01f.services.security;

import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.login.entry.FindServicesForLoginEntry;
import r01f.eventbus.EventBus;
import r01f.model.security.login.entry.LoginEntry;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityOIDs.LoginEntryOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreFindServicesForModelObjectBase;

@Accessors(prefix="_")
public abstract class CoreFindServicesImplForLoginEntryBase
			  extends CoreFindServicesForModelObjectBase<LoginEntryOID,LoginEntry>
		   implements FindServicesForLoginEntry,
		   			  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreFindServicesImplForLoginEntryBase(final ServicesCoreModuleConfig coreCfg,
												 final Marshaller modelObjectsMarshaller,
												 final EventBus eventBus,
												 final Provider<EntityManager> entityManagerProvider) {
		super(coreCfg,
			  modelObjectsMarshaller,
			  eventBus,
			  entityManagerProvider);
	}
}

