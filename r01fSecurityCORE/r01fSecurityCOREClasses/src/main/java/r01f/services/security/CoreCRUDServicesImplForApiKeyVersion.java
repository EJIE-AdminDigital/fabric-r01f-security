package r01f.services.security;



import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.login.apikey.CRUDServicesForApiKeyVersion;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.login.apikey.ApiKeyVersion;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.ApiKeyVersionOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreCRUDServicesForModelObjectBase;
import r01f.types.AppVersion;

@Accessors(prefix="_")
public abstract class CoreCRUDServicesImplForApiKeyVersion
     		  extends CoreCRUDServicesForModelObjectBase<ApiKeyVersionOID,ApiKeyVersion>
  		   implements CRUDServicesForApiKeyVersion,
  					  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreCRUDServicesImplForApiKeyVersion(final ServicesCoreModuleConfig coreCfg,
											  	final Marshaller modelObjectsMarshaller,
											  	final EventBus eventBus,
											  	final Provider<EntityManager> entityManagerProvider) {
		super(coreCfg,
			  modelObjectsMarshaller,
			  eventBus,
			  entityManagerProvider);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CRUDResult<ApiKeyVersion> loadCurrent(final SecurityContext securityContext) {
		return this.forSecurityContext(securityContext)
					.createDelegateAs(CRUDServicesForApiKeyVersion.class)
						.loadCurrent(securityContext);
	}
	@Override
	public CRUDResult<ApiKeyVersion> loadById(final SecurityContext securityContext,
										  	  final AppVersion id) {
		return this.forSecurityContext(securityContext)
					.createDelegateAs(CRUDServicesForApiKeyVersion.class)
						.loadById(securityContext,
								  id);
	}
}

