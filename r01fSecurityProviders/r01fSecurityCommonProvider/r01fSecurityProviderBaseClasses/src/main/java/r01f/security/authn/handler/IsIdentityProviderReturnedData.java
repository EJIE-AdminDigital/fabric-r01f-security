package r01f.security.authn.handler;

import java.io.Serializable;

import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityIDs.SecurityPrincipalID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.types.contact.EMail;

public interface IsIdentityProviderReturnedData
		 extends Serializable {
/////////////////////////////////////////////////////////////////////////////////////////
//	SECURITY PRINCIPAL DATA
/////////////////////////////////////////////////////////////////////////////////////////	
	public IdentityProvider getIdentityProvider();
	
	public default SecurityPrincipalOID getSecurityPrincipalOid() {
		throw new UnsupportedOperationException("This method must be implemented by the concrete class");
	}
	public default SecurityPrincipalID getSecurityPrincipalId() {
		return SecurityPrincipalID.fromLoginId(this.getLoginId());	// OVERRIDE THIS METHOD IF THE SECURITY PRINCIPAL ID IS NOT A LOGIN ID
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public LoginID getLoginId();
	public EMail getMail();

}
