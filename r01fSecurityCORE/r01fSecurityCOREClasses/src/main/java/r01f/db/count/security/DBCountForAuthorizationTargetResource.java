package r01f.db.count.security;

import jakarta.persistence.EntityManager;

import lombok.experimental.Accessors;
import r01f.api.interfaces.security.authorization.CountServicesForAuthorizationTargetResource;
import r01f.db.entities.security.DBEntityForAuthorizationTargetResource;
import r01f.model.security.authorization.AuthorizationTargetResource;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.services.core.persistence.db.DBCountForModelObjectBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;

/**
 * Persistence layer for SecurityTarget
 */
@Accessors(prefix="_")
public abstract class DBCountForAuthorizationTargetResource<R extends AuthorizationTargetResource,
												 		    DB extends DBEntityForAuthorizationTargetResource>
			  extends DBCountForModelObjectBase<AuthorizationTargetResourceOID,R,
											    DBPrimaryKeyForModelObject,DB>
		   implements CountServicesForAuthorizationTargetResource<R> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBCountForAuthorizationTargetResource(final Class<R> modelObjectType,final Class<DB> dbEntityType,
										 		 final DBModuleConfig dbCfg,
										 		 final EntityManager entityManager,
										 		 final Marshaller marshaller) {
		super(modelObjectType,dbEntityType,
			  dbCfg,
			  entityManager,
			  marshaller);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS TO IMPLEMENT
/////////////////////////////////////////////////////////////////////////////////////////

}
