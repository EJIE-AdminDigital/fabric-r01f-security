package r01f.db.find.security;

import jakarta.persistence.EntityManager;

import r01f.api.interfaces.security.login.google.FindServicesForGoogleLogin;
import r01f.db.entities.security.DBEntityForGoogleLogin;
import r01f.model.security.login.google.GoogleLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.interfaces.FindServicesForModelObject;

/**
 * Persistence layer User finder.
 */
public class DBFindForGoogleLogin<L extends GoogleLogin,
						   		  DB extends DBEntityForGoogleLogin>
	 extends DBFindForLoginBase<L,DB>
  implements FindServicesForModelObject<LoginOID,L>,
  			 FindServicesForGoogleLogin<L> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBFindForGoogleLogin(final Class<L> modelObjectType,final Class<DB> dbEntityType,
							    final DBModuleConfig dbCfg,
								final EntityManager entityManager,
								final Marshaller marshaller) {
		super(modelObjectType,dbEntityType,
			  dbCfg,
			  entityManager,
			  marshaller);
	}

}
