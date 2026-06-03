package r01f.db.transfer.security;

import r01f.db.entities.security.DBEntityForGiltzaLogin;
import r01f.model.ModelObject;
import r01f.model.security.login.giltza.GiltzaLogin;
import r01f.securitycontext.SecurityContext;
import r01f.services.core.persistence.db.DBEntity;

/**
 * Transfers a {@link ModelObject}'s state to a given {@link DBEntity}
 */
public abstract class GiltzaLoginTransferToDBBase<L extends GiltzaLogin,
												  DB extends DBEntityForGiltzaLogin>
			  extends SecurityObjectTransferToDBBase<L,DB> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public GiltzaLoginTransferToDBBase() {
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
		dbEntity.setEmail(modelObj.getEmail() != null ? modelObj.getEmail().asString()
													   	: null);
	}
}