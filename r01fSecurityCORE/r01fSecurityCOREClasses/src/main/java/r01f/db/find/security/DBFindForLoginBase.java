package r01f.db.find.security;

import jakarta.persistence.EntityManager;
import r01f.api.interfaces.security.login.FindServicesForLogin;
import r01f.db.entities.security.DBEntityForLoginBase;
import r01f.model.security.login.Login;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityOIDs.LoginOID;
import r01f.services.core.persistence.db.DBFindForModelObjectBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;

public abstract class DBFindForLoginBase<DB extends DBEntityForLoginBase>
			  extends DBFindForModelObjectBase<LoginOID,Login,
			 								   DBPrimaryKeyForModelObject,DB>
           implements FindServicesForLogin {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBFindForLoginBase(final Class<DB> dbEntityType,
							  final DBModuleConfig dbCfg,
							  final EntityManager entityManager,
							  final Marshaller marshaller) {
		super(Login.class,dbEntityType,
			  dbCfg,
			  entityManager,
			  marshaller);
	}
}
