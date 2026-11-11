package r01f.db.find.security;

import jakarta.persistence.EntityManager;
import r01f.api.interfaces.security.login.entry.FindServicesForLoginEntry;
import r01f.db.entities.security.DBEntityForLoginEntryBase;
import r01f.model.security.login.entry.LoginEntry;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityOIDs.LoginEntryOID;
import r01f.services.core.persistence.db.DBFindForModelObjectBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;
import r01f.services.interfaces.FindServicesForModelObject;

public class DBFindForLoginEntryBase<DB extends DBEntityForLoginEntryBase>
	 extends DBFindForModelObjectBase<LoginEntryOID,LoginEntry,
	 								  DBPrimaryKeyForModelObject,DB>
  implements FindServicesForModelObject<LoginEntryOID,LoginEntry>,
  			 FindServicesForLoginEntry {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBFindForLoginEntryBase(final Class<DB> dbEntityType,
								   final DBModuleConfig dbCfg,
								   final EntityManager entityManager,
								   final Marshaller marshaller) {
		super(LoginEntry.class,dbEntityType,
			  dbCfg,
			  entityManager,
			  marshaller);
	}
}
