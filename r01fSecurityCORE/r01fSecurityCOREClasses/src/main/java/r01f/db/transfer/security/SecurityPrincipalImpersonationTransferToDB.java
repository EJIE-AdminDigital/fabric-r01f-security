package r01f.db.transfer.security;

import r01f.db.entities.security.DBEntityForSecurityPrincipalImpersonationBase;
import r01f.model.ModelObject;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.impersonation.SecurityPrincipalImpersonation;
import r01f.services.core.persistence.db.DBEntity;

/**
 * Transfers a {@link ModelObject}'s state to a given {@link DBEntity}
 */
public class SecurityPrincipalImpersonationTransferToDB<DB extends DBEntityForSecurityPrincipalImpersonationBase>
     extends SecurityObjectTransferToDBBase<SecurityPrincipalImpersonation,DB> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public SecurityPrincipalImpersonationTransferToDB() {
		// nothing
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
												 final SecurityPrincipalImpersonation modelObj,final DB dbEntity) {
		super.setDBEntityFieldsFromModelObject(securityContext, 
											   modelObj,dbEntity);
		
		// impersonation
		dbEntity.setImpersonatedPrincipalOid(modelObj.getImpersonatedPrincipalOid() != null ? modelObj.getImpersonatedPrincipalOid().asString() : null);
		dbEntity.setImpersonatingPrincipalOid(modelObj.getImpersonatingPrincipalOid() != null ? modelObj.getImpersonatingPrincipalOid().asString() : null);
		
		// date range
		dbEntity.setStartedAt(modelObj.getDateRange() != null ? modelObj.getDateRange().getLowerBound() : null);
		dbEntity.setFinishedAt(modelObj.getDateRange() != null ? modelObj.getDateRange().getUpperBound() : null);
	}
}