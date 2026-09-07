package r01f.api.interfaces.security.pushtoken;

import r01f.api.interfaces.security.SecurityServiceInterface;
import r01f.guids.CommonOIDs.SystemID;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.pushtoken.PushToken;
import r01f.model.security.pushtoken.IsPushTokenID;
import r01f.model.security.pushtoken.PushTokenOID;
import r01f.securitycontext.SecurityContext;
import r01f.services.interfaces.CRUDServicesForModelObject;
import r01f.types.contact.Phone;

public interface CRUDServicesForPushToken<T extends PushToken>
		 extends CRUDServicesForModelObject<PushTokenOID,T>, 
				 SecurityServiceInterface {

/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public CRUDResult<T> loadBy(final SecurityContext securityContext,
								final IsPushTokenID phone,final SystemID systemOid);
}
