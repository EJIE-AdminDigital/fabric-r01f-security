package r01f.db.transfer.security;

import r01f.db.entities.security.DBEntityForUserPasswordLogin;
import r01f.model.ModelObject;
import r01f.model.security.login.userpassword.UserPasswordLogin;
import r01f.securitycontext.SecurityContext;
import r01f.services.core.persistence.db.DBEntity;

/**
 * Transfers a {@link ModelObject}'s state to a given {@link DBEntity}
 */
public abstract class UserPasswordLoginTransferToDBBase<L extends UserPasswordLogin,
													    DB extends DBEntityForUserPasswordLogin>
			  extends SecurityObjectTransferToDBBase<L,DB> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public UserPasswordLoginTransferToDBBase() {
		// nothing
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
												 final L modelObj,final DB dbEntity) {
		super.setDBEntityFieldsFromModelObject(securityContext,
											   modelObj,dbEntity);
		
		dbEntity.setLoginId(modelObj.getLoginId() != null ? modelObj.getLoginId().asString()
														  : null);
		// BEWARE!!	Store the password HASH so change the received clear-text password by a hashed password
		dbEntity.setPasswordHash(modelObj.getPasswordHash() != null ? modelObj.getPasswordHash().asString()		// BEWARE!!! at this point, the password is hashed!
																	: null);
		dbEntity.setExpiratesAt(modelObj.getPasswordExpirationDate() != null ? modelObj.getPasswordExpirationDate()
																			 : null);
		dbEntity.setUserOid(modelObj.getUserOid() != null ? modelObj.getUserOid().asString()
													   : null);
		dbEntity.setConfirmed(modelObj.isConfirmed());
	}
}