package r01f.db.crud.security;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.authorization.CRUDServicesForAuthorizationOnResource;
import r01f.db.entities.security.DBEntityForAuthorizationOnResourceBase;
import r01f.db.transfer.security.AuthorizationOnResourceTransferToDB;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.PersistenceRequestedOperation;
import r01f.model.security.authorization.SecurityPrincipalAuthorizationOnResource;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.SecurityRole;
import r01f.oids.SecurityOIDs.AuthorizationOnResourceOID;
import r01f.oids.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.core.persistence.db.DBCRUDForModelObjectBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;

@Slf4j
public abstract class DBCRUDForAuthorizationOnResource<DB extends DBEntityForAuthorizationOnResourceBase>
			  extends DBCRUDForModelObjectBase<AuthorizationOnResourceOID,SecurityPrincipalAuthorizationOnResource,
											   DBPrimaryKeyForModelObject,DB>
		   implements CRUDServicesForAuthorizationOnResource {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final AuthorizationOnResourceTransferToDB<DB> _objToDBEntityTransformer;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBCRUDForAuthorizationOnResource(final Class<DB> dbEntityType,
										    final DBModuleConfig dbCfg,
										    final EntityManager entityManager,
										    final Marshaller marshaller) {
		super(SecurityPrincipalAuthorizationOnResource.class,dbEntityType,
			  dbCfg,
			  entityManager,
			  marshaller);
		_objToDBEntityTransformer = new AuthorizationOnResourceTransferToDB<>();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	MODEL <TO> DB
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
												 final SecurityPrincipalAuthorizationOnResource modelObj,final DB dbEntity) {
		_objToDBEntityTransformer.setDBEntityFieldsFromModelObject(securityContext,
																   modelObj,dbEntity);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EXTENSION METHODS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public CRUDResult<SecurityPrincipalAuthorizationOnResource> loadBy(final SecurityContext securityContext,
																	   final SecurityPrincipalOID principalOid,
																	   final AuthorizationTargetResourceOID resourceOid) {
		// Do the query
		TypedQuery<DB> query = this.getEntityManager()
								   .createQuery(Strings.customized("SELECT dbAuthzOnRes " +
																     "FROM {} dbAuthzOnRes " +
																    "WHERE dbAuthzOnRes._securityPrincipalOid = :principalOid " +
																      "AND dbAuthzOnRes._resourceOid = :resourceOid ",
																   _DBEntityType.getSimpleName()),
																   _DBEntityType)
								   .setParameter("principalOid",principalOid.asString())
								   .setParameter("resourceOid",resourceOid.asString());

		DB dbEntity = (CollectionUtils.hasData(query.getResultList()))? query.getResultList().get(0) : null;
		return _crudResultForSingleEntity(securityContext,
										  PersistenceRequestedOperation.LOAD,
								   		  dbEntity);
	}
	@Override
	public CRUDResult<SecurityPrincipalAuthorizationOnResource> loadBy(final SecurityContext securityContext,
																	   final SecurityPrincipalOID principalOid,
																	   final AuthorizationTargetResourceOID resourceOid,
																	   final SecurityRole role) {
		// Do the query
		TypedQuery<DB> query = this.getEntityManager()
								   .createQuery(Strings.customized("SELECT dbAuthzOnRes " +
																     "FROM {} dbAuthzOnRes " +
																    "WHERE dbAuthzOnRes._securityPrincipalOid = :principalOid " +
																      "AND dbAuthzOnRes._resourceOid = :resourceOid " +
																      "AND dbAuthzOnRes._role = :role",
																   _DBEntityType.getSimpleName()),
																   _DBEntityType)
								   .setParameter("principalOid", principalOid.asString())
								   .setParameter("resourceOid",resourceOid.asString())
								   .setParameter("role",role.asString());

		DB dbEntity = (CollectionUtils.hasData(query.getResultList()))? query.getResultList().get(0) : null;
		return _crudResultForSingleEntity(securityContext,
										  PersistenceRequestedOperation.LOAD,
								   		  dbEntity);
	}
}
