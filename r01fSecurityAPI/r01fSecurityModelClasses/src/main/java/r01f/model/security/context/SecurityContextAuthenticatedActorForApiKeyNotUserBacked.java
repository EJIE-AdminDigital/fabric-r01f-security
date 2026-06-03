package r01f.model.security.context;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.guids.CommonOIDs.AppCode;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.types.AppVersion;

@MarshallType(as="authenticatedActorForApiKey")
@Accessors(prefix="_")
public class SecurityContextAuthenticatedActorForApiKeyNotUserBacked
     extends SecurityContextAuthenticatedActorBase
  implements SecurityContextAuthenticatedActorForApiKey {

	private static final long serialVersionUID = 6775310586709594797L;
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
	public SecurityContextAuthenticatedActorForApiKeyNotUserBacked(final LoginOID apiKeyOid,
																   final LoginID loginId,final AppVersion apiKeyVersion) {
		super(SecurityProviderID.API_KEY,loginId);
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
