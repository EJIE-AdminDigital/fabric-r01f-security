package r01f.db.transfer.security;

import r01f.db.entities.security.DBEntityForAuthorizationTargetResourceBase;
import r01f.model.security.authorization.AuthorizationTargetResource;
import r01f.model.security.context.SecurityContext;

public class AuthorizationTargetResourceTransferToDB<DB extends DBEntityForAuthorizationTargetResourceBase>
	 extends SecurityObjectTransferToDBBase<AuthorizationTargetResource,DB> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public AuthorizationTargetResourceTransferToDB() {
		// nothing
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
												 final AuthorizationTargetResource modelObj,final DB dbEntity) {
		super.setDBEntityFieldsFromModelObject(securityContext,
											   modelObj,dbEntity);
		dbEntity.setAppCode(modelObj.getAppCode() != null ? modelObj.getAppCode().asString() : null);
		dbEntity.setAppComponent(modelObj.getAppComponent() != null ? modelObj.getAppComponent().asString() : null);
		
		dbEntity.setDescription(modelObj.getName() != null ? modelObj.getName() : null);
		dbEntity.setResources(modelObj.getResources() != null ? modelObj.getResources()
																		.getSerializedData() 
															  : null);
	}
}
