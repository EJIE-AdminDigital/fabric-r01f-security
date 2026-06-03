package r01f.services.security;

import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.login.apikey.FindServicesForApiKeyVersion;
import r01f.eventbus.EventBus;
import r01f.model.security.login.apikey.ApiKeyVersion;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityOIDs.ApiKeyVersionOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreFindServicesForModelObjectBase;

@Accessors(prefix="_")
public abstract class CoreFindServicesImplForApiKeyVersion
     		  extends CoreFindServicesForModelObjectBase<ApiKeyVersionOID,ApiKeyVersion>
  		   implements FindServicesForApiKeyVersion,
  					  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreFindServicesImplForApiKeyVersion(final ServicesCoreModuleConfig coreCfg,
											  	final Marshaller modelObjectsMarshaller,
											  	final EventBus eventBus,
											  	final Provider<EntityManager> entityManagerProvider) {
		super(coreCfg,
			  modelObjectsMarshaller,
			  eventBus,
			  entityManagerProvider);
	}
}

