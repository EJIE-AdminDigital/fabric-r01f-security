package r01f.db.transfer.security;

import r01f.db.entities.security.DBEntityForUserAuthorizationOnResource;
import r01f.model.security.authorization.UserAuthorizationOnResource;
import r01f.securitycontext.SecurityContext;

public abstract class UserAuthorizationOnResourceTransferToDBBase<A extends UserAuthorizationOnResource,
																  DB extends DBEntityForUserAuthorizationOnResource>
			  extends SecurityObjectTransferToDBBase<A,DB > {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public UserAuthorizationOnResourceTransferToDBBase() {
		// nothing
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
												 final A modelObj,final DB dbEntity) {
		super.setDBEntityFieldsFromModelObject(securityContext, 
											   modelObj,dbEntity);
		dbEntity.setUserOid(modelObj.getUserOid() != null ? modelObj.getUserOid().asString() : null);
		dbEntity.setResourceOid(modelObj.getAuthorizationTargetResourceOid() != null ? modelObj.getAuthorizationTargetResourceOid().asString() : null);
		dbEntity.setRole(modelObj.getRole() != null ? modelObj.getRole().asString() : null);
		//dbEntity.setSystemId(modelObj.getSystemId().asString());
	}
}
