package r01f.services.security;

import com.google.inject.Singleton;

import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.login.pairedphone.FindServicesForPairedPhoneLogin;
import r01f.eventbus.EventBus;
import r01f.model.security.login.pairedphone.PairedPhoneLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreFindServicesForModelObjectBase;

@Singleton
@Accessors(prefix="_")
public abstract class CoreFindServicesImplForPairedPhoneLogin<L extends PairedPhoneLogin>
	 extends CoreFindServicesForModelObjectBase<LoginOID,L>
 implements FindServicesForPairedPhoneLogin<L>,
            IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreFindServicesImplForPairedPhoneLogin(final ServicesCoreModuleConfig coreCfg,
											       final Marshaller modelObjectsMarshaller,
											       final EventBus eventBus,
											       final Provider<EntityManager> entityManagerProvider) {
		super(coreCfg,
			  modelObjectsMarshaller,
			  eventBus,
			  entityManagerProvider);
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	METHODS
/////////////////////////////////////////////////////////////////////////////////////////
}
