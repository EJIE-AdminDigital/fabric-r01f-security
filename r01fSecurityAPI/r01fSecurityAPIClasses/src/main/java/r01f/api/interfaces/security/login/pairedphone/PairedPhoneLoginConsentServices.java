package r01f.api.interfaces.security.login.pairedphone;

import r01f.model.security.login.pairedphone.PairedPhoneLoginConsent;
import r01f.model.security.login.pairedphone.PairedPhoneLoginConsentRequest;
import r01f.model.security.login.pairedphone.PairedPhoneLoginConsentResponse;
import r01f.model.security.operation.SecurityOperationResult;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.PairedPhoneLoginConsentOID;
import r01f.services.interfaces.ExposedServiceInterface;
import r01f.services.interfaces.ServiceInterfaceForModelObject;

@ExposedServiceInterface
public interface PairedPhoneLoginConsentServices<C extends PairedPhoneLoginConsent>
		 extends ServiceInterfaceForModelObject<PairedPhoneLoginConsentOID,C> {
/////////////////////////////////////////////////////////////////////////////////////////
//	       1. request :
//	 			  1.1 create consent
//	              1.2 push message
//         2. response ( FROM Paired Phone )
//				  2.1 update consent
/////////////////////////////////////////////////////////////////////////////////////////
	public SecurityOperationResult<C> request(final SecurityContext securityContext,
										      final PairedPhoneLoginConsentRequest req);

	public SecurityOperationResult<C>  response(final SecurityContext securityContext,
		                                        final PairedPhoneLoginConsentResponse response);

	public SecurityOperationResult<C>  loadConsent(final SecurityContext securityContext,
                                                   final PairedPhoneLoginConsentOID oid);


}
