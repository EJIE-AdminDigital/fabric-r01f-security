package r01f.services.security;

import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.authorization.CRUDServicesForAuthorizationTargetResource;
import r01f.eventbus.EventBus;
import r01f.model.security.authorization.AuthorizationTargetResource;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreCRUDServicesForModelObjectBase;

@Accessors(prefix="_")
public abstract class CoreCRUDServicesImplForAuthorizationTargetResource<R extends AuthorizationTargetResource>
			  extends CoreCRUDServicesForModelObjectBase<AuthorizationTargetResourceOID,R>
		   implements CRUDServicesForAuthorizationTargetResource<R>,
					  IsSecurityCoreServiceImpl {

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreCRUDServicesImplForAuthorizationTargetResource(final ServicesCoreModuleConfig coreCfg,
															  final Marshaller modelObjectsMarshaller,
															  final EventBus eventBus,
															  final Provider<EntityManager> entityManagerProvider) {
		super(coreCfg,
			  modelObjectsMarshaller,
			  eventBus,
			  entityManagerProvider);
	}
}
