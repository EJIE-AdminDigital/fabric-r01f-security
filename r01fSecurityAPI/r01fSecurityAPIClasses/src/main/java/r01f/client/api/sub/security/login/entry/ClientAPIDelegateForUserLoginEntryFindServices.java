package r01f.client.api.sub.security.login.entry;

import jakarta.inject.Provider;

import r01f.api.interfaces.security.login.entry.FindServicesForUserLoginEntry;
import r01f.model.security.login.entry.UserLoginEntry;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.UserLoginEntryOID;
import r01f.services.api.delegates.ClientAPIDelegateForModelObjectFindServices;

public class ClientAPIDelegateForUserLoginEntryFindServices<E extends UserLoginEntry>
	 extends ClientAPIDelegateForModelObjectFindServices<UserLoginEntryOID,E> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForUserLoginEntryFindServices(final Provider<SecurityContext>  securityContextProvider,
														  final Marshaller marshaller,
														  final FindServicesForUserLoginEntry<E> findServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  findServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////

}
