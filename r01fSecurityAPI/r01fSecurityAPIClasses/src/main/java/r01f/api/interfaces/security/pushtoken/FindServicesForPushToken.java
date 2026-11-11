package r01f.api.interfaces.security.pushtoken;

import r01f.api.interfaces.security.IsSecurityServiceInterface;
import r01f.model.persistence.FindResult;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.pushtoken.IsPushToken;
import r01f.model.security.pushtoken.PushTokenOID;
import r01f.oids.CommonOIDs.SystemID;
import r01f.oids.SecurityIDs.SecurityToken;
import r01f.services.interfaces.FindServicesForModelObject;
import r01f.types.contact.Phone;

public interface FindServicesForPushToken<T extends IsPushToken>
		 extends FindServicesForModelObject<PushTokenOID,T>,
		 		 IsSecurityServiceInterface {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public FindResult<T> findByPhone(final SecurityContext securityContext,
			 						 final Phone phoneNumber);
	
	public FindResult<T> findByPhone(final SecurityContext securityContext,
									 final SystemID systemId,final Phone phoneNumber);

	public FindResult<T> findByPhoneNumberAndToken(final SecurityContext securityContext,
												   final SystemID systemId,final Phone phoneNumber,
												   final SecurityToken token);

	public FindResult<T> findByToken(final SecurityContext securityContext,
									 final SystemID systemId,
									 final SecurityToken token);
}
