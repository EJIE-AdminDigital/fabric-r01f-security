package r01f.db.find.security;

import jakarta.persistence.EntityManager;

import r01f.api.interfaces.security.login.saml.FindServicesForSAMLLogin;
import r01f.db.entities.security.DBEntityForSAMLLogin;
import r01f.model.security.login.saml.SAMLLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.interfaces.FindServicesForModelObject;

/**
 * Persistence layer User finder.
 */
public class DBFindForSAMLLogin<L extends SAMLLogin,
						   		DB extends DBEntityForSAMLLogin>
	 extends DBFindForLoginBase<L,DB>
  implements FindServicesForModelObject<LoginOID,L>,
  			 FindServicesForSAMLLogin<L> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBFindForSAMLLogin(final Class<L> modelObjectType,final Class<DB> dbEntityType,
							  final DBModuleConfig dbCfg,
							  final EntityManager entityManager,
							  final Marshaller marshaller) {
		super(modelObjectType,dbEntityType,
			  dbCfg,
			  entityManager,
			  marshaller);
	}
}
