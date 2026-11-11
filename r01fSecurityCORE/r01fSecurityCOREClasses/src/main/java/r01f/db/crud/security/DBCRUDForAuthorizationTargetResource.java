package r01f.db.crud.security;

import jakarta.persistence.EntityManager;
import r01f.api.interfaces.security.authorization.CRUDServicesForAuthorizationTargetResource;
import r01f.db.entities.security.DBEntityForAuthorizationTargetResourceBase;
import r01f.db.transfer.security.AuthorizationTargetResourceTransferToDB;
import r01f.model.security.authorization.AuthorizationTargetResource;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.services.core.persistence.db.DBCRUDForModelObjectBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;

public abstract class DBCRUDForAuthorizationTargetResource<DB extends DBEntityForAuthorizationTargetResourceBase>
			  extends DBCRUDForModelObjectBase<AuthorizationTargetResourceOID,AuthorizationTargetResource,
											   DBPrimaryKeyForModelObject,DB>
		   implements CRUDServicesForAuthorizationTargetResource {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	protected final AuthorizationTargetResourceTransferToDB<DB> _objToDBEntityTransformer;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBCRUDForAuthorizationTargetResource(final Class<DB> dbEntityType,
										 		final DBModuleConfig dbCfg,
										 		final EntityManager entityManager,
										 		final Marshaller marshaller) {
		super(AuthorizationTargetResource.class,dbEntityType,
			  dbCfg,
			  entityManager,
			  marshaller);
		_objToDBEntityTransformer = new AuthorizationTargetResourceTransferToDB<>();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	MODEL <TO> DB
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
												 final AuthorizationTargetResource modelObj,final DB dbEntity) {
		_objToDBEntityTransformer.setDBEntityFieldsFromModelObject(securityContext,
													  			   modelObj,dbEntity);
	}
}
