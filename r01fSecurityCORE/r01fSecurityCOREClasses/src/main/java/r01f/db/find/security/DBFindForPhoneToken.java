package r01f.db.find.security;

import java.util.Collection;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.pushtoken.FindServicesForPushToken;
import r01f.db.entities.security.DBEntityForPushToken;
import r01f.db.transfer.security.PushTokenToDBEntityTransferDelegateBase;
import r01f.guids.CommonOIDs.SystemID;
import r01f.model.persistence.FindResult;
import r01f.model.persistence.FindResultBuilder;
import r01f.model.security.pushtoken.PushToken;
import r01f.model.security.pushtoken.PushTokenOID;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.SecurityToken;
import r01f.services.core.persistence.db.DBFindForModelObjectBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;
import r01f.types.contact.Phone;
import r01f.util.types.Strings;

@Slf4j
public abstract class DBFindForPhoneToken<T extends PushToken,DB extends DBEntityForPushToken>
	 		  extends DBFindForModelObjectBase<PushTokenOID,T,
	 								  		   DBPrimaryKeyForModelObject,DB>
  		   implements FindServicesForPushToken<T> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final PushTokenToDBEntityTransferDelegateBase<T,DB> _transformer;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBFindForPhoneToken(final Class<T> modelObjType,final Class<DB> dbType,
							   final DBModuleConfig dbCfg,
							   final EntityManager entityManager,
							   final Marshaller marshaller,
							   final PushTokenToDBEntityTransferDelegateBase<T,DB> modelObjToDBEntity) {
		super(modelObjType,dbType,
			  dbCfg,
			  entityManager,
			  marshaller);
		_transformer = modelObjToDBEntity;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public FindResult<T> findByPhone(final SecurityContext securityContext,
									 final Phone phoneNumber) {
		// Do the query
		TypedQuery<DB> query = this.getEntityManager()
								   .createQuery(Strings.customized("SELECT dbToken " +
										   							 "FROM {} dbToken " +
										   							"WHERE dbToken._phone = :phone",
										   						   _DBEntityType.getSimpleName()),
										   		_DBEntityType)
								   .setParameter("phone",phoneNumber.asString());
		Collection<DB> dbEntities = query.getResultList();

		// Return
		FindResult<T> outResult = FindResultBuilder.using(securityContext)
												   .on(_modelObjectType)
												   .foundDBEntities(dbEntities)
												   .transformedToModelObjectsUsing(_transformer);
		return outResult;
	}
	@Override
	public FindResult<T> findByPhone(final SecurityContext securityContext,
									 final SystemID systemId,
									 final Phone phoneNumber) {
		log.info(" > DBFindForPhoneToken find by phone : {} / systemId :{} " ,
												phoneNumber, systemId);
		// Do the query
		TypedQuery<DB> query = this.getEntityManager()
								   .createQuery(Strings.customized("SELECT dbToken " +
										   							 "FROM {} dbToken " +
										   							"WHERE dbToken._systemId = :systemId " +
										   							  "AND dbToken._phone = :phone",
										   						   _DBEntityType.getSimpleName()),
										   		_DBEntityType)
								   .setParameter("systemId",systemId.asString())
								   .setParameter("phone",phoneNumber.asString());
		Collection<DB> dbEntities = query.getResultList();

		// Return
		FindResult<T> outResult = FindResultBuilder.using(securityContext)
												   .on(_modelObjectType)
												   .foundDBEntities(dbEntities)
												   .transformedToModelObjectsUsing(_transformer);
		return outResult;
	}
	@Override
	public FindResult<T> findByPhoneNumberAndToken(final SecurityContext securityContext,
												   final SystemID systemId,
												   final Phone phoneNumber,
												   final SecurityToken token) {

		// Do the query
		TypedQuery<DB> query = this.getEntityManager()
								   .createQuery(Strings.customized("SELECT dbToken " +
										   							 "FROM {} dbToken " +
										   							"WHERE dbToken._systemId = :systemId " +
										   							  "AND dbToken._phone = :phone " +
										   							  "AND dbToken._token = :token",
										   						   _DBEntityType.getSimpleName()),
										   		_DBEntityType)
				    			   .setParameter("systemId",systemId.asString())
				    			   .setParameter("phone",phoneNumber.asString())
				    			   .setParameter("token",token.asString());
		Collection<DB> dbEntities = query.getResultList();

		// Return
		FindResult<T> outResult = FindResultBuilder.using(securityContext)
												   .on(_modelObjectType)
												   .foundDBEntities(dbEntities)
												   .transformedToModelObjectsUsing(_transformer);
		return outResult;
	}
	@Override
	public FindResult<T> findByToken(final SecurityContext securityContext,
									 final SystemID systemId,
									 final SecurityToken token) {
		// Do the query
		TypedQuery<DB> query = this.getEntityManager()
								   .createQuery(Strings.customized("SELECT dbToken " +
										   							 "FROM {} dbToken " +
										   							"WHERE dbToken._systemId = :systemId " +
										   							  "AND dbToken._token = :token",
										   						   _DBEntityType.getSimpleName()),
										   		_DBEntityType)
								   .setParameter("systemId",systemId.asString())
								   .setParameter("token",token.asString());
		Collection<DB> dbEntities = query.getResultList();

		// Return
		FindResult<T> outResult = FindResultBuilder.using(securityContext)
												   .on(_modelObjectType)
												   .foundDBEntities(dbEntities)
												   .transformedToModelObjectsUsing(_transformer);
		return outResult;
	}
}
