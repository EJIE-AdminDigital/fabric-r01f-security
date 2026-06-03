package r01f.db.crud.security;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.user.CRUDServicesForUser;
import r01f.db.entities.security.DBEntityForApiKeyLogin;
import r01f.db.entities.security.DBEntityForGiltzaLogin;
import r01f.db.entities.security.DBEntityForGoogleLogin;
import r01f.db.entities.security.DBEntityForLogin;
import r01f.db.entities.security.DBEntityForPairedPhoneLogin;
import r01f.db.entities.security.DBEntityForSAMLLogin;
import r01f.db.entities.security.DBEntityForUserBase;
import r01f.db.entities.security.DBEntityForUserPasswordLogin;
import r01f.db.entities.security.DBEntityForXLNetsLogin;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.CRUDResultBuilder;
import r01f.model.security.user.User;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.persistence.db.DBCRUDForModelObjectBase;
import r01f.services.core.persistence.db.TransfersModelObjectStateToDBEntity;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;
import r01f.types.contact.EMail;
import r01f.types.contact.Phone;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;

@Slf4j
public abstract class DBCRUDForUser<U extends User,
									DB extends DBEntityForUserBase>
			  extends DBCRUDForModelObjectBase<UserOID,U,
			  								   DBPrimaryKeyForModelObject,
			  								   DB>
		   implements CRUDServicesForUser<U> {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	protected final TransfersModelObjectStateToDBEntity<U,DB> _transformer;

	protected final Class<? extends DBEntityForXLNetsLogin> _dbEntityTypeForXLNetsLogin;
	protected final Class<? extends DBEntityForGoogleLogin> _dbEntityTypeForGoogleLogin;
	protected final Class<? extends DBEntityForGiltzaLogin> _dbEntityTypeForGiltzaLogin;
	protected final Class<? extends DBEntityForSAMLLogin> _dbEntityTypeForSAMLLogin;
	protected final Class<? extends DBEntityForUserPasswordLogin> _dbEntityTypeForUserPasswordLogin;
	protected final Class<? extends DBEntityForApiKeyLogin> _dbEntityTypeForApiKeyLogin;
	protected final Class<? extends DBEntityForPairedPhoneLogin> _dbEntityTypeForPairedPhoneLogin;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBCRUDForUser(final Class<U> modelObjectType,final Class<DB> dbEntityType,
						 final Class<? extends DBEntityForGoogleLogin> dbEntityTypeForGoogleLogin,final Class<? extends DBEntityForUserPasswordLogin> dbEntityTypeForUserPasswordLogin,
						 final DBModuleConfig dbCfg,
						 final EntityManager entityManager,
						 final Marshaller marshaller,
						 final TransfersModelObjectStateToDBEntity<U,DB> modelObjToDBEntity) {
		this(modelObjectType,dbEntityType,
			 null,dbEntityTypeForGoogleLogin,null,null,	// xlnets / giltza / saml not supported
			 dbEntityTypeForUserPasswordLogin,
			 null,null,	// api key & paired phone not supported
			 dbCfg,
			 entityManager,
			 marshaller,
			 modelObjToDBEntity);
	}
	public DBCRUDForUser(final Class<U> modelObjectType,final Class<DB> dbEntityType,
						 final Class<? extends DBEntityForXLNetsLogin> dbEntityTypeForXLNetsLogin,final Class<? extends DBEntityForGoogleLogin> dbEntityTypeForGoogleLogin,
						 final Class<? extends DBEntityForUserPasswordLogin> dbEntityTypeForUserPasswordLogin,
						 final DBModuleConfig dbCfg,
						 final EntityManager entityManager,
						 final Marshaller marshaller,
						 final TransfersModelObjectStateToDBEntity<U,DB> modelObjToDBEntity) {
		this(modelObjectType,dbEntityType,
			 dbEntityTypeForXLNetsLogin,dbEntityTypeForGoogleLogin,null,null,	// giltza / saml not supported
			 dbEntityTypeForUserPasswordLogin,
			 null,null,		// paired phone or api key login are not supported
			 dbCfg,
			 entityManager,
			 marshaller,
			 modelObjToDBEntity);
	}
	public DBCRUDForUser(final Class<U> modelObjectType,final Class<DB> dbEntityType,
						 final Class<? extends DBEntityForXLNetsLogin> dbEntityTypeForXLNetsLogin,final Class<? extends DBEntityForGoogleLogin> dbEntityTypeForGoogleLogin,final Class<? extends DBEntityForGiltzaLogin> dbEntityTypeForGiltzaLogin,final Class<? extends DBEntityForSAMLLogin> dbEntityTypeForSAMLLogin,
						 final Class<? extends DBEntityForUserPasswordLogin> dbEntityTypeForUserPasswordLogin,
						 final Class<? extends DBEntityForApiKeyLogin> dbEntityTypeForApiKeyLogin,final Class<? extends DBEntityForPairedPhoneLogin> dbEntityTypeForPairedPhoneLogin,
						 final DBModuleConfig dbCfg,
						 final EntityManager entityManager,
						 final Marshaller marshaller,
						 final TransfersModelObjectStateToDBEntity<U,DB> modelObjToDBEntity) {
		super(modelObjectType,dbEntityType,
			  dbCfg,
			  entityManager,
			  marshaller);
		_transformer = modelObjToDBEntity;

		_dbEntityTypeForXLNetsLogin = dbEntityTypeForXLNetsLogin;
		_dbEntityTypeForGoogleLogin = dbEntityTypeForGoogleLogin;
		_dbEntityTypeForGiltzaLogin = dbEntityTypeForGiltzaLogin;
		_dbEntityTypeForUserPasswordLogin = dbEntityTypeForUserPasswordLogin;
		_dbEntityTypeForPairedPhoneLogin = dbEntityTypeForPairedPhoneLogin;
		_dbEntityTypeForApiKeyLogin = dbEntityTypeForApiKeyLogin;
		_dbEntityTypeForSAMLLogin = dbEntityTypeForSAMLLogin;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
												 final U modelObj,final DB dbEntity) {
		_transformer.setDBEntityFieldsFromModelObject(securityContext,
													  modelObj,dbEntity);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EXTENSION METHODS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public CRUDResult<U> loadBy(final SecurityContext securityContext,
								final SecurityProviderID securityProviderId,final LoginID loginId) {
		log.debug("DBCRUDForUser.loadByLoginId {}/{}" ,
				 securityProviderId,loginId);
		// [0] - Get the [db entity] type for the [login]
		Class<? extends DBEntityForLogin> dbEntityTypeForLogin = null;
		if (securityProviderId.is(SecurityProviderID.GOOGLE)) {
			dbEntityTypeForLogin = _dbEntityTypeForGoogleLogin;
		} else if (securityProviderId.is(SecurityProviderID.GILTZA)) {
			dbEntityTypeForLogin = _dbEntityTypeForGiltzaLogin;
		} else if (securityProviderId.is(SecurityProviderID.XLNETS)) {
			dbEntityTypeForLogin = _dbEntityTypeForXLNetsLogin;
		} else if (securityProviderId.is(SecurityProviderID.USER_PASSWORD)) {
			dbEntityTypeForLogin = _dbEntityTypeForUserPasswordLogin;
		} else if (securityProviderId.is(SecurityProviderID.PAIRED_PHONE)) {
			dbEntityTypeForLogin = _dbEntityTypeForPairedPhoneLogin;
		} else if (securityProviderId.is(SecurityProviderID.API_KEY)) {
			dbEntityTypeForLogin = _dbEntityTypeForApiKeyLogin;
		} else if (securityProviderId.is(SecurityProviderID.SAML)
					|| securityProviderId.is(SecurityProviderID.OSAKIDETZA_AZURE_AD)) {
			dbEntityTypeForLogin = _dbEntityTypeForSAMLLogin;
		} else {
			throw new IllegalArgumentException(securityProviderId + " is NOT a supported " + SecurityProviderID.class.getSimpleName() + " value");
		}
		if (dbEntityTypeForLogin == null) {
			throw new IllegalArgumentException("The [db entity] type for " + securityProviderId + " was NOT set: this is a DEVELOPER FAULT; check the constructor!");
		}

		// [1] - Create the JPQL
		String jpql = Strings.customized("SELECT dbUser " +
									       "FROM {} dbUser " +
									       "JOIN {} dbLogin " +
									      "WHERE dbUser._oid = dbLogin._userOid " +
									        "AND dbLogin._loginId = :loginId",
									   _DBEntityType.getSimpleName(),
									   dbEntityTypeForLogin.getSimpleName());
		// [2] - Do the query
		TypedQuery<DB> query = this.getEntityManager()
								   .createQuery(jpql,
												_DBEntityType)
								   .setParameter("loginId",loginId.asString());

		DB dbEntity = CollectionUtils.hasData(query.getResultList()) ? query.getResultList().get(0) 	// BEWARE! if [api key login] there might exist MANY [api keys] for the same [loginId]... but all them belongs to the SAME user
																	 : null;
		if (dbEntity == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientRequestedEntityWasNOTFound()
									.build();
		}
		CRUDResult<U> outEntities =
				CRUDResultBuilder.using(securityContext)
								 .on(_modelObjectType)
								 .loaded()
								 .dbEntity(dbEntity)
								 .transformedToModelObjectUsing(this);
		return outEntities;
	}
	@Override
	public CRUDResult<U> loadBy(final SecurityContext securityContext,
								final EMail email) {
		log.debug("DBCRUDForUser.loadBy (email) {} " ,
				 email);
		// Do the query
		TypedQuery<DB> query = this.getEntityManager()
								   .createQuery(Strings.customized("SELECT entity " +
																     "FROM {} entity " +
																    "WHERE entity._email = :email",
																   _DBEntityType.getSimpleName()),
																   _DBEntityType)
								   .setParameter("email", email.asString());

		DB dbEntity = CollectionUtils.hasData(query.getResultList()) ? query.getResultList().get(0)
																	 : null;
		if (dbEntity == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientRequestedEntityWasNOTFound()
									.build();
		}
		CRUDResult<U> outEntities =
				CRUDResultBuilder.using(securityContext)
								 .on(_modelObjectType)
								 .loaded()
								 .dbEntity(dbEntity)
								 .transformedToModelObjectUsing(this);
		return outEntities;
	}
	@Override
	public CRUDResult<U> loadBy(final SecurityContext securityContext,
								final Phone phone) {
		log.debug("DBCRUDForUser.loadBy (phone) {} " ,
				 phone);
		// Do the query
		TypedQuery<DB> query = this.getEntityManager()
								   .createQuery(Strings.customized("SELECT entity " +
																     "FROM {} entity " +
																    "WHERE entity._phone = :phone",
																   _DBEntityType.getSimpleName()),
																   _DBEntityType)
								   .setParameter("phone", phone.asString());

		DB dbEntity = CollectionUtils.hasData(query.getResultList()) ? query.getResultList().get(0)
																	 : null;
		if (dbEntity == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientRequestedEntityWasNOTFound()
									.build();
		}
		CRUDResult<U> outEntities =
				CRUDResultBuilder.using(securityContext)
								 .on(_modelObjectType)
								 .loaded()
								 .dbEntity(dbEntity)
								 .transformedToModelObjectUsing(this);
		return outEntities;
	}
}
