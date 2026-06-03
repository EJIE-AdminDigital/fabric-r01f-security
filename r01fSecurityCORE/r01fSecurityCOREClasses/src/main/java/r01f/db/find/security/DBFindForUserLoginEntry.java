package r01f.db.find.security;

import jakarta.persistence.EntityManager;

import r01f.api.interfaces.security.login.entry.FindServicesForUserLoginEntry;
import r01f.db.entities.security.DBEntityForUserLoginEntry;
import r01f.model.security.login.entry.UserLoginEntry;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityOIDs.UserLoginEntryOID;
import r01f.services.core.persistence.db.DBFindForModelObjectBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;
import r01f.services.interfaces.FindServicesForModelObject;

/**
 * Persistence layer User finder.
 */
public class DBFindForUserLoginEntry<E extends UserLoginEntry,
						   			 DB extends DBEntityForUserLoginEntry>
	 extends DBFindForModelObjectBase<UserLoginEntryOID,E,
	 								  DBPrimaryKeyForModelObject,DB>
  implements FindServicesForModelObject<UserLoginEntryOID,E>,
  			 FindServicesForUserLoginEntry<E> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBFindForUserLoginEntry(final Class<E> modelObjectType,final Class<DB> dbEntityType,
								   final DBModuleConfig dbCfg,
								   final EntityManager entityManager,
								   final Marshaller marshaller) {
		super(modelObjectType,dbEntityType,
			  dbCfg,
			  entityManager,
			  marshaller);
	}
}
