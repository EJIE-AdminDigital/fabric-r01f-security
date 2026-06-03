package r01f.db.transfer.security;


import r01f.db.entities.security.DBEntityForPairedPhoneLogin;
import r01f.model.ModelObject;
import r01f.model.security.login.pairedphone.PairedPhoneLogin;
import r01f.securitycontext.SecurityContext;
import r01f.services.core.persistence.db.DBEntity;

/**
 * Transfers a {@link ModelObject}'s state to a given {@link DBEntity}
 */

public abstract class PairedPhoneLoginTransferToDBBase<L extends PairedPhoneLogin,
                                                       DB extends DBEntityForPairedPhoneLogin>
	          extends SecurityObjectTransferToDBBase<L,DB> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public PairedPhoneLoginTransferToDBBase() {
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
		dbEntity.setPhone(modelObj.getPairedPhone() != null ? modelObj.getPairedPhone().asString()
													   	    : null);
	}
}
