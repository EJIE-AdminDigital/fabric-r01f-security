package r01f.db.transfer.security.webauthn;

import r01f.db.entities.security.webauthn.DBEntityForAuthenticatorCredentialBase;
import r01f.model.ModelObject;
import r01f.model.security.webauthn.AuthenticatorCredential;
import r01f.securitycontext.SecurityContext;
import r01f.services.core.persistence.db.DBEntity;
import r01f.services.core.persistence.db.TransfersModelObjectStateToDBEntity;

/**
 * Transfers a {@link ModelObject}'s state to a given {@link DBEntity}
 */
public abstract class AuthenticatorCredentialTransferToDBBase<C extends AuthenticatorCredential,
												              DB extends DBEntityForAuthenticatorCredentialBase<C>>
         implements TransfersModelObjectStateToDBEntity<C,DB> {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public AuthenticatorCredentialTransferToDBBase() {
		// nothing
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
												 final C modelObj,final DB dbEntity) {

		dbEntity.setOid(modelObj.getOid() != null ? modelObj.getOid().asString() : null);

		dbEntity.setCredentialId(modelObj.getCredentialId() != null ? modelObj.getCredentialId().asString()
														            : null);
		dbEntity.setUserOid(modelObj.getUserOid() != null ? modelObj.getUserOid().asString()
													   	  : null);
	}
}