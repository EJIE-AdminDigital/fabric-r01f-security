package r01f.db.crud.security;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import r01f.api.interfaces.security.pushtoken.CRUDServicesForPushToken;
import r01f.db.entities.security.DBEntityForPushToken;
import r01f.db.transfer.security.PushTokenToDBEntityTransferDelegateBase;
import r01f.guids.CommonOIDs.SystemID;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.CRUDResultBuilder;
import r01f.model.persistence.PersistencePerformedOperation;
import r01f.model.security.pushtoken.PushToken;
import r01f.model.security.pushtoken.IsPushTokenID;
import r01f.model.security.pushtoken.PushTokenOID;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.core.persistence.db.CompletesDBEntityBeforeCreateOrUpdate;
import r01f.services.core.persistence.db.DBCRUDForModelObjectBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;
import r01f.util.types.Strings;

public abstract class DBCRUDForPushToken<T extends PushToken,DB extends DBEntityForPushToken>
	 		  extends DBCRUDForModelObjectBase<PushTokenOID,T,
	 								  		   DBPrimaryKeyForModelObject,DB>
   		   implements CRUDServicesForPushToken<T>,
   		   			  CompletesDBEntityBeforeCreateOrUpdate<T,DB> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final PushTokenToDBEntityTransferDelegateBase<T,DB> _modelObjectToDBEntityTransformer;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBCRUDForPushToken(final Class<T> modelObjType,final Class<DB> dbType,
							  final DBModuleConfig dbCfg,
							  final EntityManager entityManager,
							  final Marshaller marshaller,
							  final PushTokenToDBEntityTransferDelegateBase<T,DB> modelObjToDBEntity) {
		super(modelObjType,dbType,
			  dbCfg,
			  entityManager,
			  marshaller);
		_modelObjectToDBEntityTransformer = modelObjToDBEntity;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EXTENSION METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void completeDBEntityBeforeCreateOrUpdate(final SecurityContext securityContext,
													 final PersistencePerformedOperation performedOp,
													 final T modelObj,
													 final DB dbEntity) {
		// nothing now
	}
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
			                                     final T modelObj,
			                                     final DB dbEntity) {
		_modelObjectToDBEntityTransformer.setDBEntityFieldsFromModelObject(securityContext,
																		   modelObj,
																		   dbEntity);
	}
	
	@Override @SuppressWarnings("resource")
	public CRUDResult<T> loadBy(final SecurityContext securityContext,
								final IsPushTokenID id,
								final SystemID systemOid) {
		TypedQuery<DB> query = this.getEntityManager()
									  .createQuery(Strings.customized("SELECT entity " +
											  							"FROM {} entity " +
											  						   "WHERE entity._pushTokenId = :phoneTokenId " +
											  							 "AND entity._pushTokenIdType = :phoneTokenIdType " +
											  						     "AND entity._systemId = :systemId ",
											  						  _DBEntityType.getSimpleName()),
											  	   _DBEntityType)
									  .setParameter("phoneTokenId",   id.asString())
									  .setParameter("phoneTokenIdType",id.getTypeId().asString())
									  .setParameter("systemId",systemOid.asString());
		DB dbEntity = query.getResultList().size() == 0 ? null : query.getResultList().get(0);
		if (dbEntity == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType).notLoaded()
									.becauseClientRequestedEntityWasNOTFound()
									.build();
		}
		CRUDResult<T> outEntities = CRUDResultBuilder.using(securityContext)
													 .on(_modelObjectType).loaded()
													 .dbEntity(dbEntity).transformedToModelObjectUsing(this);
		return outEntities;
	}
}
