package r01f.model.security.context;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.guids.CommonOIDs.AppCode;
import r01f.model.security.user.User;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.types.AppVersion;

@MarshallType(as="authenticatedActorForApiKeyUserBacked")
@Accessors(prefix="_")
public class SecurityContextAuthenticatedActorForApiKeyUserBacked<U extends User> 
     extends SecurityContextAuthenticatedActorForUser<U> 
  implements SecurityContextAuthenticatedActorForApiKey {

	private static final long serialVersionUID = -645271649341212481L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="apiKeyOid",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter protected final LoginOID _apiKeyOid;
	
	@MarshallField(as="apiKeyVersion",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter protected final AppVersion _apiKeyVersion;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * @see SecurityContextAuthenticatedActorBuilder
	 * @param loginId
	 * @param apiKeyVersion
	 */
	public SecurityContextAuthenticatedActorForApiKeyUserBacked(final LoginOID apiKeyOid,
																final LoginID loginId,final AppVersion apiKeyVersion,
													  	  		final U user) {
		super(SecurityProviderID.API_KEY,loginId,
			  user);
		_apiKeyOid = apiKeyOid;
		_apiKeyVersion = apiKeyVersion;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public AppCode getAppCode() {
		return _loginId.asAppCode();
	}
}
