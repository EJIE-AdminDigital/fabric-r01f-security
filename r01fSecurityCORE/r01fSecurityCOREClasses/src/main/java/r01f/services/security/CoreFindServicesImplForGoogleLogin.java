package r01f.services.security;

import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.login.google.FindServicesForGoogleLogin;
import r01f.eventbus.EventBus;
import r01f.model.security.login.google.GoogleLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreFindServicesForModelObjectBase;

@Accessors(prefix="_")
public abstract class CoreFindServicesImplForGoogleLogin<L extends GoogleLogin>
			  extends CoreFindServicesForModelObjectBase<LoginOID,L>
		   implements FindServicesForGoogleLogin<L>,
		   			  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreFindServicesImplForGoogleLogin(final ServicesCoreModuleConfig coreCfg,
										      final Marshaller modelObjectsMarshaller,
											  final EventBus eventBus,
											  final Provider<EntityManager> entityManagerProvider) {
		super(coreCfg,
			  modelObjectsMarshaller,
			  eventBus,
			  entityManagerProvider);
	}
}

