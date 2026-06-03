package r01f.client.api.security.sub.pushtoken;

import jakarta.inject.Provider;
import r01f.api.interfaces.security.pushtoken.FindServicesForPushToken;
import r01f.guids.CommonOIDs.SystemID;
import r01f.model.persistence.FindResult;
import r01f.model.security.pushtoken.PushToken;
import r01f.model.security.pushtoken.PushTokenOID;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.SecurityToken;
import r01f.services.api.delegates.ClientAPIDelegateForModelObjectFindServices;
import r01f.types.contact.Phone;

public class ClientAPIDelegateForPushTokenFindServices<T extends PushToken>
     extends ClientAPIDelegateForModelObjectFindServices<PushTokenOID,T> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForPushTokenFindServices(final Provider<SecurityContext> securityContextProvider,
													  final Marshaller modelObjectsMarshaller,
													  final FindServicesForPushToken<T> findServicesProxy) {
		super(securityContextProvider,
			  modelObjectsMarshaller,
			  findServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	public FindResult<T> findByPhone(final Phone phoneNumber) {
		return this.getServiceProxyAs(FindServicesForPushToken.class)
					.findByPhone(this.getSecurityContext(),
								 phoneNumber);
	}
	@SuppressWarnings("unchecked")
	public FindResult<T> findByPhone(final SystemID systemId,
									 final Phone phoneNumber) {
		return this.getServiceProxyAs(FindServicesForPushToken.class)
					.findByPhone(this.getSecurityContext(),
								 systemId, phoneNumber);
	}
	@SuppressWarnings("unchecked")
	public FindResult<T> findByPhoneNumberAndToken(final SystemID systemId,
			   final Phone phoneNumber,
			   final SecurityToken token) {
		return this.getServiceProxyAs(FindServicesForPushToken.class)
				   .findByPhoneNumberAndToken(this.getSecurityContext(),
						   					  systemId,
						   					  phoneNumber,token);
	}
	@SuppressWarnings("unchecked")
	public FindResult<T> findByToken(final SystemID systemId,
									 final SecurityToken token) {
		return this.getServiceProxyAs(FindServicesForPushToken.class)
					.findByToken(this.getSecurityContext(),
								 systemId,
								 token);
	}
}
