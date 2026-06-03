package r01f.db.transfer.security;

import r01f.db.entities.security.DBEntityForGoogleLogin;
import r01f.model.ModelObject;
import r01f.model.security.login.google.GoogleLogin;
import r01f.securitycontext.SecurityContext;
import r01f.services.core.persistence.db.DBEntity;

/**
 * Transfers a {@link ModelObject}'s state to a given {@link DBEntity}
 */
public abstract class GoogleLoginTransferToDBBase<L extends GoogleLogin,
												  DB extends DBEntityForGoogleLogin>
			  extends SecurityObjectTransferToDBBase<L,DB> {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public GoogleLoginTransferToDBBase() {
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
		dbEntity.setUserOid(modelObj.getUserOid() != null ? modelObj.getUserOid().asString()
													   	  : null);
		dbEntity.setEmail(modelObj.getUserOid() != null ? modelObj.getGoogleMail().asString()
													   	: null);
	}
}