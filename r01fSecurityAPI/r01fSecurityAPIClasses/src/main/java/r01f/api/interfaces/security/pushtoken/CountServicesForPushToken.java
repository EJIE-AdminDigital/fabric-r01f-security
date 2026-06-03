package r01f.api.interfaces.security.pushtoken;

import r01f.api.interfaces.security.SecurityServiceInterface;
import r01f.model.security.pushtoken.PushToken;
import r01f.model.security.pushtoken.PushTokenOID;
import r01f.services.interfaces.CountServicesForModelObject;

public interface CountServicesForPushToken<T extends PushToken>
	     extends CountServicesForModelObject<PushTokenOID,T>,
				 SecurityServiceInterface {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
}
