package r01f.api.interfaces.security.pushtoken;

import r01f.api.interfaces.security.IsSecurityServiceInterface;
import r01f.model.security.pushtoken.IsPushToken;
import r01f.model.security.pushtoken.PushTokenOID;
import r01f.services.interfaces.CountServicesForModelObject;

public interface CountServicesForPushToken<T extends IsPushToken>
	     extends CountServicesForModelObject<PushTokenOID,T>,
				 IsSecurityServiceInterface {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
}
