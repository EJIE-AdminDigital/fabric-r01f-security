package r01f.model.security.context;

import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;

@MarshallType(as="authenticatedActorForSystem")
@Accessors(prefix="_")
public class SecurityContextAuthenticatedActorForSystem 
     extends SecurityContextAuthenticatedActorBase {

	private static final long serialVersionUID = 7314511908348042769L;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * @see SecurityContextAuthenticatedActorBuilder
	 */
	public SecurityContextAuthenticatedActorForSystem() {
		super(SecurityProviderID.SYSTEM,LoginID.SYSTEM);
	}
}
