package r01f.model.security.context;

import r01f.securitycontext.SecurityContextAuthenticatedActor;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.types.AppVersion;

public interface SecurityContextAuthenticatedActorForApiKey 
		 extends SecurityContextAuthenticatedActor {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public LoginOID getApiKeyOid();
	public AppVersion getApiKeyVersion();
}
