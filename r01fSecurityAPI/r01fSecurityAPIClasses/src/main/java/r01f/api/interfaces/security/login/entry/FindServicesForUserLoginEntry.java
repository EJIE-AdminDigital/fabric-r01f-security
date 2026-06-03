package r01f.api.interfaces.security.login.entry;

import r01f.api.interfaces.security.SecurityServiceInterface;
import r01f.model.security.login.entry.UserLoginEntry;
import r01f.securitycontext.SecurityOIDs.UserLoginEntryOID;
import r01f.services.interfaces.FindServicesForModelObject;

public interface FindServicesForUserLoginEntry<E extends UserLoginEntry>
		 extends FindServicesForModelObject<UserLoginEntryOID,E>,
		 		 SecurityServiceInterface {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////

}

