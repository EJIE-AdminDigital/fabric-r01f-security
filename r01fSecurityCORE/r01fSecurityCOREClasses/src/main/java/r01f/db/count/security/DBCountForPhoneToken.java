package r01f.db.count.security;

import jakarta.persistence.EntityManager;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.pushtoken.CountServicesForPushToken;
import r01f.db.entities.security.DBEntityForPushTokenBase;
import r01f.model.security.pushtoken.IsPushToken;
import r01f.model.security.pushtoken.PushTokenOID;
import r01f.objectstreamer.Marshaller;
import r01f.services.core.persistence.db.DBCountForModelObjectBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;

/**
 * Persistence layer for SecurityTarget
 */
@Accessors(prefix="_")
public abstract class DBCountForPhoneToken<T extends IsPushToken,DB extends DBEntityForPushTokenBase>
			  extends DBCountForModelObjectBase<PushTokenOID,T,
											    DBPrimaryKeyForModelObject,DB>
		   implements CountServicesForPushToken<T> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBCountForPhoneToken(final Class<T> modelObjType,final Class<DB> dbEntityType,
								final DBModuleConfig dbCfg,
								final EntityManager entityManager,
								final Marshaller marshaller) {
		super(modelObjType,dbEntityType,
			  dbCfg,
			  entityManager,
			  marshaller);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS TO IMPLEMENT
/////////////////////////////////////////////////////////////////////////////////////////

}
