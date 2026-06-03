package r01f.services.keystore;


import jakarta.inject.Provider;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import lombok.experimental.Accessors;
import r01f.api.interfaces.keystore.CRUDServicesForKeystoreBase;
import r01f.eventbus.EventBus;
import r01f.model.security.keystore.KeystoreWithPKBase;
import r01f.model.security.keystore.oids.KeystoreOIDs.KeystoreOID;
import r01f.objectstreamer.Marshaller;
import r01f.services.core.ServicesCoreModuleConfig;

/**
 * Implements the persistence-related services which in turn are
 * delegated to {@link CRUDServicesImplForKeystoreWithPKBase}
 */
@Singleton
@Accessors(prefix="_")
public abstract class CRUDServicesImplForKeystoreWithPKBase<K extends KeystoreWithPKBase<KeystoreOID, K>>
			  extends CRUDServicesImplForKeystoreBase<K>
		   implements CRUDServicesForKeystoreBase<K>,
					  KeystoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public CRUDServicesImplForKeystoreWithPKBase(final ServicesCoreModuleConfig coreCfg,
												 final Marshaller modelObjectsMarshaller,
												 final EventBus eventBus,
												 final Provider<EntityManager> entityManagerProvider) {
		super(coreCfg,
			  modelObjectsMarshaller,
			  eventBus,
			  entityManagerProvider);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SERVICES EXTENSION
// 	IMPORTANT!!! Do NOT put any logic in these methods ONLY DELEGATE!!!
/////////////////////////////////////////////////////////////////////////////////////////

}
