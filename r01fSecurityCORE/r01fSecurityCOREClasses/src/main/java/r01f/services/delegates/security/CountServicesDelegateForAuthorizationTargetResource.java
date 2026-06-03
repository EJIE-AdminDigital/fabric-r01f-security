package r01f.services.delegates.security;


import r01f.api.interfaces.security.authorization.CountServicesForAuthorizationTargetResource;
import r01f.eventbus.EventBus;
import r01f.model.security.authorization.AuthorizationTargetResource;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.CountServicesForModelObjectDelegateBase;

public abstract class CountServicesDelegateForAuthorizationTargetResource<R extends AuthorizationTargetResource>
			  extends CountServicesForModelObjectDelegateBase<AuthorizationTargetResourceOID,R>
		   implements CountServicesForAuthorizationTargetResource<R> {

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////

	public CountServicesDelegateForAuthorizationTargetResource(final ServicesCoreModuleConfig coreCfg,
															   final Class<R> modelObjectType,
															   final CountServicesForAuthorizationTargetResource<R> dbCount,
															   final Marshaller marshaller,
															   final EventBus eventBus) {
		super(coreCfg,
			  modelObjectType,
			  dbCount,
			  eventBus);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////

}
