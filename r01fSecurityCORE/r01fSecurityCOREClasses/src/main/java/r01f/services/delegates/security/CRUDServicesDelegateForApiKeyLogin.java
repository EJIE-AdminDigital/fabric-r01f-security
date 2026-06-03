package r01f.services.delegates.security;

import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.apikey.CRUDServicesForApiKeyLogin;
import r01f.eventbus.EventBus;
import r01f.internal.services.security.ApiKeyLoginConfig;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.CRUDResultBuilder;
import r01f.model.persistence.PersistenceRequestedOperation;
import r01f.model.security.login.apikey.ApiKeyLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityToken;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.CRUDServicesForModelObjectDelegateBase;
import r01f.services.ids.ServiceIDs.CoreSubModule;
import r01f.types.AppVersion;

@Slf4j
public abstract class CRUDServicesDelegateForApiKeyLogin<L extends ApiKeyLogin>
			  extends CRUDServicesForModelObjectDelegateBase<LoginOID,L>
		   implements CRUDServicesForApiKeyLogin<L> {

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR   & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public CRUDServicesDelegateForApiKeyLogin(final ServicesCoreModuleConfig coreCfg,
											  final Class<L> modelObjectType,
											  final CRUDServicesForApiKeyLogin<L> dbCrud,
											  final Marshaller marshaller,
											  final EventBus eventBus) {
		super(coreCfg,
			  modelObjectType,
			  dbCrud,
			  eventBus);
	}
///////////////////////////////////////////////////////////////////////////////////////
// 	EXTENSION
///////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
								final UserOID userOid) {
		if (userOid == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientBadRequest("The userOid MUST not be null")
									.build();
		}
		CRUDResult<L> outResult = this.getServiceImplAs(CRUDServicesForApiKeyLogin.class)
												.loadBy(securityContext,
														userOid);
		return outResult;
	}
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
								final LoginID login) {
		if (login == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientBadRequest("The login MUST not be null")
									.build();
		}
		CRUDResult<L> outResult = this.getServiceImplAs(CRUDServicesForApiKeyLogin.class)
												.loadBy(securityContext,
														login);
		return outResult;
	}
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<L> loadByPublicKey(final SecurityContext securityContext,
										 final SecurityToken publicKey) {
		if (publicKey == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientBadRequest("The publicKey MUST not be null")
									.build();
		}
		CRUDResult<L> outResult = this.getServiceImplAs(CRUDServicesForApiKeyLogin.class)
												.loadByPublicKey(securityContext,
																 publicKey);
		return outResult;
	}
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
							  	final LoginID loginId,final AppVersion version) {
		if (loginId == null || version == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientBadRequest("The loginId and app version MUST not be null")
									.build();
		}
		CRUDResult<L> outResult = this.getServiceImplAs(CRUDServicesForApiKeyLogin.class)
												.loadBy(securityContext,
														loginId,version);
		return outResult;
	}
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
							    final UserOID ownerOid,final AppVersion version) {
		if (ownerOid == null || version == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientBadRequest("The userOid & version MUST not be null")
									.build();
		}
		CRUDResult<L> outResult = this.getServiceImplAs(CRUDServicesForApiKeyLogin.class)
												.loadBy(securityContext,
														ownerOid,version);
		return outResult;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Creates a new [api key login] object
	 * @param ownerOid
	 * @param version
	 * @param readOnly
	 * @return
	 */
	protected abstract L _createNewApiKeyLoginFor(final UserOID ownerOid,
							   				   	  final AppVersion version,final boolean readOnly);
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CRUDResult<L> issue(final SecurityContext securityContext,
							   final UserOID ownerOid,
							   final AppVersion version,final boolean readOnly) {
		log.info("...issue for user {}", ownerOid );
		///////////////////////////////
		// [0] - param checking
		if (ownerOid == null
		 || version == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notCreated()
									.becauseClientBadRequest("The userOid & version MUST not be null")
									.build();
		}
		///////////////////////////////
		// [1] - if there exists another currently usable api-key, invalidate it
		CRUDResult<L> currentKeyLoadResult = this.loadBy(securityContext,
														 ownerOid,
														 version);
		if (currentKeyLoadResult.hasSucceeded()) {
			log.info("There exists another usable [api key] for owner/version={}/{}; it'll be invalidated before issuing a new one",
					ownerOid,version);
			L currentKey = currentKeyLoadResult.getOrThrow();
			currentKey.notActiveFromNowOn();

			CRUDResult<L> currentKeyUpdateResult = this.update(securityContext,
															   currentKey);
			if (currentKeyUpdateResult.hasFailed()) return CRUDResultBuilder.using(securityContext)
																			.on(_modelObjectType)
																			.notCreated()
																			.because(currentKeyUpdateResult.asCRUDError())
																			.build();
		}
		///////////////////////////////
		// [2] - Now create the key >> BEWARE!! this is where the [public & private keys] are created!!!!!
		L newApiKey = _createNewApiKeyLoginFor(ownerOid,
											   version,readOnly);
		return this.create(securityContext,
						   newApiKey);
	}
	@Override
	public CRUDResult<L> issueForLastVersion(final SecurityContext securityContext,
											 final UserOID ownerOid,
											 final boolean readOnly) {
		log.info("...issue last version for user {}", ownerOid );
		if (ownerOid == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notCreated()
									.becauseClientBadRequest("The userOid MUST not be null")
									.build();
		}
		ApiKeyLoginConfig apiKeyConfig = this.getCoreConfig()
											 .getSubModuleConfigFor(CoreSubModule.BUSINESS);
		AppVersion AppVersion = apiKeyConfig.getApiKeyLastVersion();

		log.info("The [api key] last version is {}",
				 AppVersion );
		if (AppVersion == null) {
			 log.warn("The [api key] config is NOT valid: {}",
					  apiKeyConfig.debugInfo());
			 throw new IllegalArgumentException("Could NOT find the last [api key] version in config!");
		}
		return this.issue(securityContext,
						  ownerOid,
						  AppVersion,readOnly);
	}
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<L> invalidate(final SecurityContext securityContext,
									final LoginOID apiKeyOid) {
		if (apiKeyOid == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.not(PersistenceRequestedOperation.named("invalidate"))
									.becauseClientBadRequest("The apiKeyOid MUST not be null")
									.build();
		}
		CRUDResult<L> outResult = this.getServiceImplAs(CRUDServicesForApiKeyLogin.class)
									  .invalidate(securityContext,
											  	  apiKeyOid);
		return outResult;
	}
}
