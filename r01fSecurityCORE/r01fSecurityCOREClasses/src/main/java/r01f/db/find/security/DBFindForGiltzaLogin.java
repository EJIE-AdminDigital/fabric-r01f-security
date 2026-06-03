package r01f.db.find.security;

import jakarta.persistence.EntityManager;

import r01f.api.interfaces.security.login.giltza.FindServicesForGiltzaLogin;
import r01f.db.entities.security.DBEntityForGiltzaLogin;
import r01f.model.security.login.giltza.GiltzaLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.interfaces.FindServicesForModelObject;

/**
 * Persistence layer User finder.
 */
public class DBFindForGiltzaLogin<L extends GiltzaLogin,
						   		  DB extends DBEntityForGiltzaLogin>
	 extends DBFindForLoginBase<L,DB>
  implements FindServicesForModelObject<LoginOID,L>,
  			 FindServicesForGiltzaLogin<L> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBFindForGiltzaLogin(final Class<L> modelObjectType,final Class<DB> dbEntityType,
							    final DBModuleConfig dbCfg,
								final EntityManager entityManager,
								final Marshaller marshaller) {
		super(modelObjectType,dbEntityType,
			  dbCfg,
			  entityManager,
			  marshaller);
	}

}
