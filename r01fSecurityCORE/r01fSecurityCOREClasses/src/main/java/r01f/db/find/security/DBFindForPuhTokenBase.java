package r01f.db.find.security;

import java.util.Collection;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.pushtoken.FindServicesForPushToken;
import r01f.db.entities.security.DBEntityForPushTokenBase;
import r01f.model.persistence.FindResult;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.pushtoken.IsPushToken;
import r01f.model.security.pushtoken.PushTokenOID;
import r01f.objectstreamer.Marshaller;
import r01f.oids.CommonOIDs.SystemID;
import r01f.oids.SecurityIDs.SecurityToken;
import r01f.services.core.persistence.db.DBFindForModelObjectBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;
import r01f.types.contact.Phone;
import r01f.util.types.Strings;

@Slf4j
public abstract class DBFindForPuhTokenBase<T extends IsPushToken,DB extends DBEntityForPushTokenBase>
	 		  extends DBFindForModelObjectBase<PushTokenOID,T,
	 								  		   DBPrimaryKeyForModelObject,DB>
  		   implements FindServicesForPushToken<T> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBFindForPuhTokenBase(final Class<T> modelObjType,final Class<DB> dbType,
								 final DBModuleConfig dbCfg,
								 final EntityManager entityManager,
								 final Marshaller marshaller) {
		super(modelObjType,dbType,
			  dbCfg,
			  entityManager,
			  marshaller);
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
										   							"WHERE dbToken._destinationId = :phone",
										   						   _DBEntityType.getSimpleName()),
										   		_DBEntityType)
								   .setParameter("phone",phoneNumber.asString());
		Collection<DB> dbEntities = query.getResultList();

		// Return
		return _buildResultsFromDBEntities(securityContext,
										   dbEntities);
	}
	@Override
	public FindResult<T> findByPhone(final SecurityContext securityContext,
									 final SystemID systemId,
									 final Phone phoneNumber) {
		log.info(" > find by phone : {} / systemId :{} " ,
												phoneNumber, systemId);
		// Do the query
		TypedQuery<DB> query = this.getEntityManager()
								   .createQuery(Strings.customized("SELECT dbToken " +
										   							 "FROM {} dbToken " +
										   							"WHERE dbToken._sourceSystemId = :systemId " +
										   							  "AND dbToken._destinationId = :phone",
										   						   _DBEntityType.getSimpleName()),
										   		_DBEntityType)
								   .setParameter("systemId",systemId.asString())
								   .setParameter("phone",phoneNumber.asString());
		Collection<DB> dbEntities = query.getResultList();

		// Return
		return _buildResultsFromDBEntities(securityContext,
										   dbEntities);
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
										   							"WHERE dbToken._sourceSystemId = :systemId " +
										   							  "AND dbToken._destinationId = :phone " +
										   							  "AND dbToken._token = :token",
										   						   _DBEntityType.getSimpleName()),
										   		_DBEntityType)
				    			   .setParameter("systemId",systemId.asString())
				    			   .setParameter("phone",phoneNumber.asString())
				    			   .setParameter("token",token.asString());
		Collection<DB> dbEntities = query.getResultList();

		// Return
		return _buildResultsFromDBEntities(securityContext,
										   dbEntities);
	}
	@Override
	public FindResult<T> findByToken(final SecurityContext securityContext,
									 final SystemID systemId,
									 final SecurityToken token) {
		// Do the query
		TypedQuery<DB> query = this.getEntityManager()
								   .createQuery(Strings.customized("SELECT dbToken " +
										   							 "FROM {} dbToken " +
										   							"WHERE dbToken._sourceSystemId = :systemId " +
										   							  "AND dbToken._token = :token",
										   						   _DBEntityType.getSimpleName()),
										   		_DBEntityType)
								   .setParameter("systemId",systemId.asString())
								   .setParameter("token",token.asString());
		Collection<DB> dbEntities = query.getResultList();

		// Return
		return _buildResultsFromDBEntities(securityContext,
										   dbEntities);
	}
}
