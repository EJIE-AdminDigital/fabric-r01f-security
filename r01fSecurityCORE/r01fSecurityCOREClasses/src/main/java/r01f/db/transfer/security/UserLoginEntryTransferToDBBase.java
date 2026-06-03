package r01f.db.transfer.security;

import r01f.db.entities.security.DBEntityForUserLoginEntry;
import r01f.model.ModelObject;
import r01f.model.security.login.entry.UserLoginEntry;
import r01f.securitycontext.SecurityContext;
import r01f.services.core.persistence.db.DBEntity;

/**
 * Transfers a {@link ModelObject}'s state to a given {@link DBEntity}
 */
public abstract class UserLoginEntryTransferToDBBase<E extends UserLoginEntry,
													 DB extends DBEntityForUserLoginEntry>
			  extends SecurityObjectTransferToDBBase<E,DB> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public UserLoginEntryTransferToDBBase() {
		// nothing
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
												 final E modelObj,final DB dbEntity) {
		super.setDBEntityFieldsFromModelObject(securityContext, 
											   modelObj,dbEntity);
		// common fields
		dbEntity.setEntryDate(modelObj.getDate() != null ? modelObj.getDate() : null);
		dbEntity.setEntryType(modelObj.getId() != null ? modelObj.getId().toString() : null);
		
		dbEntity.setSecurityProvider(modelObj.getSecurityProviderId() != null ? modelObj.getSecurityProviderId().asString() : null);
		dbEntity.setLoginId(modelObj.getLoginId() != null ? modelObj.getLoginId().asString() : null);
		
		// user
		dbEntity.setUserOid(modelObj.getUserOid() != null ? modelObj.getUserOid().asString() : null);
		
		// security token
		dbEntity.setToken(modelObj.getToken() != null ? modelObj.getToken().asString() : null);
		
		// impersonation
		dbEntity.setImpersonatedUserOid(modelObj.getImpersonatedUserOid() != null ? modelObj.getImpersonatedUserOid().asString() : null);
	}
}