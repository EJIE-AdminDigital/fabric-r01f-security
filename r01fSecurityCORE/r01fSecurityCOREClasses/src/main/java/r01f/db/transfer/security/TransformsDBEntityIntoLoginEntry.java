package r01f.db.transfer.security;

import r01f.db.entities.security.DBEntityForLoginEntryBase;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.entry.LoginEntry;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityOIDs.LoginEntryOID;
import r01f.oids.SecurityOIDs.LoginOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.core.persistence.db.TransformsDBEntityIntoModelObject;

public class TransformsDBEntityIntoLoginEntry<DB extends DBEntityForLoginEntryBase> 
  implements TransformsDBEntityIntoModelObject<DB,LoginEntry> {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public LoginEntry dbEntityToModelObject(final SecurityContext securityContext,
											final DB dbEntity) {
		LoginEntry outLoginEntry = new LoginEntry();
		outLoginEntry.setOid(dbEntity.getOid() != null ? LoginEntryOID.from(dbEntity.getOid()) : null);
		
		outLoginEntry.setEvent(dbEntity.getEvent());
		outLoginEntry.setAt(dbEntity.getAt());
		
		outLoginEntry.setLoginOid(dbEntity.getLoginOid() != null ? LoginOID.from(dbEntity.getLoginOid()) : null);
		outLoginEntry.setLoginId(dbEntity.getLoginId() != null ? LoginID.from(dbEntity.getLoginId()) : null);
		
		outLoginEntry.setSecurityPrincipalOid(dbEntity.getSecurityPrincipalOid() != null ? SecurityPrincipalOID.from(dbEntity.getSecurityPrincipalOid()) : null);
		
		return outLoginEntry;
	}

}
