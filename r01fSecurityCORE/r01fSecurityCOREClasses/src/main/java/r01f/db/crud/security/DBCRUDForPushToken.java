package r01f.db.crud.security;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import r01f.api.interfaces.security.pushtoken.CRUDServicesForPushToken;
import r01f.db.entities.security.DBEntityForPushTokenBase;
import r01f.db.transfer.security.PushTokentTransferToDB;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.CRUDResultBuilder;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.pushtoken.IsPushDestinationID;
import r01f.model.security.pushtoken.IsPushToken;
import r01f.model.security.pushtoken.PushTokenOID;
import r01f.objectstreamer.Marshaller;
import r01f.oids.CommonOIDs.SystemID;
import r01f.services.core.persistence.db.DBCRUDForModelObjectBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;
import r01f.util.types.Strings;

public abstract class DBCRUDForPushToken<T extends IsPushToken,DB extends DBEntityForPushTokenBase>
	 		  extends DBCRUDForModelObjectBase<PushTokenOID,T,
	 								  		   DBPrimaryKeyForModelObject,DB>
   		   implements CRUDServicesForPushToken<T> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final PushTokentTransferToDB<T,DB> _objToDBEntityTransformer;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBCRUDForPushToken(final Class<T> modelObjType,final Class<DB> dbType,
							  final DBModuleConfig dbCfg,
							  final EntityManager entityManager,
							  final Marshaller marshaller) {
		super(modelObjType,dbType,
			  dbCfg,
			  entityManager,
			  marshaller);
		_objToDBEntityTransformer = new PushTokentTransferToDB<>();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EXTENSION METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
			                                     final T modelObj,
			                                     final DB dbEntity) {
		_objToDBEntityTransformer.setDBEntityFieldsFromModelObject(securityContext,
																   modelObj,dbEntity);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override 
	public CRUDResult<T> loadBy(final SecurityContext securityContext,
								final SystemID srcSystemId,final IsPushDestinationID destId) {
		TypedQuery<DB> query = this.getEntityManager()
									  .createQuery(Strings.customized("SELECT dbPushToken " +
											  							"FROM {} dbPushToken " +
											  						   "WHERE dbPushToken._destinationId = :destId " +
											  							 "AND dbPushToken._destinationType = :destType " +
											  						     "AND dbPushToken._sourceSystemId = :srcId ",
											  						  _DBEntityType.getSimpleName()),
											  	   _DBEntityType)
									  .setParameter("destId",destId.asString())
									  .setParameter("destType",destId.getType())
									  .setParameter("srcId",srcSystemId.asString());
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
