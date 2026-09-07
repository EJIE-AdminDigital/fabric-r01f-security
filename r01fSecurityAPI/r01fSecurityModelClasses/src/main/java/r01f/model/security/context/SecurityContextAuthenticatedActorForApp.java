package r01f.model.security.context;

import lombok.experimental.Accessors;
import r01f.guids.CommonOIDs.AppCode;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.securitycontext.SecurityIDS.SecurityToken;

@MarshallType(as="authenticatedActorForApp")
@Accessors(prefix="_")
public class SecurityContextAuthenticatedActorForApp 
     extends SecurityContextAuthenticatedActorBase {


	private static final long serialVersionUID = -645271649341212481L;

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * @see SecurityContextAuthenticatedActorBuilder
	 * @param appcode
	 */
	public SecurityContextAuthenticatedActorForApp(final AppCode appCode) {
		super(SecurityProviderID.SYSTEM,LoginID.fromAppCode(appCode));
	}
	
	
	/**
	 * @see SecurityContextAuthenticatedActorBuilder
	 * @param appcode
	 */
	public SecurityContextAuthenticatedActorForApp(final SecurityToken clientIdOfOAuthBasedClientCredentials) {
		super(SecurityProviderID.IDENTITY_BROKER,
			  LoginID.fromString(clientIdOfOAuthBasedClientCredentials.asString()));
	}
	
	
	
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public AppCode getAppCode() {
		return _loginId.asAppCode();
	}
}
