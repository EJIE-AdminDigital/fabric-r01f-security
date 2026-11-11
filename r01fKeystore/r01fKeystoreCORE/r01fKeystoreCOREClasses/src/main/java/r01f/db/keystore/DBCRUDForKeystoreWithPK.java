package r01f.db.keystore;

import jakarta.persistence.EntityManager;

import r01f.api.interfaces.keystore.CRUDServicesForKeystoreBase;
import r01f.db.entities.keystore.DBEntityKeystoreBase;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.keystore.KeystorePersistableObject;
import r01f.objectstreamer.Marshaller;
import r01f.services.core.persistence.db.TransfersModelObjectStateToDBEntity;
import r01f.services.core.persistence.db.config.DBModuleConfig;

/**
 * Persistence layer
 */
public abstract class DBCRUDForKeystoreWithPK<K extends KeystorePersistableObject, DB extends DBEntityKeystoreBase>
			  extends DBCRUDForKeystore<K,DB>
		   implements CRUDServicesForKeystoreBase<K> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBCRUDForKeystoreWithPK(final Class<K> modelObjectType,final Class<DB> dbEntityType,
								 final DBModuleConfig dbCfg,
								 final EntityManager entityManager,
								 final Marshaller marshaller,
								 final TransfersModelObjectStateToDBEntity<K,DB> transformerFromModelObjectStateToDBEntity ) {
		super(modelObjectType,dbEntityType,
			  dbCfg,
			  entityManager,
			  marshaller,
			  transformerFromModelObjectStateToDBEntity);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
			                                     final K modelObj,  DB dbEntity) {
		_transformerFromModelObjectStateToDBEntity.setDBEntityFieldsFromModelObject(securityContext, modelObj, dbEntity);
	}
}
