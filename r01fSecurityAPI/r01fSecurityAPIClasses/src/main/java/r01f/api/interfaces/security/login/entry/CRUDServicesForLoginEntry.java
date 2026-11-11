package r01f.api.interfaces.security.login.entry;

import r01f.api.interfaces.security.IsSecurityServiceInterface;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.entry.LoginEntry;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.LoginEntryEvent;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityOIDs.LoginEntryOID;
import r01f.services.interfaces.CRUDServicesForModelObject;

public interface CRUDServicesForLoginEntry
		 extends CRUDServicesForModelObject<LoginEntryOID,LoginEntry>,
		 		 IsSecurityServiceInterface {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * load las login entry (the most recent one)
	 * @param securityContext
	 * @param providerId
	 * @param loginId
	 * @param event
	 * @return
	 */
	public CRUDResult<LoginEntry> loadLastLoginEntryFor(final SecurityContext securityContext,
											   			final IdentityProvider providerId,final LoginID loginId,
											   			final LoginEntryEvent event);
}

