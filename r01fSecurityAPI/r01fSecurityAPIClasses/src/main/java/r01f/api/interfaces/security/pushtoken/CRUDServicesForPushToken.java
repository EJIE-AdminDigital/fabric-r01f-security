package r01f.api.interfaces.security.pushtoken;

import r01f.api.interfaces.security.IsSecurityServiceInterface;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.pushtoken.IsPushDestinationID;
import r01f.model.security.pushtoken.IsPushToken;
import r01f.model.security.pushtoken.PushTokenOID;
import r01f.oids.CommonOIDs.SystemID;
import r01f.services.interfaces.CRUDServicesForModelObject;

public interface CRUDServicesForPushToken<T extends IsPushToken>
		 extends CRUDServicesForModelObject<PushTokenOID,T>, 
				 IsSecurityServiceInterface {

/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public CRUDResult<T> loadBy(final SecurityContext securityContext,
								final SystemID srcSystemId,final IsPushDestinationID destId);
}
