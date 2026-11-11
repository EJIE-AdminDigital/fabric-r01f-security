package r01f.api.interfaces.security.authorization;

import r01f.api.interfaces.security.IsSecurityServiceInterface;
import r01f.model.security.authorization.AuthorizationTargetResource;
import r01f.oids.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.services.interfaces.CountServicesForModelObject;

public interface CountServicesForAuthorizationTargetResource 
	     extends CountServicesForModelObject<AuthorizationTargetResourceOID,AuthorizationTargetResource>,
				 IsSecurityServiceInterface {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
}
