package r01f.services.security;

import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.login.entry.FindServicesForUserLoginEntry;
import r01f.eventbus.EventBus;
import r01f.model.security.login.entry.UserLoginEntry;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityOIDs.UserLoginEntryOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreFindServicesForModelObjectBase;

@Accessors(prefix="_")
public abstract class CoreFindServicesImplForUserLoginEntry<E extends UserLoginEntry>
			  extends CoreFindServicesForModelObjectBase<UserLoginEntryOID,E>
		   implements FindServicesForUserLoginEntry<E>,
		   			  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreFindServicesImplForUserLoginEntry(final ServicesCoreModuleConfig coreCfg,
												 final Marshaller modelObjectsMarshaller,
												 final EventBus eventBus,
												 final Provider<EntityManager> entityManagerProvider) {
		super(coreCfg,
			  modelObjectsMarshaller,
			  eventBus,
			  entityManagerProvider);
	}
}

