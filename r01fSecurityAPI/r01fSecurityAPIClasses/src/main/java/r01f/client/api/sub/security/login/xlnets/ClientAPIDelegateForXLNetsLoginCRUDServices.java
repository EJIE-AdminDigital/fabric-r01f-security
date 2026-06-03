package r01f.client.api.sub.security.login.xlnets;

import jakarta.inject.Provider;

import r01f.api.interfaces.security.login.xlnets.CRUDServicesForXLNetsLogin;
import r01f.client.api.sub.security.login.ClientAPIDelegateForLoginCRUDServicesBase;
import r01f.model.security.login.xlnets.XLNetsLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;

public class ClientAPIDelegateForXLNetsLoginCRUDServices<L extends XLNetsLogin>
	 extends ClientAPIDelegateForLoginCRUDServicesBase<L> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForXLNetsLoginCRUDServices(final Provider<SecurityContext>  securityContextProvider,
													   final Marshaller marshaller,
													   final CRUDServicesForXLNetsLogin<L> crudServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  crudServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
}
