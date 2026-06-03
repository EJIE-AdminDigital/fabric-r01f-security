package r01f.api.interfaces.security.login.pairedphone;

import r01f.model.security.login.pairedphone.PairedPhoneLoginConsent;
import r01f.securitycontext.SecurityOIDs.PairedPhoneLoginConsentOID;
import r01f.services.interfaces.CRUDServicesForModelObject;
import r01f.services.interfaces.ExposedServiceInterface;

@ExposedServiceInterface
public interface CRUDServicesForPairedPhoneLoginConsent<C extends PairedPhoneLoginConsent>
		 extends CRUDServicesForModelObject<PairedPhoneLoginConsentOID,C> {
	// Interface
}