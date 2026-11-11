package r01f.db.transfer.security;

import r01f.db.entities.security.DBEntityForLoginBase;
import r01f.model.ModelObject;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.Login;
import r01f.model.security.login.data.oauthflow.LoginDataForOAUTHFlow;
import r01f.services.core.persistence.db.DBEntity;

/**
 * Transfers a {@link ModelObject}'s state to a given {@link DBEntity}
 */
public class LoginTransferToDB<DB extends DBEntityForLoginBase>
	 extends SecurityObjectTransferToDBBase<Login,DB> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public LoginTransferToDB() {
		// nothing
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
												 final Login modelObj,final DB dbEntity) {
		super.setDBEntityFieldsFromModelObject(securityContext, 
											   modelObj,dbEntity);
		
		// Id
		dbEntity.setId(modelObj.getId() != null ? modelObj.getId().asString() : null);
		dbEntity.setIdFormat(modelObj.getIdFormat());
		
		// status
		dbEntity.setStatus(modelObj.getStatus());
		
		// Security principal
		dbEntity.setSecurityPrincipalOid(modelObj.getSecurityPrincipalOid() != null ? modelObj.getSecurityPrincipalOid().asString() : null);
		dbEntity.setSecurityPrincipalId(modelObj.getSecurityPrincipalId() != null ? modelObj.getSecurityPrincipalId().asString() : null);
		dbEntity.setSecurityPrincipalSourceIdentityProvider(modelObj.getSecurityPrincipalSourceIdentityProvider());
		
		// Authentication method & Identity broker (if any)
		dbEntity.setAuthenticationMethod(modelObj.getAuthenticationMethod());
		if (modelObj.getData() != null
		 && modelObj.getData() instanceof LoginDataForOAUTHFlow oauthLoginData) {
			dbEntity.setIdentityBroker(oauthLoginData.getIdentityBroker());
		} else {
			dbEntity.setIdentityBroker(null);
		}
		
		// valid range
		dbEntity.setValidFrom(modelObj.getValidRange() != null ? modelObj.getValidRange().getLowerBound() : null);
		dbEntity.setValidTo(modelObj.getValidRange() != null ? modelObj.getValidRange().getUpperBound() : null);
	}
}