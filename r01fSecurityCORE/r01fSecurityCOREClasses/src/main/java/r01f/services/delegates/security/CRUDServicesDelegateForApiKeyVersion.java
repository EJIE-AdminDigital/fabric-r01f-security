package r01f.services.delegates.security;

import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.apikey.CRUDServicesForApiKeyVersion;
import r01f.eventbus.EventBus;
import r01f.internal.services.security.ApiKeyLoginConfig;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.PersistenceRequestedOperation;
import r01f.model.security.login.apikey.ApiKeyVersion;
import r01f.model.security.login.apikey.ApiKeyVersionValidator;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.ApiKeyVersionOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.CRUDServicesForModelObjectDelegateBase;
import r01f.services.core.delegates.persistence.ValidatesModelObjectBeforeCreateOrUpdate;
import r01f.services.ids.ServiceIDs.CoreSubModule;
import r01f.types.AppVersion;
import r01f.validation.ObjectValidationResult;

/**
 * Service layer delegated type for CRUD (Create/Read/Update/Delete) operations
 */
@Slf4j
public abstract class CRUDServicesDelegateForApiKeyVersion
	 		  extends CRUDServicesForModelObjectDelegateBase<ApiKeyVersionOID,ApiKeyVersion>
  		   implements ValidatesModelObjectBeforeCreateOrUpdate<ApiKeyVersion>,
  			 		  CRUDServicesForApiKeyVersion {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public CRUDServicesDelegateForApiKeyVersion(final ServicesCoreModuleConfig cfg,
												final CRUDServicesForApiKeyVersion dbCrud,
			                                 	final Marshaller marshaller,
			                                 	final EventBus eventBus) {
		super(cfg,
			  ApiKeyVersion.class,
			  dbCrud,
			  eventBus);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CRUDResult<ApiKeyVersion> loadById(final SecurityContext securityContext,
										  	  final AppVersion version) {
		if (version == null) throw new IllegalArgumentException("The versionId cannot be null");
		return this.getServiceImplAs(CRUDServicesForApiKeyVersion.class)
				   .loadById(securityContext,
						     version);
	}
	@Override
	public CRUDResult<ApiKeyVersion> loadCurrent(final SecurityContext securityContext) {
		// The CURRENT [apiKey version id] is stored on a properties file
		ApiKeyLoginConfig apiKeyCfg = _coreConfig.getSubModuleConfigFor(CoreSubModule.BUSINESS);

		CRUDResult<ApiKeyVersion> currentVersionCRUD = null;

		if (apiKeyCfg.getApiKeyLastVersion() == null) {
			log.warn("Current API KEY Version must be stored on {}.{} properties file.",
					 _coreConfig.getCoreAppCode(),_coreConfig.getCoreModule());
		} else {
			// ... load the configured [apiKey version] from the DB
			AppVersion propsStoredVersionId = apiKeyCfg.getApiKeyLastVersion();
			currentVersionCRUD =  this.loadById(securityContext,
												propsStoredVersionId);
			if (currentVersionCRUD.hasFailed()) {
				log.warn("API KEY Version {} stored at {}.{} properties file couldn't be loaded from DB: {}.",
						 apiKeyCfg.getApiKeyLastVersion(),
						 _coreConfig.getCoreAppCode(),_coreConfig.getCoreModule(),
						 currentVersionCRUD.asCRUDError()
										   .getDetailedMessage());
				currentVersionCRUD = null;
			}
		}
		// If no [api key version] was fount load the CURRENT [api key version] from the DB
		if (currentVersionCRUD == null) {
			currentVersionCRUD = this.getServiceImplAs(CRUDServicesForApiKeyVersion.class)
									 .loadCurrent(securityContext);
		}
		return currentVersionCRUD;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  PARAMS VALIDATION ON CREATION / UPDATE
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public ObjectValidationResult<ApiKeyVersion> validateModelObjBeforeCreateOrUpdate(final SecurityContext securityContext,
																	 	  		   	  final PersistenceRequestedOperation requestedOp,
																	 	  		   	  final ApiKeyVersion version) {
		// [1]: Do base validations
		ObjectValidationResult<ApiKeyVersion> outValid = ApiKeyVersionValidator.createApiKeyVersionValidator()
																				.validate(version);

		// [2]: Return
		return outValid;
	}
}