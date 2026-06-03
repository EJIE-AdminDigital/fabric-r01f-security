package r01f.db.crud.security;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.authorization.CRUDServicesForUserAuthorizationOnResource;
import r01f.db.entities.security.DBEntityForUserAuthorizationOnResource;
import r01f.db.transfer.security.UserAuthorizationOnResourceTransferToDBBase;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.CRUDResultBuilder;
import r01f.model.security.authorization.UserAuthorizationOnResource;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.UserRole;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.securitycontext.SecurityOIDs.UserAuthorizationOnResourceOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.persistence.db.DBCRUDForModelObjectBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;

@Slf4j
public abstract class DBCRUDForUserAuthorizationOnResource<A extends UserAuthorizationOnResource,
														   DB extends DBEntityForUserAuthorizationOnResource>
			  extends DBCRUDForModelObjectBase<UserAuthorizationOnResourceOID,A,
											   DBPrimaryKeyForModelObject,DB>
		   implements CRUDServicesForUserAuthorizationOnResource<A> {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	private final UserAuthorizationOnResourceTransferToDBBase<A,DB> _transformer;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBCRUDForUserAuthorizationOnResource(final Class<A> modelObjectType,final Class<DB> dbEntityType,
											    final DBModuleConfig dbCfg,
											    final EntityManager entityManager,
											    final Marshaller marshaller,
											    final UserAuthorizationOnResourceTransferToDBBase<A,DB> modelObjToDBEntity) {
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
												 final A modelObj,final DB dbEntity) {
		_transformer.setDBEntityFieldsFromModelObject(securityContext,
													  modelObj,dbEntity);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EXTENSION METHODS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public CRUDResult<A> loadBy(final SecurityContext securityContext,
								final UserOID userOid,
								final AuthorizationTargetResourceOID resourceOid) {
		log.debug("DBCRUDForAuthorizationUserPolicies.loadByUserOids {} - {} ", userOid, resourceOid);
		
		// Do the query
		TypedQuery<DB> query = this.getEntityManager()
								   .createQuery(Strings.customized("SELECT entity " +
																     "FROM {} entity " +
																    "WHERE entity._userOid = :userOid " +
																      "AND entity._resourceOid = :resourceOid ",
																   _DBEntityType.getSimpleName()),
																   _DBEntityType)
								   .setParameter("userOid", userOid.asString())
								   .setParameter("resourceOid",resourceOid.asString());

		DB dbEntity = (CollectionUtils.hasData(query.getResultList()))? query.getResultList().get(0) : null;
		if (dbEntity == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientRequestedEntityWasNOTFound()
									.build();
		}
		CRUDResult<A> outEntity =
				CRUDResultBuilder.using(securityContext)
								 .on(_modelObjectType)
								 .loaded()
								 .dbEntity(dbEntity)
								 .transformedToModelObjectUsing(this);
		return outEntity;
	}

	@Override
	public CRUDResult<A> loadBy(final SecurityContext securityContext,
								final UserOID userOid,
								final AuthorizationTargetResourceOID resourceOid,
								final UserRole role) {
		log.debug("DBCRUDForAuthorizationUserPolicies.loadByUserOids {} - {} ", userOid, resourceOid);
		
		// Do the query
		TypedQuery<DB> query = this.getEntityManager()
								   .createQuery(Strings.customized("SELECT entity " +
																     "FROM {} entity " +
																    "WHERE entity._userOid = :userOid " +
																      "AND entity._resourceOid = :resourceOid " +
																      "AND entity._role = :role",
																   _DBEntityType.getSimpleName()),
																   _DBEntityType)
								   .setParameter("userOid", userOid.asString())
								   .setParameter("resourceOid",resourceOid.asString())
								   .setParameter("role",role.asString());

		DB dbEntity = (CollectionUtils.hasData(query.getResultList()))? query.getResultList().get(0) : null;
		if (dbEntity == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientRequestedEntityWasNOTFound()
									.build();
		}
		CRUDResult<A> outEntity =
				CRUDResultBuilder.using(securityContext)
								 .on(_modelObjectType)
								 .loaded()
								 .dbEntity(dbEntity)
								 .transformedToModelObjectUsing(this);
		return outEntity;
	}
}
