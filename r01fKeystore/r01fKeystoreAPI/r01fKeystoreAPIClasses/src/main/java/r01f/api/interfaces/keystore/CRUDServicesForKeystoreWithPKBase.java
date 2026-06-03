package r01f.api.interfaces.keystore;

import r01f.model.security.keystore.KeystoreWithPKPersistableObject;
import r01f.services.interfaces.ExposedServiceInterface;

@ExposedServiceInterface
public interface CRUDServicesForKeystoreWithPKBase<K extends KeystoreWithPKPersistableObject>
		 extends CRUDServicesForKeystoreBase<K>,
		 		 KeystoreServiceInterface {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
}