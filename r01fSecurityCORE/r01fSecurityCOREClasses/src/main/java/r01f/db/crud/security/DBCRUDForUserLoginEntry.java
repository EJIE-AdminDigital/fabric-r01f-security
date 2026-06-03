package r01f.db.crud.security;

import java.util.Collection;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.entry.CRUDServicesForUserLoginEntry;
import r01f.db.entities.security.DBEntityForUserLoginEntry;
import r01f.db.transfer.security.UserLoginEntryTransferToDBBase;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.CRUDResultBuilder;
import r01f.model.security.login.entry.UserLoginEntry;
import r01f.objectstreamer.Marshaller;
import r01f.patterns.Factory;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.securitycontext.SecurityIDS.SecurityToken;
import r01f.securitycontext.SecurityIDS.UserLoginEntryID;
import r01f.securitycontext.SecurityOIDs.UserLoginEntryOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.persistence.db.DBCRUDForModelObjectBase;
import r01f.services.core.persistence.db.DBEntityHasModelObjectDescriptor;
import r01f.services.core.persistence.db.TransformsDBEntityIntoModelObject;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;
import r01f.util.types.collections.Lists;

@Slf4j
public class DBCRUDForUserLoginEntry<E extends UserLoginEntry,DB extends DBEntityForUserLoginEntry>
	 extends DBCRUDForModelObjectBase<UserLoginEntryOID,E,
	 								  DBPrimaryKeyForModelObject,DB>
  implements CRUDServicesForUserLoginEntry<E> {

/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final UserLoginEntryTransferToDBBase<E,DB> _modelObjToDBEntity;
	private final Factory<E> _userLoginEntryFactory;

	@SuppressWarnings("unused")
	protected static <E extends UserLoginEntry,DB extends DBEntityForUserLoginEntry>
			  TransformsDBEntityIntoModelObject<DB,E> _createDefaultTransformsDBEntityIntoModelObjectUsing(final Factory<E> userLoginEntryFactory) {

				return new TransformsDBEntityIntoModelObject<DB,E>() {
								@Override
								public E dbEntityToModelObject(final SecurityContext securityContext,
															   final DB dbEntity) {
									// the DB entity DO NOT have a descriptor... so use a custom transformer
									E modelObj = userLoginEntryFactory.create();
									modelObj.setOid(dbEntity.getOid() != null ? UserLoginEntryOID.forId(dbEntity.getOid()) : null);

									modelObj.setDate(dbEntity.getEntryDate() != null ? dbEntity.getEntryDate() : null);

									modelObj.setId(dbEntity.getEntryType() != null ? UserLoginEntryID.forId(dbEntity.getEntryType()) : null);

									modelObj.setSecurityProviderId(dbEntity.getSecurityProvider() != null ? SecurityProviderID.forId(dbEntity.getSecurityProvider()) : null);
									modelObj.setLoginId(dbEntity.getLoginId() != null ? LoginID.forId(dbEntity.getLoginId()) : null);

									modelObj.setUserOid(dbEntity.getUserOid() != null ? UserOID.forId(dbEntity.getUserOid()) : null);
									modelObj.setImpersonatedUserOid(dbEntity.getImpersonatedUserOid() != null ? UserOID.forId(dbEntity.getImpersonatedUserOid()) : null);

									modelObj.setToken(dbEntity.getToken() != null ? SecurityToken.from(dbEntity.getToken()) : null);
									return modelObj;
								}
					  };
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBCRUDForUserLoginEntry(final Class<E> modelObjectType,final Class<DB> dbEntityType,
								   final DBModuleConfig dbCfg,
								   final EntityManager entityManager,
								   final Marshaller marshaller,
								   final UserLoginEntryTransferToDBBase<E,DB> modelObjToDBEntity,
								   final Factory<E> userLoginEntryFactory) {
		this(modelObjectType,dbEntityType,
			  // transforms [db entity] into model obj
			  _createDefaultTransformsDBEntityIntoModelObjectUsing(userLoginEntryFactory),
			  dbCfg,
			  entityManager,
			  marshaller,
			  modelObjToDBEntity,
			  userLoginEntryFactory);
	}
	public DBCRUDForUserLoginEntry(final Class<E> modelObjectType,final Class<DB> dbEntityType,
								   final TransformsDBEntityIntoModelObject<DB,E> transformsModelObjToDBEntity,
								   final DBModuleConfig dbCfg,
								   final EntityManager entityManager,
								   final Marshaller marshaller,
								   final UserLoginEntryTransferToDBBase<E,DB> modelObjToDBEntity,
								   final Factory<E> userLoginEntryFactory) {
		super(modelObjectType,dbEntityType,
			  transformsModelObjToDBEntity,
			  dbCfg,
			  entityManager,
			  marshaller);
		_modelObjToDBEntity = modelObjToDBEntity;
		_userLoginEntryFactory = userLoginEntryFactory;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	MODEL <TO> DB
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
												 final E modelObj,
												 final DB dbEntity) {
		_modelObjToDBEntity.setDBEntityFieldsFromModelObject(securityContext,
													  		 modelObj,dbEntity);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CRUDResult<E> loadLastLoginEntryFor(final SecurityContext securityContext,
											   final SecurityProviderID providerId,final LoginID loginId,
											   final UserLoginEntryID entryId) {
		////////// create the query
		CriteriaBuilder criteriaBuilder = _entityManager.getCriteriaBuilder();
		CriteriaQuery<DB> criteriaQuery = criteriaBuilder.createQuery(_DBEntityType);
		Root<? extends DB> root = criteriaQuery.from(_DBEntityType);

		// predicates
		Predicate providerIdPredicate = providerId != null ? criteriaBuilder.equal(root.<String>get("_securityProvider"),
														   						   providerId.asString())
														   : null;
		Predicate loginIdPredicate = criteriaBuilder.equal(root.<String>get("_loginId"),
														   loginId.asString());
		Predicate entryIdPredicate = criteriaBuilder.equal(root.<String>get("_entryType"),
														   entryId.asString());
		Collection<Predicate> andPredicatesCol = Lists.newArrayList();
		if (providerIdPredicate != null) andPredicatesCol.add(providerIdPredicate);
		andPredicatesCol.add(loginIdPredicate);
		andPredicatesCol.add(entryIdPredicate);

		Predicate andPredicates = criteriaBuilder.and(andPredicatesCol.toArray(new Predicate[andPredicatesCol.size()]));
		criteriaQuery.where(andPredicates);

		// order
		Order order = criteriaBuilder.desc(root.get("_createDate"));
		criteriaQuery.orderBy(order);

		DB dbEntity = _entityManager.createQuery(criteriaQuery)
									.setHint(QueryHints.READ_ONLY,HintValues.TRUE)
									.setMaxResults(1)	// just ONE result = the last one
									.getSingleResult();

		// Compose the PersistenceOperationResult object
		CRUDResult<E> outEntityLoadResult = null;
		if (dbEntity != null) {
			E modelObj = dbEntityToModelObject(securityContext,
											    dbEntity);
			outEntityLoadResult = CRUDResultBuilder.using(securityContext)
										  .on(_modelObjectType)
										  .loaded()
										  .entity(modelObj);
		} else {
			outEntityLoadResult = CRUDResultBuilder.using(securityContext)
										  .on(_modelObjectType)
										  .notLoaded()
										  .becauseClientRequestedEntityWasNOTFound()
										  		.about("providerId/loginId/entryType",Strings.customized("{}/{}/{}",
										  																 providerId,loginId,entryId))
										  		.build();
			log.trace(outEntityLoadResult.getDetailedMessage());
		}
		return outEntityLoadResult;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	PASSWORD RESET
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CRUDResult<E> loadForPasswordReset(final SecurityContext securityContext,
											  final UserOID userOid,
											  final SecurityToken token) {
		log.debug("DBCRUDForUserLoginEntry.loadForPasswordReset {} ", userOid);
		
		// loads a [db login entry] with PASSWORD_RECOVERY_REQUEST id
		DB dbEntity = _loadDBEntityBy(userOid,
									  UserLoginEntryID.forPasswordRecoveryRequest(),
									  token);
		if (dbEntity == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientRequestedEntityWasNOTFound()
									.build();
		}
		CRUDResult<E> outEntities = CRUDResultBuilder.using(securityContext)
													 .on(_modelObjectType)
													 .loaded()
													 .dbEntity(dbEntity)
													 .transformedToModelObjectUsing(this);
		return outEntities;
	}

	@Override
	public CRUDResult<E> updateAfterPasswordReset(final SecurityContext securityContext,
												  final UserOID userOid,
												  final SecurityToken token) {
		log.debug("DBCRUDForUserLoginEntry.updateAfterPasswordReset {} ", userOid);
		
		// Finds a [db login entry] with PASSWORD_RECOVERY_REQUEST id
		DB dbEntity = _loadDBEntityBy(userOid,
									  UserLoginEntryID.forPasswordRecoveryRequest(),
									  token);
		if (dbEntity == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientRequestedEntityWasNOTFound()
									.build();
		}
		// update it: set the token null (do NOT forget the descriptor!!)
		dbEntity.setToken(null);
		E modelObj = this.dbEntityToModelObject(securityContext,
												dbEntity);
		modelObj.setToken(null);

		// the login entry could have DBEntityHasModelObjectDescriptor, or not....[ By Default NOT]
		if (dbEntity instanceof DBEntityHasModelObjectDescriptor) {
			this.setDescriptorForDBEntity(modelObj,dbEntity);
		}
		// return
		CRUDResult<E> outEntity = CRUDResultBuilder.using(securityContext)
													 .on(_modelObjectType)
													 .loaded()
													 .dbEntity(dbEntity)
													 .transformedToModelObjectUsing(this);
		return outEntity;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	private DB _loadDBEntityBy(final UserOID userOid,final UserLoginEntryID entryId,final SecurityToken token) {
		TypedQuery<DB> query = this.getEntityManager()
									.createQuery(Strings.customized("SELECT entity " +
																	"FROM {} entity " +
																   "WHERE entity._userOid = :userOid " +
																	 "AND entity._entryType = :entryType " +
																	 "AND entity._token = :token",
																	_DBEntityType.getSimpleName()),
																	_DBEntityType)
									.setParameter("userOid",userOid.asString())
									.setParameter("entryType",entryId.asString())
									.setParameter("token",token.asString());
		DB dbEntity = (CollectionUtils.hasData(query.getResultList()))
								? query.getResultList().get(0)
								: null;
		return dbEntity;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	IMPERSONATION
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CRUDResult<E> pushImpersonationLogin(final SecurityContext securityContext,
												final UserOID impersonatingUserOid,final UserOID impersonatedUserOid,
												final SecurityToken token) {
		// [0] - Update all other user's impersonation requests: just a SINGLE impersonation should be active at a time for a given user
		String updateJpql = Strings.customized("UPDATE {} le " +
						       					  "SET le._entryType = '{}' " +	// set type = IMPERSONATION_NOT_VALID
						       					"WHERE le._userOid = :userOid " +	// userOid = impersonating user
						       					  "AND le._entryType = '{}'",			// entry type = IMPERSONATION_PENDING
						       				   _DBEntityType.getSimpleName(),
						       				   UserLoginEntryID.IMPERSONATION_NOT_VALID.asString(),
						       				   UserLoginEntryID.IMPERSONATION_PENDING.asString());
		TypedQuery<DB> qry = _entityManager.createQuery(updateJpql,
													    _DBEntityType)
										   .setParameter("userOid",impersonatingUserOid.asString());
		int updatedRecordsCount = qry.executeUpdate();
		log.debug("... {} [impersonation login entries] updated to id = {} for user {} (just a single [impersonation login entry] should be active for a given user)",
								 UserLoginEntryID.IMPERSONATION_NOT_VALID,
								 updatedRecordsCount,impersonatingUserOid);

		// [1] - Create a new impersonation

		E impersonationLoginEntry = _userLoginEntryFactory.create();
		UserLoginEntryOID oid  = UserLoginEntryOID.supply();
		impersonationLoginEntry.setOid(oid);
		impersonationLoginEntry.setUserOid(impersonatingUserOid);
		impersonationLoginEntry.setId(UserLoginEntryID.IMPERSONATION_PENDING);		// the new impersonation is pending to be used
		impersonationLoginEntry.setImpersonatedUserOid(impersonatedUserOid);
		impersonationLoginEntry.setToken(token);
		log.info(" \n\n  Warn! Created new login entry {} for impersionatio ImpersonatingUserOid {} , impersonatedUserOid {} based on scx {} , {}  !!! ",
				 							 oid,
				 							 impersonatingUserOid,impersonatedUserOid,
				 							 securityContext.getSecurityProviderId(),securityContext.getLoginId());
        // Warn! This is important,
		// because otherwise there is no SecurityProviderID or LoginID available in the impersonation filter.
		// @See
		impersonationLoginEntry.setSecurityProviderId(securityContext.getSecurityProviderId());
		impersonationLoginEntry.setLoginId(securityContext.getLoginId());

		// [99] - Return the impersonation login entry
		return this.create(securityContext,
						   impersonationLoginEntry);
	}
	@Override
	public CRUDResult<E> popImpersonationLogin(final SecurityContext securityContext,
											   final UserLoginEntryOID loginEntryOid) {
		// [1] - load the required login
		DBPrimaryKeyForModelObject pk = this.dbEntityPrimaryKeyFor(loginEntryOid);
		DB dbEntity = this.doLoadDBEntity(securityContext,
									 	  pk);
		// [2] - Update it's id to IMPERSONATION_NOT_VALID (it's used)
		if (dbEntity != null) {
			dbEntity.setEntryType(UserLoginEntryID.IMPERSONATION_NOT_VALID.asString());
			_entityManager.persist(dbEntity);
		}
		// [99] - Return the login
		CRUDResult<E> outEntityPop = null;
		if (dbEntity != null) {
			E modelObj = dbEntityToModelObject(securityContext,
											    		 dbEntity);
			outEntityPop = CRUDResultBuilder.using(securityContext)
										    .on(_modelObjectType)
										    .loaded()
										    .entity(modelObj);
		} else {
			outEntityPop = CRUDResultBuilder.using(securityContext)
										    .on(_modelObjectType)
										    .notLoaded()
										    .becauseClientRequestedEntityWasNOTFound()
										  		.about(loginEntryOid)
										  		.build();
			log.trace(outEntityPop.getDetailedMessage());
		}
		return outEntityPop;
	}
}
