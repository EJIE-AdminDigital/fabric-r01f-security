package r01f.db.transfer.security;


import r01f.db.entities.security.DBEntityForApiKeyLogin;
import r01f.model.security.login.apikey.ApiKeyLogin;
import r01f.securitycontext.SecurityContext;

public abstract class ApiKeyLoginTransferToDBBase<L extends ApiKeyLogin,
												  DB extends DBEntityForApiKeyLogin>
     		  extends SecurityObjectTransferToDBBase<L,DB> {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
												 final L modelObj,final DB dbEntity) {
		super.setDBEntityFieldsFromModelObject(securityContext,
											   modelObj,dbEntity);
		// owner
		dbEntity.setUserOid(modelObj.getUserOid() != null ? modelObj.getUserOid().asString() : null);

		// Login ID [ Remenber that for ApiKeyLogin, the LoginID is the PublicKey ..]
		dbEntity.setLoginId(modelObj.getLoginId()!= null ? modelObj.getLoginId().asString() : null);

		// version
		dbEntity.setVersion(modelObj.getApiKeyVersion() != null ? modelObj.getApiKeyVersion().asString() : null);

		// public key
		dbEntity.setPublicKey(modelObj.getApiKeyPair() != null
						   && modelObj.getApiKeyPair().getPublicKey() != null ? modelObj.getApiKeyPair().getPublicKey().asString()
								   											  : null);

		// readonly
		dbEntity.setReadOnly(modelObj.isReadOnly());

		// active
		dbEntity.setActive(modelObj.isActiveNow());

		// usage range
		dbEntity.setValidFrom(modelObj.getApiKeyUsageRange().hasLowerBound() ? modelObj.getApiKeyUsageRange().getLowerBound()
																			 : null);
		dbEntity.setValidTo(modelObj.getApiKeyUsageRange().hasUpperBound() ? modelObj.getApiKeyUsageRange().getUpperBound()
				 												 	   	   : null);


	}
}