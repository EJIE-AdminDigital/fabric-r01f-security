package r01f.client.api.sub.security.login.entry;

import jakarta.inject.Provider;
import r01f.api.interfaces.security.login.entry.CRUDServicesForLoginEntry;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.entry.LoginEntry;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityOIDs.LoginEntryOID;
import r01f.services.api.delegates.ClientAPIDelegateForModelObjectCRUDServices;

public class ClientAPIDelegateForUserLoginEntryCRUDServices
	 extends ClientAPIDelegateForModelObjectCRUDServices<LoginEntryOID,LoginEntry> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForUserLoginEntryCRUDServices(final Provider<SecurityContext>  securityContextProvider,
														  final Marshaller marshaller,
														  final CRUDServicesForLoginEntry crudServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  crudServicesProxy);
	}
}
