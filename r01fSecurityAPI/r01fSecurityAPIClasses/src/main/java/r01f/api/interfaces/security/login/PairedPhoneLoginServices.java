package r01f.api.interfaces.security.login;

import r01f.api.interfaces.security.IsSecurityServiceInterface;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.pairedphone.messages.login.PairedPhoneLoginConsentRequest;
import r01f.model.security.login.pairedphone.messages.login.PairedPhoneLoginConsentResponseFromPairedPhone;
import r01f.model.security.login.pairedphone.messages.login.PairedPhoneLoginResponse;
import r01f.model.security.operation.IsSecurityOperationResult;
import r01f.types.contact.Phone;

/**
 * Steps:
 * 1. Create a [consent request] (FROM desktop application)
 *		1.1 create [consent request] > store it at the Login table
 *      1.2 push message to the paired phone
 * 2. response (FROM Paired Phone)
 *      2.2 check the [consent request] against the [consent response] > if ok, mark the [consent request] as accepted
 *		2.1 update [consent request]
 */
public interface PairedPhoneLoginServices
		 extends IsSecurityServiceInterface {
/////////////////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////////////////////////
	public IsSecurityOperationResult<PairedPhoneLoginConsentRequest> loadConsentRequest(final SecurityContext securityContext,
                                                   										final Phone phone);
	public IsSecurityOperationResult<PairedPhoneLoginConsentRequest> createConsentRequestFor(final SecurityContext securityContext,
										        			 							  	 final Phone phone);
	public IsSecurityOperationResult<PairedPhoneLoginResponse> receiveConsentResponseFromPairedPhone(final SecurityContext securityContext,
		                                          													 final PairedPhoneLoginConsentResponseFromPairedPhone response);

}
