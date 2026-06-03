package r01f.api.interfaces.security.authorization;

import r01f.api.interfaces.security.SecurityServiceInterface;
import r01f.model.security.authorization.AuthorizationTargetResource;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.services.interfaces.CRUDServicesForModelObject;

public interface CRUDServicesForAuthorizationTargetResource<R extends AuthorizationTargetResource>
		 extends CRUDServicesForModelObject<AuthorizationTargetResourceOID,R>,
				 SecurityServiceInterface {
	// nothing
}

