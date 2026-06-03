package r01f.services.security;
import com.google.inject.Singleton;

import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.login.pairedphone.CRUDServicesForPairedPhoneLoginConsent;
import r01f.eventbus.EventBus;
import r01f.model.security.login.pairedphone.PairedPhoneLoginConsent;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityOIDs.PairedPhoneLoginConsentOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreCRUDServicesForModelObjectBase;


@Singleton
@Accessors(prefix="_")
public abstract class CoreCRUDServicesImplForPairedPhoneLoginConsent<C extends PairedPhoneLoginConsent>
	 extends CoreCRUDServicesForModelObjectBase<PairedPhoneLoginConsentOID, C>
 implements CRUDServicesForPairedPhoneLoginConsent<C>,
            IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreCRUDServicesImplForPairedPhoneLoginConsent(final ServicesCoreModuleConfig coreCfg,
											              final Marshaller modelObjectsMarshaller,
											              final EventBus eventBus,
											              final Provider<EntityManager> entityManagerProvider) {
		super(coreCfg,
			  modelObjectsMarshaller,
			  eventBus,
			  entityManagerProvider);
	}
}
