package r01f.services.delegates.security;


import r01f.api.interfaces.security.authorization.CountServicesForAuthorizationTargetResource;
import r01f.eventbus.EventBus;
import r01f.model.security.authorization.AuthorizationTargetResource;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.CountServicesForModelObjectDelegateBase;

public abstract class CountServicesDelegateForAuthorizationTargetResourceBase
			  extends CountServicesForModelObjectDelegateBase<AuthorizationTargetResourceOID,AuthorizationTargetResource>
		   implements CountServicesForAuthorizationTargetResource {

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public CountServicesDelegateForAuthorizationTargetResourceBase(final ServicesCoreModuleConfig coreCfg,
																   final CountServicesForAuthorizationTargetResource dbCount,
																   final Marshaller marshaller,
																   final EventBus eventBus) {
		super(coreCfg,
			  AuthorizationTargetResource.class,
			  dbCount,
			  eventBus);
	}
}
