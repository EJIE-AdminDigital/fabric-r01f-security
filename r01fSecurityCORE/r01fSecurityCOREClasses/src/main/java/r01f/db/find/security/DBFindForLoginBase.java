package r01f.db.find.security;

import jakarta.persistence.EntityManager;

import r01f.api.interfaces.security.login.FindServicesForLogin;
import r01f.db.entities.security.DBEntityForLogin;
import r01f.model.security.login.Login;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.services.core.persistence.db.DBFindForModelObjectBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;
import r01f.services.interfaces.FindServicesForModelObject;

/**
 * Persistence layer User finder.
 */
public abstract class DBFindForLoginBase<L extends Login,
						   		  		 DB extends DBEntityForLogin>
	   extends DBFindForModelObjectBase<LoginOID,L,
	 								  	DBPrimaryKeyForModelObject,DB>
    implements FindServicesForModelObject<LoginOID,L>,
  			   FindServicesForLogin<L> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBFindForLoginBase(final Class<L> modelObjectType,final Class<DB> dbEntityType,
							  final DBModuleConfig dbCfg,
							  final EntityManager entityManager,
							  final Marshaller marshaller) {
		super(modelObjectType,dbEntityType,
			  dbCfg,
			  entityManager,
			  marshaller);
	}
}
