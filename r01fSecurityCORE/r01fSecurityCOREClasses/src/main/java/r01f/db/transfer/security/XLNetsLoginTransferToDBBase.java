package r01f.db.transfer.security;

import r01f.db.entities.security.DBEntityForXLNetsLogin;
import r01f.model.ModelObject;
import r01f.model.security.login.xlnets.XLNetsLogin;
import r01f.securitycontext.SecurityContext;
import r01f.services.core.persistence.db.DBEntity;

/**
 * Transfers a {@link ModelObject}'s state to a given {@link DBEntity}
 */
public abstract class XLNetsLoginTransferToDBBase<L extends XLNetsLogin,
												  DB extends DBEntityForXLNetsLogin>
			  extends SecurityObjectTransferToDBBase<L,DB> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public XLNetsLoginTransferToDBBase() {
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
		dbEntity.setWorkPlace(modelObj.getWorkPlaceCode() != null ? modelObj.getWorkPlaceCode().asString()
																  : null);
	}
}