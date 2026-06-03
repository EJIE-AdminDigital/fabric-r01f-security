package r01f.db.transfer.security;

import r01f.db.entities.security.DBEntityForAuthorizationTargetResource;
import r01f.model.security.authorization.AuthorizationTargetResource;
import r01f.securitycontext.SecurityContext;

public abstract class AuthorizationTargetResourceTransferToDBBase<R extends AuthorizationTargetResource,
														   		  DB extends DBEntityForAuthorizationTargetResource>
			  extends SecurityObjectTransferToDBBase<R,DB> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public AuthorizationTargetResourceTransferToDBBase() {
		// no-args
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
												 final R modelObj,final DB dbEntity) {
		super.setDBEntityFieldsFromModelObject(securityContext,
											   modelObj,dbEntity);
		dbEntity.setAppCode(modelObj.getAppCode() != null ? modelObj.getAppCode().asString() : null);
		dbEntity.setAppComponent(modelObj.getAppComponent() != null ? modelObj.getAppComponent().asString() : null);
		
		dbEntity.setDescription(modelObj.getName() != null ? modelObj.getName() : null);
		dbEntity.setResources(modelObj.getResources() != null ? modelObj.getResources() : null);
	}
}
