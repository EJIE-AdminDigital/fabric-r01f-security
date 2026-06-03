package r01f.db.crud.security;

import jakarta.persistence.EntityManager;

import r01f.api.interfaces.security.authorization.CRUDServicesForAuthorizationTargetResource;
import r01f.db.entities.security.DBEntityForAuthorizationTargetResource;
import r01f.db.transfer.security.AuthorizationTargetResourceTransferToDBBase;
import r01f.model.security.authorization.AuthorizationTargetResource;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.services.core.persistence.db.DBCRUDForModelObjectBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;

public abstract class DBCRUDForAuthorizationTargetResource<R extends AuthorizationTargetResource,
												 		   DB extends DBEntityForAuthorizationTargetResource>
			  extends DBCRUDForModelObjectBase<AuthorizationTargetResourceOID,R,
											   DBPrimaryKeyForModelObject,DB>
		   implements CRUDServicesForAuthorizationTargetResource<R> {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	private final AuthorizationTargetResourceTransferToDBBase<R,DB> _transformer;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBCRUDForAuthorizationTargetResource(final Class<R> modelObjectType,final Class<DB> dbEntityType,
										 		final DBModuleConfig dbCfg,
										 		final EntityManager entityManager,
										 		final Marshaller marshaller,
										 		final AuthorizationTargetResourceTransferToDBBase<R,DB> modelObjToDBEntity) {
		super(modelObjectType,dbEntityType,
			  dbCfg,
			  entityManager,
			  marshaller);
		_transformer = modelObjToDBEntity;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	MODEL <TO> DB
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
												 final R modelObj,final DB dbEntity) {
		_transformer.setDBEntityFieldsFromModelObject(securityContext,
													  modelObj,dbEntity);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS TO IMPLEMENT
/////////////////////////////////////////////////////////////////////////////////////////

}
