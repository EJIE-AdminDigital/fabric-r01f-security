package r01f.db.find.security;

import jakarta.persistence.EntityManager;

import r01f.api.interfaces.security.login.apikey.FindServicesForApiKeyVersion;
import r01f.db.entities.security.DBEntityForApiKeyVersion;
import r01f.model.security.login.apikey.ApiKeyVersion;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityOIDs.ApiKeyVersionOID;
import r01f.services.core.persistence.db.DBFindForModelObjectBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;


/**
 * Persistence layer for User.
 */
public abstract class DBFindForApiKeyVersion<DB extends DBEntityForApiKeyVersion>
	 		  extends DBFindForModelObjectBase<ApiKeyVersionOID,ApiKeyVersion,
	 		  								   DBPrimaryKeyForModelObject,DB>
  		   implements FindServicesForApiKeyVersion {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBFindForApiKeyVersion(final Class<DB> dbEntityType,
								  final DBModuleConfig dbCfg,
							  	  final EntityManager entityManager,
							  	  final Marshaller marshaller) {
		super(ApiKeyVersion.class,dbEntityType,
			  dbCfg,
			  entityManager,
			  marshaller);
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	EXTENSION METHOD
/////////////////////////////////////////////////////////////////////////////////////////
}
