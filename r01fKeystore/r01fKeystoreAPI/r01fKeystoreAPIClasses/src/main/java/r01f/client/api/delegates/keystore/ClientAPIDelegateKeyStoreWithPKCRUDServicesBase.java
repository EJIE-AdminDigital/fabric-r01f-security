package r01f.client.api.delegates.keystore;

import jakarta.inject.Provider;

import r01f.api.interfaces.keystore.CRUDServicesForKeystoreWithPKBase;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.keystore.KeystoreWithPKPersistableObject;
import r01f.objectstreamer.Marshaller;

public abstract class ClientAPIDelegateKeyStoreWithPKCRUDServicesBase<K extends KeystoreWithPKPersistableObject>
	 		  extends ClientAPIDelegateKeyStoreCRUDServicesBase<K> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateKeyStoreWithPKCRUDServicesBase(final Provider<SecurityContext> securityContextProvider,
														       final Marshaller modelObjectsMarshaller,
														       final CRUDServicesForKeystoreWithPKBase<K> crudServicesProxy) {
		super(securityContextProvider,
			  modelObjectsMarshaller,
			  crudServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
}
