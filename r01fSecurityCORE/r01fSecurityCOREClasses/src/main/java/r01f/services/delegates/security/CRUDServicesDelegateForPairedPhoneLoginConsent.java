package r01f.services.delegates.security;



import r01f.api.interfaces.security.login.pairedphone.CRUDServicesForPairedPhoneLoginConsent;
import r01f.eventbus.EventBus;
import r01f.model.security.login.pairedphone.PairedPhoneLoginConsent;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityOIDs.PairedPhoneLoginConsentOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.CRUDServicesForModelObjectDelegateBase;


public abstract class CRUDServicesDelegateForPairedPhoneLoginConsent<C extends PairedPhoneLoginConsent>
	 		  extends CRUDServicesForModelObjectDelegateBase<PairedPhoneLoginConsentOID, C>
  		   implements CRUDServicesForPairedPhoneLoginConsent<C> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR   & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public CRUDServicesDelegateForPairedPhoneLoginConsent(final ServicesCoreModuleConfig coreCfg,
			                                              final Class<C> modelObjectType,
											              final CRUDServicesForPairedPhoneLoginConsent<C> dbCrud,
											              final Marshaller marshaller,
											              final EventBus eventBus) {
		super(coreCfg,
			  modelObjectType,
			  dbCrud,			
			  eventBus);
	}
}
