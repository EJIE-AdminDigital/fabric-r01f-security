package r01f.model.security.login.pairedphone;

import r01f.model.security.PersistableSecurityObject;
import r01f.securitycontext.SecurityOIDs.PairedPhoneLoginConsentOID;

/**
 * Paired Phone Login Consent
 */
public interface PairedPhoneLoginConsent
		 extends PersistableSecurityObject<PairedPhoneLoginConsentOID> {	
/////////////////////////////////////////////////////////////////////////////////////////
// METHODS TO IMPLEMENT	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	public PairedPhoneLoginConsentRequest  getRequest();
	
	public PairedPhoneLoginConsentStatus getStatus();

	public PairedPhoneLoginConsentResponse getResponse();	
	
}
