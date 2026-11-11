package r01f.security.handler.saml;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.debug.Debuggable;
import r01f.jwt.JWT;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityIDs.SecurityPrincipalID;
import r01f.security.authn.handler.CallBackOnLoginHandlerBase;
import r01f.security.authn.provider.config.AuthenticationProviderSecretConfig;
import r01f.security.saml.api.SAMLAPI;
import r01f.security.saml.api.SAMLAPIForIdPResponse;
import r01f.security.saml.api.SAMLIdPBinding;
import r01f.security.saml.api.SAMLResponse;
import r01f.types.contact.EMail;
import r01f.types.url.Url;
import r01f.types.url.UrlQueryString;
import r01f.types.url.UrlQueryStringParam;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;

/**
 * After successfully logged in SAML, the user will be redirected to this handler
 * and here THREE things happen:
 * 		1. A SAML [access token] is created using the received saml [authorization code]
 * 		2. With the [access token] get the [user data]
 * 		3. Create a JWT token
 * 		2. The final url (the target app url) is composed with the JWT as a querystring param
 * The target app will check if the querystring-received JWT token is a legitimate one and if so
 * allow the user access
 */
@Slf4j
public class SAMLCallBackOnLoginHandler
	 extends CallBackOnLoginHandlerBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTANTS
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String JWT_CUSTOM_CLAIM_FOR_USER_NAME = "user-name";
	public static final String JWT_CUSTOM_CLAIM_FOR_USER_LAST_NAME = "user-last-name";
	public static final String JWT_CUSTOM_CLAIM_FOR_USER_DISPLAY_NAME = "user-display-name";
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final SAMLAPI  _samlAPI;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public SAMLCallBackOnLoginHandler(final AuthenticationProviderSecretConfig securityProviderSecretConfig,
									  final SAMLAPI samlAPI) {
		super(securityProviderSecretConfig);
		_samlAPI = samlAPI;
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * This method is called by the idp after being loged-in (see RESTDelegateBaseForSAMLLogin)
	 * @param idp
	 * @param idpBinding
	 * @param samlResponse,
	 * @param afterLoginRedirToUrl
	 * @return
	 */
	public Response loginCallBack(final IdentityProvider idp,final SAMLIdPBinding idpBinding,
								  final String samlResponse,
								  final Url afterLoginRedirToUrl) {
		log.info("=========================================================");
		log.warn("INFORMATIVE Purpose: AFTER LOGIN, by provider type: SAML (idp={})",idp);
		log.info("=========================================================");

		// [0] - Get the SAML API
		SAMLAPIForIdPResponse responseAPI = _samlAPI.usingIdP(idp)
												    .getIdPResponseAPI();

		// [1] - Get the SAML response containing the assertions
		SAMLResponse resp = responseAPI.decodeAndValidateAuthnResponse(samlResponse,
											  					   	   idpBinding);
		Map<String,String> attrs = SAMLAPIForIdPResponse.getAttributesFromResponse(resp);

		// [2] - a bit of debugging
		_debugAssertions(attrs);

		// [3] - Create a callback redir url
		// Map the attributes
		SAMLAttributes samlAttributes = new SAMLAttributes(idp,attrs);
		log.debug("{}",samlAttributes.debugInfo());

		// The attribute names (assertions) are configured at OKTA console
		JWT jwt = _createJWT(_securityProviderSecretConfig.getKeyData()
														  .asString(),
							 idp,AuthenticationMethod.SAML,
							 SecurityPrincipalID.fromLoginId(samlAttributes.getLogin()),
							 samlAttributes.getEmail(),
							 samlAttributes.createJWTClaims());

		// Create a url like afterLoginRedirToUrl?jwt={the_jwt}
		UrlQueryString qryStr = UrlQueryString.fromParams(UrlQueryStringParam.of("jwt",jwt.asString()));
		Url redirUrl = afterLoginRedirToUrl.joinWith(qryStr);
		
		// [2] - Redir
		URI targetURI = UriBuilder.fromUri(redirUrl.asString())
								  .build();
		
		log.warn("INFORMATIVE Purpose: END AFTER LOGIN. Login ID = {}",
				 samlAttributes.getLogin());

		return Response.seeOther(targetURI)
					   .build();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	private static final String STD_ASSERTION_NAME_PREFIX = "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/";
	private static final String MS_ASSERTION_NAME_PREFIX = "http://schemas.microsoft.com/ws/2008/06/identity/claims/";
	private static final String AZUREAD_ASSERTION_NAME_PREFIX = "http://schemas.microsoft.com/identity/claims/";

	@Accessors(prefix="_")
	private class SAMLAttributes
	   implements Debuggable {
		@Getter private LoginID _login;
		@Getter private EMail _email;
		@Getter private String _name;
		@Getter private String _lastName;
		@Getter private String _displayName;
		@Getter private String _userPrincipalName;

		SAMLAttributes(final IdentityProvider samlIdp,
					   final Map<String,String> attrs) {
			// Map the attrs
//			if (samlIdp.is(SAMLEuskadiConstants.OKTA)) {
//				// The attribute names (assertions) are configured at OKTA console
//				_login = attrs.get("login") != null ? LoginID.forId(attrs.get("login")) : null;
//				_email = attrs.get("email") != null ? EMail.of(attrs.get("email")) : null;
//				_name = attrs.get("user-fisrt-name");
//				_lastName = attrs.get("user-last-name");
//			}
			if (samlIdp.isIn(IdentityProvider.ELKARLAN_AZURE_AD,
							 IdentityProvider.ELKARLAN2_AZURE_AD)) {
				// The attribute names (assertions) are configured at ADFS console
				_login = attrs.get(MS_ASSERTION_NAME_PREFIX + "windowsaccountname") != null ? LoginID.forId(attrs.get(MS_ASSERTION_NAME_PREFIX + "windowsaccountname")) : null;
				_email = attrs.get(STD_ASSERTION_NAME_PREFIX + "emailaddress") != null ? EMail.of(attrs.get(STD_ASSERTION_NAME_PREFIX + "emailaddress")) : null;
				_name = attrs.get(STD_ASSERTION_NAME_PREFIX + "givenname");
				_lastName = attrs.get(STD_ASSERTION_NAME_PREFIX + "surname");
				_displayName = attrs.get(STD_ASSERTION_NAME_PREFIX + "name");
				_userPrincipalName = attrs.get(STD_ASSERTION_NAME_PREFIX + "upn");
			}
			else if (samlIdp.isIn(IdentityProvider.OSAKIDETZA_AZURE_AD,
								  IdentityProvider.OSAKIDETZA_PD_AZURE_AD)) {
				// The attribute names (assertions) are configured at Azure AD service
				// for example http://schemas.microsoft.com/identity/claims/displayname = R01PLATEAWeb - Test User
				// Claims básicos de identidad:
				//	- sub: Identificador del sujeto (usuario). Es único para cada usuario dentro del inquilino.
				//	- name: Nombre completo del usuario.
				//	- preferred_username: Nombre de usuario preferido, a menudo es la dirección de correo electrónico.
				//	- given_name: Nombre de pila del usuario.
				//	- family_name: Apellido del usuario.
				//	- email: Dirección de correo electrónico del usuario.
				//TODO revisar parámetros
				_login = attrs.get(AZUREAD_ASSERTION_NAME_PREFIX + "displayname") != null ? LoginID.forId(attrs.get(AZUREAD_ASSERTION_NAME_PREFIX + "displayname")) : null;
				_email = attrs.get(STD_ASSERTION_NAME_PREFIX + "emailaddress") != null ? EMail.of(attrs.get(STD_ASSERTION_NAME_PREFIX + "emailaddress")) : null;
				_name = attrs.get(STD_ASSERTION_NAME_PREFIX + "givenname");
				_lastName = attrs.get(STD_ASSERTION_NAME_PREFIX + "surname");
				_displayName = attrs.get(STD_ASSERTION_NAME_PREFIX + "name");
				_userPrincipalName = attrs.get(STD_ASSERTION_NAME_PREFIX + "upn");
			} else {
				throw new IllegalArgumentException(samlIdp + " is NOT a supported [SAML Identity Provider]!");
			}
		}
		public Map<String,String> createJWTClaims() {
			return new HashMap<String,String>() {{
							put(JWT_CUSTOM_CLAIM_FOR_USER_NAME,Strings.isNOTNullOrEmpty(_name) ? _name : "");
							put(JWT_CUSTOM_CLAIM_FOR_USER_LAST_NAME,Strings.isNOTNullOrEmpty(_lastName) ? _lastName : "");
							put(JWT_CUSTOM_CLAIM_FOR_USER_DISPLAY_NAME,Strings.isNOTNullOrEmpty(_displayName) ? _displayName : "");
					}};
		}
		@Override
		public CharSequence debugInfo() {
			return Strings.customized("SAML attributes:\n" +
									  "              login={}\n" +
									  "              email={}\n" +
									  "               name={}\n" +
									  "          last-name={}\n" +
									  "       display-name={}\n" +
									  "user-principal-name={}",
									  _login,
									  _email,
									  _name,_lastName,_displayName,
									  _userPrincipalName);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	DEBUG
/////////////////////////////////////////////////////////////////////////////////////////
	private static void _debugAssertions(final Map<String,String> attrs) {
		// Debug
		log.debug("[saml callback on login] > Received {} SAML assertions",
				 CollectionUtils.hasData(attrs) ? attrs.size() : 0);
		if (CollectionUtils.isNullOrEmpty(attrs)) {
			log.debug("[saml callback on login] SAML IdP did NOT return any assertion");
			return;
		}
		if (CollectionUtils.hasData(attrs)) {
			attrs.entrySet()
				 .stream()
				 .forEach(me -> log.trace("\t\t-{} = {}",me.getKey(),me.getValue()));	// BEWARE!! personal info will be printed to the logs! do NOT enable in production env
		}
	}
}
