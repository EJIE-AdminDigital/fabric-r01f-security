package r01f.client.api.delegates.keystore;

import jakarta.inject.Provider;

import r01f.api.interfaces.keystore.CRUDServicesForKeystoreBase;
import r01f.model.security.keystore.KeystorePersistableObject;
import r01f.model.security.keystore.oids.KeystoreOIDs.KeystoreOID;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.api.delegates.ClientAPIDelegateForModelObjectCRUDServices;

public abstract class ClientAPIDelegateKeyStoreCRUDServicesBase<K extends KeystorePersistableObject>
	 		  extends ClientAPIDelegateForModelObjectCRUDServices<KeystoreOID,K> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateKeyStoreCRUDServicesBase(final Provider<SecurityContext> securityContextProvider,
													 final Marshaller modelObjectsMarshaller,
													 final CRUDServicesForKeystoreBase<K> crudServicesProxy) {
		super(securityContextProvider,
			  modelObjectsMarshaller,
			  crudServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
}
