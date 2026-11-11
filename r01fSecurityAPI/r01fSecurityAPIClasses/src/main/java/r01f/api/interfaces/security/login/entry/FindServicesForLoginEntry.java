package r01f.api.interfaces.security.login.entry;

import r01f.api.interfaces.security.IsSecurityServiceInterface;
import r01f.model.security.login.entry.LoginEntry;
import r01f.oids.SecurityOIDs.LoginEntryOID;
import r01f.services.interfaces.FindServicesForModelObject;

public interface FindServicesForLoginEntry
		 extends FindServicesForModelObject<LoginEntryOID,LoginEntry>,
		 		 IsSecurityServiceInterface {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////

}

