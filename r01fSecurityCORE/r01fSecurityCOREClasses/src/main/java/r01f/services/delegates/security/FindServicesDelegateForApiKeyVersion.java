package r01f.services.delegates.security;	


import r01f.api.interfaces.security.login.apikey.FindServicesForApiKeyVersion;
import r01f.eventbus.EventBus;
import r01f.model.security.login.apikey.ApiKeyVersion;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityOIDs.ApiKeyVersionOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.FindServicesForModelObjectDelegateBase;

/**
 * Service layer delegated type for FIND (Create/Read/Update/Delete) operations
 */
public abstract class FindServicesDelegateForApiKeyVersion
	 		  extends FindServicesForModelObjectDelegateBase<ApiKeyVersionOID,ApiKeyVersion>
  		   implements FindServicesForApiKeyVersion {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public FindServicesDelegateForApiKeyVersion(final ServicesCoreModuleConfig cfg,
			                                 	final FindServicesForApiKeyVersion dbFind,
			                                 	final Marshaller marshaller,
			                                 	final EventBus eventBus) {
		super(cfg,
			  ApiKeyVersion.class,
			  dbFind,
			  eventBus);
	}
}