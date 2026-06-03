package r01f.db.transfer.security;

import r01f.db.entities.security.DBEntityForUserBase;
import r01f.model.security.user.User;
import r01f.securitycontext.SecurityContext;
import r01f.util.types.collections.CollectionUtils;

public abstract class UserTransferToDBBase<U extends User,
										   DB extends DBEntityForUserBase>
			  extends SecurityObjectTransferToDBBase<U,DB> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public UserTransferToDBBase() {
		// nothing
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
												 final U modelObj,final DB dbEntity) {
		super.setDBEntityFieldsFromModelObject(securityContext,
											   modelObj,dbEntity);
		dbEntity.setStatus(modelObj.getStatus() != null ? modelObj.getStatus()
														: null);
		
		dbEntity.setHasSystemWideRole(CollectionUtils.hasData(modelObj.getUserSystemWideRoles()));	// has any system-wide role?
	}
}
