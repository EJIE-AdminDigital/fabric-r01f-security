package r01f.client.api.sub.security.login.pairedphone;

import jakarta.inject.Provider;

import r01f.api.interfaces.security.login.pairedphone.CRUDServicesForPairedPhoneLogin;
import r01f.client.api.sub.security.login.ClientAPIDelegateForLoginCRUDServicesBase;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.login.pairedphone.PairedPhoneLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.types.contact.Phone;

public class ClientAPIDelegateForUserPairedPhoneLoginCRUDServices<L extends PairedPhoneLogin>
	 extends ClientAPIDelegateForLoginCRUDServicesBase<L> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForUserPairedPhoneLoginCRUDServices(final Provider<SecurityContext>  securityContextProvider,
														 	    final Marshaller marshaller,
														 	    final CRUDServicesForPairedPhoneLogin<L> crudServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  crudServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Load By Phone
	 * @param phone
	 * @param password
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public L loadBy(final Phone phone) {
		CRUDResult<L> opResult = this.getServiceProxyAs(CRUDServicesForPairedPhoneLogin.class)
									 .loadBy(this.getSecurityContext(),
											 phone);
		L outEntity = opResult.getOrThrow();
		return outEntity;
	}
}
