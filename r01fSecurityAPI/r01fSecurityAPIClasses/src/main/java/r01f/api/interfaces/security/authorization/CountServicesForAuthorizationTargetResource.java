package r01f.api.interfaces.security.authorization;

import r01f.api.interfaces.security.SecurityServiceInterface;
import r01f.model.security.authorization.AuthorizationTargetResource;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.services.interfaces.CountServicesForModelObject;

public interface CountServicesForAuthorizationTargetResource<R extends AuthorizationTargetResource> 
	     extends CountServicesForModelObject<AuthorizationTargetResourceOID,R>,
				 SecurityServiceInterface {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
}
