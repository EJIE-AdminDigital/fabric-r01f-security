package r01f.api.interfaces.security.authorization;

import r01f.api.interfaces.security.IsSecurityServiceInterface;
import r01f.model.security.authorization.AuthorizationTargetResource;
import r01f.oids.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.services.interfaces.CRUDServicesForModelObject;

public interface CRUDServicesForAuthorizationTargetResource
		 extends CRUDServicesForModelObject<AuthorizationTargetResourceOID,AuthorizationTargetResource>,
				 IsSecurityServiceInterface {
	// nothing
}

