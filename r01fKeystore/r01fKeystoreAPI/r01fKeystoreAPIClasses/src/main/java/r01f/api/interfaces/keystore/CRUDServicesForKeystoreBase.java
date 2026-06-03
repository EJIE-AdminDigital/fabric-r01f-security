package r01f.api.interfaces.keystore;

import r01f.model.security.keystore.KeystorePersistableObject;
import r01f.model.security.keystore.oids.KeystoreOIDs.KeystoreOID;
import r01f.services.interfaces.CRUDServicesForModelObject;
import r01f.services.interfaces.ExposedServiceInterface;

@ExposedServiceInterface
public interface CRUDServicesForKeystoreBase<K extends KeystorePersistableObject>
		 extends CRUDServicesForModelObject<KeystoreOID,K>,
		 		 KeystoreServiceInterface {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
}