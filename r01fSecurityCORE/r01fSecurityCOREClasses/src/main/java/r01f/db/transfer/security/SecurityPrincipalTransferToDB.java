package r01f.db.transfer.security;

import r01f.db.entities.security.DBEntityForSecurityPrincipalBase;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;

public abstract class SecurityPrincipalTransferToDB<U extends IsSecurityPrincipal,DB extends DBEntityForSecurityPrincipalBase>
			  extends SecurityObjectWithIDTransferToDBBase<U,DB> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public SecurityPrincipalTransferToDB() {
		// nothing
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
												 final U modelObj,final DB dbEntity) {
		super.setDBEntityFieldsFromModelObject(securityContext,
											   modelObj,dbEntity);
		dbEntity.setSourceIdentityProvider(modelObj.getSourceIdentityProvider());
		
		dbEntity.setStatus(modelObj.getStatus() != null ? modelObj.getStatus()
														: null);
	}
}
