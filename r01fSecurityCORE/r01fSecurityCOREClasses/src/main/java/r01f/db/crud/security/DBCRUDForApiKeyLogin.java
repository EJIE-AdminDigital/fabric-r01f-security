package r01f.db.crud.security;

import java.time.Instant;
import java.util.Collection;
import java.util.stream.Collectors;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import r01f.api.interfaces.security.login.apikey.CRUDServicesForApiKeyLogin;
import r01f.api.interfaces.security.login.apikey.FindServicesForApiKeyLogin;
import r01f.db.entities.security.DBEntityForApiKeyLogin;
import r01f.db.find.security.DBFindForApiKeyLogin;
import r01f.db.transfer.security.ApiKeyLoginTransferToDBBase;
import r01f.guids.OID;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.CRUDResultBuilder;
import r01f.model.persistence.FindResult;
import r01f.model.security.login.apikey.ApiKeyLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityToken;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.persistence.db.TransformsDBEntityIntoModelObject;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.delegates.security.CRUDServicesDelegateForApiKeyLogin;
import r01f.types.AppVersion;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;


public class DBCRUDForApiKeyLogin<L extends ApiKeyLogin,
								  DB extends DBEntityForApiKeyLogin>
	 extends DBCRUDForLoginBase<L,DB>
  implements CRUDServicesForApiKeyLogin<L>,
  			 TransformsDBEntityIntoModelObject<DB,L> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBCRUDForApiKeyLogin(final Class<L> modelObjectType,final Class<DB> dbEntityType,
								final DBModuleConfig dbCfg,
							    final EntityManager entityManager,
							    final Marshaller marshaller,
							    final ApiKeyLoginTransferToDBBase<L,DB> modelObjToDBEntity) {
		super(modelObjectType,dbEntityType,
			  dbCfg,
			  entityManager,
			  marshaller,
			  modelObjToDBEntity);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	BEWARE!
// 	When using [api key login] a certain [user]/[loginId] might have MANY [api key login]
//	entries multiple [api key login] can be issued but just one might be active in a
//	certain [api key version] (other issued [api key login] within the SAME version
// 	are INVALIDATED
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
								final UserOID userOid) {
		// [0] - create a FINDer
		FindServicesForApiKeyLogin<L> find = new DBFindForApiKeyLogin<>(_modelObjectType,_DBEntityType,
																		_dbConfig,
																		_entityManager,
																		_modelObjectsMarshaller);
		// [1] - Find ACTIVE [api key logins] for the given [user]
		FindResult<L> loginFind = find.findByOwnerOid(securityContext,
													  userOid);

		// [2] - Get the latest [api key login]
		CRUDResult<L> loginLoad = _latestLoginLoadResultFrom(securityContext,
															 loginFind,
															 "userOid",userOid);
		// [99] Return
		return loginLoad;
	}
	@Override
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
								final LoginID login) {
		// [0] - create a FINDer
		FindServicesForApiKeyLogin<L> find = new DBFindForApiKeyLogin<>(_modelObjectType,_DBEntityType,
																		_dbConfig,
																		_entityManager,
																		_modelObjectsMarshaller);
		// [1] - Find ACTIVE [api key logins] for the given [user]
		FindResult<L> loginFind = find.findByLoginId(securityContext,
													 login);
		// [2] - Get the latest [api key login]
		CRUDResult<L> loginLoad = _latestLoginLoadResultFrom(securityContext,
															 loginFind,
															 "loginId",login);
		// [99] Return
		return loginLoad;
	}
	private CRUDResult<L> _latestLoginLoadResultFrom(final SecurityContext securityContext,
													 final FindResult<L> loginFind,
													 final String meta,final OID metaValue) {
		// BEWARE! If just filtering by [user], MULTIPLE [api key logins] will be
		//		   returned:
		//		   ie:
		// 				return super.loadBy(securityContext,
		//									loginId);  <- might return MULTIPLE [api key logins]
		// ... so additional information is needed like the [api key version]
		// ... but this method just have the [user oid] as param so
		//		1) Find ACTIVE [api key logins] for the given [user]
		//		2) Get the LATEST one (the one with the latest version)

		// a) get the logins
		Collection<L> logins = loginFind.hasSucceeded() ? loginFind.getOrThrow()
														: null;
		// b) filter valid ones
		Collection<L> validLogins = CollectionUtils.hasData(logins)
											? logins.stream()
												    .filter(ApiKeyLogin::isActiveNow)
												    .collect(Collectors.toList())
											: null;
		if (CollectionUtils.isNullOrEmpty(validLogins)) return CRUDResultBuilder.using(securityContext)
																			    .on(_modelObjectType)
																			    .notLoaded()
																			    .becauseClientRequestedEntityWasNOTFound()
																			    .about(meta,metaValue)
																			    .build();
		// c) order by the version number
		Collection<L> loginsOrderedByVersion = validLogins.stream()
														  .sorted((l1,l2) -> AppVersion.COMPARATOR.compare(l1.getApiKeyVersion(),l2.getApiKeyVersion()))
														  .collect(Collectors.toList());
		// d) get the first
		L latestVersionLogin = CollectionUtils.firstOf(loginsOrderedByVersion);

		// z) Return
		return CRUDResultBuilder.using(securityContext)
								.on(_modelObjectType)
								.loaded()
								.entity(latestVersionLogin);
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	EXTENSION METHOD
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CRUDResult<L> loadByPublicKey(final SecurityContext securityContext,
										 final SecurityToken publicKey) {
		TypedQuery<DB> qry = _entityManager.createQuery(Strings.customized("SELECT k " +
						 												    "FROM {} k " +
						 											       "WHERE k._publicKey = :publicKey",
						 												  _DBEntityType.getSimpleName()),
														_DBEntityType)
										   .setParameter("publicKey",publicKey);
		qry.setHint(QueryHints.READ_ONLY,HintValues.TRUE);
		CRUDResult<L> outApiKeyLoadResult = _crudResultForSingleEntity(securityContext,
																	   qry.getResultList(),
																	   "publicKey",publicKey.asString());
		return outApiKeyLoadResult;
	}
	@Override
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
								final LoginID loginId,final AppVersion apiKeyVersion) {
		TypedQuery<DB> qry = _entityManager.createQuery(Strings.customized("SELECT k " +
																			 "FROM {} k " +
																			"WHERE (k._validFrom <= :from AND k._validTo IS NULL) " +
																			  "AND k._active = TRUE " +		// this is redundant (if _validTo = null, active MUST be true)
																			  "AND k._loginId = :loginId " +
																			  "AND k._version = :version",
																		   _DBEntityType.getSimpleName()),
														_DBEntityType)
								  		   .setParameter("from",Instant.now())
								  		   .setParameter("loginId",loginId.asString())
								  		   .setParameter("version",apiKeyVersion.asString());
		qry.setHint(QueryHints.READ_ONLY,HintValues.TRUE);
		CRUDResult<L> outApiKeyLoadResult = _crudResultForSingleEntity(securityContext,
																	   qry.getResultList(),
																	   "loginId/version",Strings.customized("{}/{}/{}",
																			   							    loginId,apiKeyVersion));
		return outApiKeyLoadResult;
	}
	@Override
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
							  	final UserOID ownerOid,
								final AppVersion version) {
		TypedQuery<DB> qry = _entityManager.createQuery(Strings.customized("SELECT k " +
																			 "FROM {} k " +
																			"WHERE (k._validFrom <= :from AND k._validTo IS NULL) " +
																			  "AND k._active = TRUE " +		// this is redundant (if _validTo = null, active MUST be true)
																			  "AND k._userOid = :owner " +
																			  "AND k._version = :version",
																		   _DBEntityType.getSimpleName()),
														_DBEntityType)
								  		   .setParameter("from",Instant.now())
								  		   .setParameter("owner",ownerOid.asString())
								  		   .setParameter("version",version.asString());
		qry.setHint(QueryHints.READ_ONLY,HintValues.TRUE);
		CRUDResult<L> outApiKeyLoadResult = _crudResultForSingleEntity(securityContext,
																	   qry.getResultList(),
																	   "owner/version",Strings.customized("{}/{}/{}",
																			   							  ownerOid,version));
		return outApiKeyLoadResult;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  ENSURE THE PRIVATE KEY IS NEVER STORED
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CRUDResult<L> create(final SecurityContext securityContext,
								final L apiKey) {
		// BEWARE!! never store the generated private key
		L newApiKeyToBeStored = apiKey.cloneWithoutPrivateKeyInfo();
		newApiKeyToBeStored.removePrivateKeyInfo();	// ensure!

		CRUDResult<L> crudResult =  super.create(securityContext,
												 newApiKeyToBeStored);
		// Save WITHOUT PRIVATE KEY but return with it...
		if (crudResult.hasSucceeded()) {
			return CRUDResultBuilder.using(securityContext)
								    .on(_modelObjectType)
									.created()
									.entity(apiKey);
		}
		// If error
		return crudResult;
	}
	@Override
	public CRUDResult<L> update(final SecurityContext securityContext,
								final L apiKey) {
		// BEWARE!! never store the generated private key
		L apiKeyToBeStored = apiKey.cloneWithoutPrivateKeyInfo();
		apiKeyToBeStored.removePrivateKeyInfo();	// ensure

		apiKeyToBeStored.setEntityVersion(apiKey.getEntityVersion());
		CRUDResult<L> crudResult =  super.update(securityContext,
												 apiKeyToBeStored);
		// Save WITHOUT PRIVATE KEY but return with it...
		if (crudResult.hasSucceeded()) {
			return CRUDResultBuilder.using(securityContext)
										 .on(_modelObjectType)
										 .updated()
										 .entity(apiKey);
		}
		// If error
		return crudResult;
	}
	@Override
	public CRUDResult<L> invalidate(final SecurityContext securityContext,
								    final LoginOID apiKeyOid) {
		// load the [api key]
		L apiKey = this.load(securityContext,
							 apiKeyOid)
					   .getOrThrow();
		if (apiKey.getApiKeyUsageRange().getUpperBound() != null) {
			throw new IllegalStateException("The [api key] is already invalidated: it have an usable-until date: " + apiKey.getApiKeyUsageRange().getUpperBound());
		}
		// update it
		apiKey.notActiveFromNowOn();
		return this.update(securityContext,
						   apiKey);
	}
	@Override
	public CRUDResult<L> issue(final SecurityContext securityContext,
							   final UserOID ownerOid,
							   final AppVersion version,final boolean readOnly) {
		throw new IllegalStateException("Implemented at service level (see " + CRUDServicesDelegateForApiKeyLogin.class.getSimpleName() + ")");
	}
	@Override
	public CRUDResult<L> issueForLastVersion(final SecurityContext securityContext,
			                                 final UserOID ownerOid,
			                                 final boolean readOnly) {
		throw new IllegalStateException("Implemented at service level (see " + CRUDServicesDelegateForApiKeyLogin.class.getSimpleName() + ")");
	}
}
