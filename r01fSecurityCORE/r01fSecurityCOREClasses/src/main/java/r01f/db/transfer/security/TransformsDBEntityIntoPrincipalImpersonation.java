package r01f.db.transfer.security;

import java.time.Instant;

import r01f.db.entities.security.DBEntityForSecurityPrincipalImpersonationBase;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.impersonation.SecurityPrincipalImpersonation;
import r01f.oids.SecurityOIDs.SecurityPrincipalImpersonationOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.core.persistence.db.TransformsDBEntityIntoModelObject;
import r01f.types.Range;

public class TransformsDBEntityIntoPrincipalImpersonation<DB extends DBEntityForSecurityPrincipalImpersonationBase> 
  implements TransformsDBEntityIntoModelObject<DB,SecurityPrincipalImpersonation> {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public SecurityPrincipalImpersonation dbEntityToModelObject(final SecurityContext securityContext,
															    final DB dbEntity) {
		SecurityPrincipalImpersonation outImpersonation = new SecurityPrincipalImpersonation();
		outImpersonation.setOid(dbEntity.getOid() != null ? SecurityPrincipalImpersonationOID.from(dbEntity.getOid()) : null);
		 
		outImpersonation.setDateRange(new Range<Instant>(dbEntity.getStartedAt(),dbEntity.getFinishedAt()));
		
		outImpersonation.setImpersonatedPrincipalOid(dbEntity.getImpersonatedPrincipalOid() != null ? SecurityPrincipalOID.from(dbEntity.getImpersonatedPrincipalOid()) : null);
		outImpersonation.setImpersonatingPrincipalOid(dbEntity.getImpersonatingPrincipalOid() != null ? SecurityPrincipalOID.from(dbEntity.getImpersonatingPrincipalOid()) : null);
		
		return outImpersonation;
	}

}
