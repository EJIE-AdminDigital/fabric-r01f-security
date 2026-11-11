package r01f.db.crud.security;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.entry.CRUDServicesForLoginEntry;
import r01f.db.entities.security.DBEntityForLoginBase;
import r01f.db.entities.security.DBEntityForLoginEntryBase;
import r01f.db.transfer.security.LoginEntryTransferToDB;
import r01f.db.transfer.security.TransformsDBEntityIntoLoginEntry;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.PersistenceRequestedOperation;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.entry.LoginEntry;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.LoginEntryEvent;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityOIDs.LoginEntryOID;
import r01f.services.core.persistence.db.DBCRUDForModelObjectBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;

@Slf4j
public abstract class DBCRUDForLoginEntryBase<DB extends DBEntityForLoginEntryBase>
	 		  extends DBCRUDForModelObjectBase<LoginEntryOID,LoginEntry,
	 								  		   DBPrimaryKeyForModelObject,DB>
           implements CRUDServicesForLoginEntry {

/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final Class<? extends DBEntityForLoginBase> _dbLoginType;
	
	private final LoginEntryTransferToDB<DB> _modelObjectToDBEntity;
	private final TransformsDBEntityIntoLoginEntry<DB> _dbEntityToModelObject;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBCRUDForLoginEntryBase(final Class<DB> dbEntityType,
								   final Class<? extends DBEntityForLoginBase> dbLoginType,
								   final DBModuleConfig dbCfg,
								   final EntityManager entityManager,
								   final Marshaller marshaller) {
		super(LoginEntry.class,dbEntityType,
			  dbCfg,
			  entityManager,
			  marshaller);
		_dbLoginType = dbLoginType;
		_modelObjectToDBEntity = new LoginEntryTransferToDB<>();
		_dbEntityToModelObject = new TransformsDBEntityIntoLoginEntry<>();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	MODEL <TO> DB
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
												 final LoginEntry modelObj,final DB dbEntity) {
		_modelObjectToDBEntity.setDBEntityFieldsFromModelObject(securityContext,
													  			modelObj,dbEntity);
	}
	@Override
	public LoginEntry dbEntityToModelObject(final SecurityContext securityContext,
											final DB dbEntity) {
		return _dbEntityToModelObject.dbEntityToModelObject(securityContext,
															dbEntity);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CRUDResult<LoginEntry> loadLastLoginEntryFor(final SecurityContext securityContext,
											   			final IdentityProvider providerId,final LoginID loginId,
											   			final LoginEntryEvent entryEvent) {
		log.debug("loadLastLoginEntryFor {}/{} (type={})" ,
				  providerId,loginId,
				  entryEvent);
		// [1] - Create the JPQL
		String jpql = Strings.customized("SELECT dbEntry " +
									       "FROM {} dbEntry " +
									       "JOIN {} dbLogin " +
									      "WHERE dbEntry._loginOid = dbLogin._oid " +
									        "AND dbEntry._loginId = :loginId " +
									        "AND dbEntry._entryEvent = :entryEvent " +
									        "AND dbLogin._securityPrincipalSourceIdentityProviderId = :identityProviderId ",
									   _DBEntityType.getSimpleName(),
									   _dbLoginType.getSimpleName());
		// [2] - Do the query
		TypedQuery<DB> query = this.getEntityManager()
								   .createQuery(jpql,
												_DBEntityType)
								   .setParameter("loginId",loginId.asString())
								   .setParameter("identityProviderId",providerId)
								   .setParameter("entryEvent",entryEvent);

		DB dbEntity = CollectionUtils.hasData(query.getResultList()) ? query.getResultList().get(0) 	// BEWARE! if [api key login] there might exist MANY [api keys] for the same [loginId]... but all them belongs to the SAME principal
																	 : null;
		return _crudResultForSingleEntity(securityContext,
										  PersistenceRequestedOperation.LOAD,	
										  dbEntity,
										  "providerId/loginId/loginEvent",Strings.customized("{}/{}/{}",
												   											 providerId,loginId,entryEvent));
	}
}
