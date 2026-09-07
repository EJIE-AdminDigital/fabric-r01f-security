package r01f.client.api.security.sub.pushtoken;

import jakarta.inject.Provider;
import r01f.api.interfaces.security.pushtoken.CRUDServicesForPushToken;
import r01f.guids.CommonOIDs.SystemID;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.PersistenceException;
import r01f.model.security.pushtoken.PushToken;
import r01f.model.security.pushtoken.IsPushTokenID;
import r01f.model.security.pushtoken.PushTokenOID;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.api.delegates.ClientAPIDelegateForModelObjectCRUDServices;

public class ClientAPIDelegateForPushTokenCRUDServices<T extends PushToken>
     extends ClientAPIDelegateForModelObjectCRUDServices<PushTokenOID,T> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForPushTokenCRUDServices(final Provider<SecurityContext>  securityContextProvider,
													  final Marshaller marshaller,
													  final CRUDServicesForPushToken<T> crudServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  crudServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EXTENSION METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	public T loadBy(final IsPushTokenID id,
				    final SystemID systemOid) {
		CRUDResult<T> results = this.getServiceProxyAs(CRUDServicesForPushToken.class)
												.loadBy(this.getSecurityContext(),
														id,systemOid);
		T pushToken = null;
		try {
			pushToken = results.getOrThrow();
		} catch ( final PersistenceException persex) {
			if (! persex.isEntityNotFound() ) { // Throws exception just if exception is NOt because of entity not found
				throw persex;
			}
		}
		return pushToken;
	}
}
