package r01f.client.api.sub.security.login.entry;

import jakarta.inject.Provider;
import r01f.api.interfaces.security.login.entry.FindServicesForLoginEntry;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.entry.LoginEntry;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityOIDs.LoginEntryOID;
import r01f.services.api.delegates.ClientAPIDelegateForModelObjectFindServices;

public class ClientAPIDelegateForUserLoginEntryFindServices
	 extends ClientAPIDelegateForModelObjectFindServices<LoginEntryOID,LoginEntry> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForUserLoginEntryFindServices(final Provider<SecurityContext>  securityContextProvider,
														  final Marshaller marshaller,
														  final FindServicesForLoginEntry findServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  findServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////

}
