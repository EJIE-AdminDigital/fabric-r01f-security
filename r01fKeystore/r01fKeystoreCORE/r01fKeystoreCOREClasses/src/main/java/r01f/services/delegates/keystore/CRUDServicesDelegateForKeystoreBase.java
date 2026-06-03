package r01f.services.delegates.keystore;


import r01f.api.interfaces.keystore.CRUDServicesForKeystoreBase;
import r01f.eventbus.EventBus;
import r01f.model.security.keystore.KeystoreBase;
import r01f.model.security.keystore.oids.KeystoreOIDs.KeystoreOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.CRUDServicesForModelObjectDelegateBase;
import r01f.services.core.persistence.db.DBCRUDForModelObject;

/**
 * Service layer delegated type for CRUD (Create/Read/Update/Delete) operations
 */
public abstract class CRUDServicesDelegateForKeystoreBase<K extends KeystoreBase<KeystoreOID, K>>
	          extends CRUDServicesForModelObjectDelegateBase<KeystoreOID,K>
           implements CRUDServicesForKeystoreBase<K> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public CRUDServicesDelegateForKeystoreBase(final ServicesCoreModuleConfig cfg,
											   final Class<K> modelObjectType,
											   final DBCRUDForModelObject<KeystoreOID,K> dbCrud,
											   final EventBus eventBus) {
		super(cfg,
			  modelObjectType,
			  dbCrud,
			  eventBus);
	}	
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
}
