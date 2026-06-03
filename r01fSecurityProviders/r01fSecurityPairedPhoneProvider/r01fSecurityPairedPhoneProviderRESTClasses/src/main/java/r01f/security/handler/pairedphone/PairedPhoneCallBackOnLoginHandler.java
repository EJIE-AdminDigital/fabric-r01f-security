package r01f.security.handler.pairedphone;

import java.net.URI;
import java.util.HashMap;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import lombok.extern.slf4j.Slf4j;
import r01f.jwt.JWT;
import r01f.model.security.login.pairedphone.PairedPhoneLoginConsent;
import r01f.model.security.login.pairedphone.PairedPhoneLoginConsentStatus;
import r01f.security.handler.CallBackOnLoginHandlerBase;
import r01f.security.provider.SecurityProvider;
import r01f.security.provider.SecurityProviderSecretConfig;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.types.contact.Phone;
import r01f.types.url.Url;
import r01f.types.url.UrlQueryString;
import r01f.types.url.UrlQueryStringParam;

@Slf4j
public class PairedPhoneCallBackOnLoginHandler<C extends PairedPhoneLoginConsent>
     extends CallBackOnLoginHandlerBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTANTS
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String JWT_CUSTOM_CLAIM_FOR_PAIRED_PHONE = "pairedPhone";
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public PairedPhoneCallBackOnLoginHandler(final SecurityProviderSecretConfig securityProviderSecretConfig) {
		super(securityProviderSecretConfig);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@SuppressWarnings({ "null", "serial" })
	public Response loginCallBack(final C loginConsent,
							 	  final Url to) {
		
		if (loginConsent == null) {
			throw new IllegalStateException("Phone login Consent is null!!!!");
		}
		
		log.info("===========================================================");
		log.warn("INFORMATIVE Purpose: AFTER LOGIN, by provider type: Paired Phone. Login Consent oid={}", loginConsent.getOid());
		log.info("===========================================================");

		// [1] - Get the target url
		Url targetUrl = null;
		PairedPhoneLoginConsentStatus status = null;
		if (loginConsent != null) {
			// Check the login consent.
		    status = loginConsent.getStatus();
			switch (status) {
				case REQUESTED:
					// redir to login url again
					targetUrl = _securityProviderSecretConfig.getLoginUrl();
					log.info("paired phone login request, but no response received: ",
							 targetUrl);
					break;
				case DENIED:
					// redir to a page where the user can change the login
					targetUrl = Url.from("TODO/get/the/user/password/expired/url");
					log.warn("paired phone denied login: {}",
							 targetUrl);
					break;
				case ACCEPTED:
					// redir to a page where the user can change the login
					targetUrl = to;
					log.info("paired phone ACCEPTED login: {}",
							 targetUrl);
					break;
				default:
					throw new IllegalStateException("NOT a supported login CONSENT response error!!");
				}
		}

		// [2] - Get the jwt and put it on the url as param
		JWT jwt = null;
		if (status != null
		 && status.is(PairedPhoneLoginConsentStatus.ACCEPTED)) {
			// a) Create a jwt token
			UserOID  userOid = loginConsent.getResponse().getUserOid();
			Phone phone = loginConsent.getRequest().getPhone();

			jwt = _createJWT(//Secret Key Dat.
					         _securityProviderSecretConfig.getKeyData().asString(),
							 // Provider Type
					         SecurityProvider.PAIRED_PHONE.getCode(),LoginID.forId(phone.asString()),	// uses the [phone] as [loginId]
	                         // Custom Claims
					         new HashMap<String,String>() {{
					        	 			put(JWT_CUSTOM_CLAIM_FOR_USER_OID,userOid.asString());
	                        				put(JWT_CUSTOM_CLAIM_FOR_PAIRED_PHONE,phone.asString());
								    	 }});

			// b) Create a url like to?jwt={jwt_token}
			UrlQueryString qryStr = UrlQueryString.fromParams(UrlQueryStringParam.of("jwt",jwt.asString()));
			targetUrl = Url.from(to)
						   .joinWith(qryStr);
			
			log.warn("INFORMATIVE Purpose: END AFTER LOGIN. Login ID = {}",
					 LoginID.forId(phone.asString()));
		} else {
			log.warn("Status is NOT ACCEPTED. Value = {}",
					 status);
		}

		// [99] - Redir
		URI targetURI = UriBuilder.fromUri(targetUrl.asString())
								  .build();
		return Response.seeOther(targetURI)
					   .build();
	}
}
