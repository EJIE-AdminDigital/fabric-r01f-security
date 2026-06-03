package r01f.api.interfaces.security.pushtoken;

import r01f.api.interfaces.security.SecurityServiceInterface;
import r01f.guids.CommonOIDs.SystemID;
import r01f.model.persistence.FindResult;
import r01f.model.security.pushtoken.PushToken;
import r01f.model.security.pushtoken.PushTokenOID;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.SecurityToken;
import r01f.services.interfaces.FindServicesForModelObject;
import r01f.types.contact.Phone;

public interface FindServicesForPushToken<T extends PushToken>
		 extends FindServicesForModelObject<PushTokenOID,T>,
		 		 SecurityServiceInterface {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public FindResult<T> findByPhone(final SecurityContext securityContext,
			 						 final Phone phoneNumber);
	
	public FindResult<T> findByPhone(final SecurityContext securityContext,
									 final SystemID systemId,
									 final Phone phoneNumber);

	public FindResult<T> findByPhoneNumberAndToken(final SecurityContext securityContext,
												   final SystemID systemId,
												   final Phone phoneNumber,
												   final SecurityToken token);

	public FindResult<T> findByToken(final SecurityContext securityContext,
									 final SystemID systemId,
									 final SecurityToken token);
}
