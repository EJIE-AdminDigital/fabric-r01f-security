package r01f.db.transfer.security;

import r01f.db.entities.security.DBEntityForLoginEntryBase;
import r01f.model.ModelObject;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.entry.LoginEntry;
import r01f.services.core.persistence.db.DBEntity;

/**
 * Transfers a {@link ModelObject}'s state to a given {@link DBEntity}
 */
public class LoginEntryTransferToDB<DB extends DBEntityForLoginEntryBase>
     extends SecurityObjectTransferToDBBase<LoginEntry,DB> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public LoginEntryTransferToDB() {
		// nothing
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
												 final LoginEntry modelObj,final DB dbEntity) {
		super.setDBEntityFieldsFromModelObject(securityContext, 
											   modelObj,dbEntity);
		// common fields
		dbEntity.setEvent(modelObj.getEvent());
		dbEntity.setAt(modelObj.getAt() != null ? modelObj.getAt() : null);
		
		// login id & oid
		dbEntity.setLoginOid(modelObj.getLoginOid() != null ? modelObj.getLoginOid().asString() : null);
		dbEntity.setLoginId(modelObj.getLoginId() != null ? modelObj.getLoginId().asString() : null);
		
		// principal oid
		dbEntity.setSecurityPrincipalOid(modelObj.getSecurityPrincipalOid() != null ? modelObj.getSecurityPrincipalOid().asString() : null);
	}
}