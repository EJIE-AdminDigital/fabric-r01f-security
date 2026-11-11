package r01f.db.transfer.keystore;

import lombok.RequiredArgsConstructor;
import r01f.db.entities.keystore.DBEntityKeystoreWithPKBase;
import r01f.model.ModelObject;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.keystore.KeystoreWithPKBase;
import r01f.model.security.keystore.oids.KeystoreOIDs.KeystoreOID;
import r01f.services.core.persistence.db.DBEntity;

/**
 * Transfers a {@link ModelObject}'s state to a given {@link DBEntity}
 */
@RequiredArgsConstructor
public abstract class KeystoreWithPKToDBEntityTransferDelegateBase<K extends KeystoreWithPKBase<KeystoreOID, K>,
																   DB extends DBEntityKeystoreWithPKBase>
		      extends KeystoreToDBEntityTransferDelegateBase<K,DB> {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
												 final K modelObj,final DB dbEntity) {
		super.setDBEntityFieldsFromModelObject(securityContext, modelObj, dbEntity);
		dbEntity.setPrivateKey(modelObj.getPrivateKey().getKeyValue().asString());
	}
}