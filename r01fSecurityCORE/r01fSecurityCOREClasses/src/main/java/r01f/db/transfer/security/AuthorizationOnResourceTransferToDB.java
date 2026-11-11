package r01f.db.transfer.security;

import r01f.db.entities.security.DBEntityForAuthorizationOnResourceBase;
import r01f.model.security.authorization.SecurityPrincipalAuthorizationOnResource;
import r01f.model.security.context.SecurityContext;

public class AuthorizationOnResourceTransferToDB<DB extends DBEntityForAuthorizationOnResourceBase>
	 extends SecurityObjectTransferToDBBase<SecurityPrincipalAuthorizationOnResource,DB > {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public AuthorizationOnResourceTransferToDB() {
		// nothing
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
												 final SecurityPrincipalAuthorizationOnResource modelObj,final DB dbEntity) {
		super.setDBEntityFieldsFromModelObject(securityContext, 
											   modelObj,dbEntity);
		dbEntity.setSecurityPrincipalOid(modelObj.getSecurityPrincipalOid() != null ? modelObj.getSecurityPrincipalOid().asString() : null);
		
		dbEntity.setResourceOid(modelObj.getAuthorizationTargetResourceOid() != null ? modelObj.getAuthorizationTargetResourceOid().asString() : null);
		dbEntity.setRole(modelObj.getRole() != null ? modelObj.getRole().asString() : null);
	}
}
