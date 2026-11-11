package r01f.db.transfer.keystore;

import lombok.RequiredArgsConstructor;
import r01f.db.entities.keystore.DBEntityKeystoreBase;
import r01f.model.ModelObject;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.keystore.KeystoreBase;
import r01f.model.security.keystore.oids.KeystoreOIDs.KeystoreOID;
import r01f.services.core.persistence.db.DBEntity;
import r01f.services.core.persistence.db.TransfersModelObjectStateToDBEntity;

/**
 * Transfers a {@link ModelObject}'s state to a given {@link DBEntity}
 */
@RequiredArgsConstructor
public abstract class KeystoreToDBEntityTransferDelegateBase<K extends KeystoreBase<KeystoreOID, K>,
																			  DB extends DBEntityKeystoreBase>
		   implements TransfersModelObjectStateToDBEntity<K,DB> {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
												 final K modelObj, final DB dbEntity) {
		dbEntity.setOid(modelObj.getOid() != null ? modelObj.getOid().asString() : null);
		dbEntity.setPublicKey(modelObj.getPublicKey().getKeyValue().asString());
	}
}