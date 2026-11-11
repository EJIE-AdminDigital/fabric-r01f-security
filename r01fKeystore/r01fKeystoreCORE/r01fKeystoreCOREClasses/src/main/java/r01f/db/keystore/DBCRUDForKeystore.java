package r01f.db.keystore;

import jakarta.persistence.EntityManager;
import r01f.api.interfaces.keystore.CRUDServicesForKeystoreBase;
import r01f.db.entities.keystore.DBEntityKeystoreBase;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.keystore.KeystorePersistableObject;
import r01f.model.security.keystore.oids.KeystoreOIDs.KeystoreOID;
import r01f.objectstreamer.Marshaller;
import r01f.services.core.persistence.db.DBCRUDForModelObjectBase;
import r01f.services.core.persistence.db.TransfersModelObjectStateToDBEntity;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;

/**
 * Persistence layer
 */
public abstract class DBCRUDForKeystore<K extends KeystorePersistableObject, DB extends DBEntityKeystoreBase>
			  extends DBCRUDForModelObjectBase<KeystoreOID,K,DBPrimaryKeyForModelObject,DB>
		   implements CRUDServicesForKeystoreBase<K> {
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final TransfersModelObjectStateToDBEntity<K,DB>  _transformerFromModelObjectStateToDBEntity;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBCRUDForKeystore(final Class<K> modelObjectType,final Class<DB> dbEntityType,
							 final DBModuleConfig dbCfg,
							 final EntityManager entityManager,
							 final Marshaller marshaller,
							 final TransfersModelObjectStateToDBEntity<K,DB> transformerFromModelObjectStateToDBEntity ) {
		super(modelObjectType,dbEntityType,
			  dbCfg,
			  entityManager,
			  marshaller);
		_transformerFromModelObjectStateToDBEntity = transformerFromModelObjectStateToDBEntity;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
			                                     final K modelObj,  final DB dbEntity) {
		_transformerFromModelObjectStateToDBEntity.setDBEntityFieldsFromModelObject(securityContext, modelObj, dbEntity);
	}
}
