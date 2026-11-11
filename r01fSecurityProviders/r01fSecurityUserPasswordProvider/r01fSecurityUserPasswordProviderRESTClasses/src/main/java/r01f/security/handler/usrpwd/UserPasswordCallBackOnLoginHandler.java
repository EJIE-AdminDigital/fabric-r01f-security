package r01f.security.handler.usrpwd;

import java.net.URI;
import java.util.HashMap;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import lombok.extern.slf4j.Slf4j;
import r01f.jwt.JWT;
import r01f.model.security.login.authn.secondfactor.LoginAuthnSecondFactorUsingOTP;
import r01f.model.security.login.userpassword.messages.login.UserPasswordLoginResponse;
import r01f.model.security.login.userpassword.messages.login.UserPasswordLoginResult;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityIDs.SecurityPrincipalID;
import r01f.oids.SecurityOIDs.LoginOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.security.authn.handler.CallBackOnLoginHandlerBase;
import r01f.security.authn.provider.config.AuthenticationProviderSecretConfig;
import r01f.types.url.Url;
import r01f.types.url.UrlQueryString;
import r01f.types.url.UrlQueryStringParam;

/**
 * After successfully logged in, the user will be redirected to this handler
 * and here TWO things happen:
 * 		1. A JWT token is created from the XLNets session
 * 		2. The final url (the target app url) is composed with the JWT as a querystring param
 * The target app will check if the querystring-received JWT token is a legitimate one and if so
 * allow the user access
 */
@Slf4j
public class UserPasswordCallBackOnLoginHandler
     extends CallBackOnLoginHandlerBase {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public UserPasswordCallBackOnLoginHandler(final AuthenticationProviderSecretConfig securityProviderSecretConfig) {
		super(securityProviderSecretConfig);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public Response loginCallBack(final UserPasswordLoginResponse loginResponse,
							 	  final Url to) {
		log.info("=========================================================");
		log.warn("INFORMATIVE Purpose: AFTER LOGIN, by provider type: user/password. Login ID: {}", loginResponse.getLoginId());
		log.info("=========================================================");
		// [ Case 1 ]  Login Has Failed
		if (loginResponse.hasFailed()) {
			return _loginCallBackOnLoginResponseFail(loginResponse, to);
		}
		// [ Case 2 ]  Login Has Suceeded
		else if (loginResponse.hasSucceeded()) {
			return _loginCallBackOnLoginResponseSuccess(loginResponse, to);
		}
		// [ Case 3 ]  Login is pending on auth factor.
		else if (loginResponse.isAwaitingAuthnSecondFactorVerification()) {
			return _loginCallBackOnLoginResponsePendingOnAuthFactor(loginResponse, to);
		}
		throw new IllegalStateException(" Not valid LoginResponse type : " + loginResponse.getClass());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  PROTECTED METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	protected Response _loginCallBackOnLoginResponseSuccess(final UserPasswordLoginResponse loginResponse,
															final Url to) {
		if (!loginResponse.hasSucceeded()) throw new IllegalArgumentException("This method should only be called when the login has been successful");
		
		Url targetUrl = null;
		
		// The login & password was correct
		log.info("user & password login for loginId={} OK!",
				 loginResponse.getLoginId());

		// a) Create a jwt token
		LoginOID loginOid = loginResponse.getLoginOid();
		LoginID loginId = loginResponse.getLoginId();
		SecurityPrincipalOID  principalOid = loginResponse.getSecurityPrincipalOid();
		SecurityPrincipalID principalId = SecurityPrincipalID.fromLoginId(loginId);

		@SuppressWarnings("serial")
		JWT jwt = _createJWT(_securityProviderSecretConfig.getKeyData().asString(),
							 IdentityProvider.FABRIC,AuthenticationMethod.USER_PASSWORD,
							 principalId,
							 new HashMap<String,String>() {{
								 	put(JWT_CUSTOM_CLAIM_FOR_PRINCIPAL_OID,principalOid.asString());
							 }});
		// b) Create a url like to?jwt={jwt_token}
		UrlQueryString qryStr = UrlQueryString.fromParams(UrlQueryStringParam.of("jwt",jwt.asString()));
		targetUrl = Url.from(to)
					   .joinWith(qryStr);
		URI targetURI = UriBuilder.fromUri(targetUrl.asString())
								  .build();
		
		log.warn("INFORMATIVE Purpose: END AFTER LOGIN. Login ID = {}. SUCCESS",
				 loginId);

		return Response.seeOther(targetURI)
						.build();

	}

	protected Response  _loginCallBackOnLoginResponseFail(final UserPasswordLoginResponse loginResponse,
														  final Url to) {
		if (!loginResponse.hasFailed()) throw new IllegalArgumentException("This method should only be called when the login has failed");
		
		Url targetUrl = null;
		
		// The login & password is NOT correct, redir again to the login url
		UserPasswordLoginResult errorType = loginResponse.getResult();
		
		log.info("user & password login for loginId={} NOK! > {}",
				 loginResponse.getLoginId(),
				 errorType);

		switch (errorType) {
				case INVALID_LOGIN_ID:
				case PASSWORD_ERROR:
					// redir to login url again
					targetUrl = _securityProviderSecretConfig.getLoginUrl();
					log.info("USER: {} >>> user-entered loginId / password is NOT correct: redir again to the login url: {}",
							 loginResponse.getLoginId(), 
							 targetUrl);
					break;
				case PASSWORD_EXPIRED:
					// redir to a page where the user can change the login
					targetUrl = Url.from("TODO/get/the/user/password/expired/url");
					log.info("USER: {} >>> user password has EXPIRED: redir to the password expired url: {}",
							 loginResponse.getLoginId(),
							 targetUrl);
					break;
				case LOGIN_NOT_ACTIVE:
					// redir to login url again
					targetUrl = _securityProviderSecretConfig.getLoginUrl();
					log.info("USER: {} >>> user account is not confirmed: redir again to the login url: {}",
							 loginResponse.getLoginId(),
							 targetUrl);
					break;
				case AUTHN_SECOND_FACTOR_MEAN_NOT_REGISTRED:
					// redir to login url again
					targetUrl = _securityProviderSecretConfig.getLoginUrl();
					log.warn("USER: {} >>> auth factor, phone ore mail not registred: {}",
							 loginResponse.getLoginId(),
							 targetUrl);
					break;
				case AUTHN_SECOND_FACTOR_SECRET_ERROR:
					// redir to login url again
					targetUrl = _securityProviderSecretConfig.getLoginUrl();
					log.warn("USER: {} >>> auth factor, SECRET error: {}",
							 loginResponse.getLoginId(),
							 targetUrl);
					break;
				default:
					throw new IllegalStateException(loginResponse.getLoginId() + 
													" USER >>> (" + errorType + ") is NOT a supported login response error!!");
				}

		// [99] - Redir
		URI targetURI = UriBuilder.fromUri(targetUrl.asString())
									.build();
		
		log.warn("INFORMATIVE Purpose: END AFTER LOGIN. Login ID = {}. FAILED ({})",
				 loginResponse.getLoginId(),
				 errorType);

		return  Response.seeOther(targetURI)
							.header("x-r01-userpwd-error",loginResponse.getResult())
							.build();

	}

	protected Response  _loginCallBackOnLoginResponsePendingOnAuthFactor(final UserPasswordLoginResponse loginResponse,
																		 final Url to) {
		if (!loginResponse.isAwaitingAuthnSecondFactorVerification()) throw new IllegalArgumentException("This method should only be called when the login is pending on an auth second factor");

		UserPasswordLoginAuthFactorConfig authFactorConfig =  UserPasswordLoginAuthFactorConfig.createFrom(_securityProviderSecretConfig.getProperties());

		Url targetUrl = null;

		log.info(" Login was ok, but is pending on auth factor, must request to the user ({}) a secret data, otp, etc.", loginResponse.getLoginId());

		String authFactorValidationUriProp = "auth_factors_validationUrl";
		LoginID loginId = loginResponse.getLoginId();
		// a)  get the authFactorsValidationUrl
		Url	authFactorValidationUrl = null;
		if ( _securityProviderSecretConfig.getProperty(authFactorValidationUriProp) == null ||
				_securityProviderSecretConfig.getProperty(authFactorValidationUriProp).asUrl() == null) {
			log.warn(" [ Warn! ] Not provided any uri for validating user ({}) secret known data. The same login uri will be returned", 
					 loginResponse.getLoginId());
			authFactorValidationUrl = _securityProviderSecretConfig.getLoginUrl();
		}  else {
			log.warn(" [ Warn! ] User ({}). Auth Factor Validation URI : {}", 
					 loginResponse.getLoginId(),
					 authFactorConfig.getAuthFactorsValidationUri());
			authFactorValidationUrl = authFactorConfig.getAuthFactorsValidationUri();
		}
		log.info("User ({}). auth validation url {}",
				 loginResponse.getLoginId(), 
				 authFactorValidationUrl);

		// b)  join query string
		UrlQueryString qryStr = null;
		if (loginResponse.getAuthnSecondFactor()
						 .usesOTP()) {
			LoginAuthnSecondFactorUsingOTP authFactorByOTP = loginResponse.getAuthnSecondFactor()
																	      .asLoginAuthSecondFactorUsingOTP();
			if (authFactorByOTP.getContactMean() == null) throw new IllegalStateException("Contact mean (email or phone) is null for loginId=" + loginResponse.getLoginId());
			if (authFactorByOTP.isOTPDeliveredByMail()) {
				qryStr = UrlQueryString.fromParams(UrlQueryStringParam.of("to",to.asString()),
												   UrlQueryStringParam.of("loginId",loginId.asString()),
												   UrlQueryStringParam.of("email",authFactorByOTP.getContactMean()
														   										 .asEMail()
														   										 .asString()));


			} else if (authFactorByOTP.isOTPDeliveredBySms()) {
				qryStr = UrlQueryString.fromParams(UrlQueryStringParam.of("to",to.asString()),
												   UrlQueryStringParam.of("loginId",loginId.asString()),
												   UrlQueryStringParam.of("phone",authFactorByOTP.getContactMean()
														   										 .asPhone()
														   										 .asString()));

			} else throw new IllegalArgumentException("Not valid OTP type :" + authFactorByOTP.getClass());
		} else {
			 qryStr = UrlQueryString.fromParams(UrlQueryStringParam.of("to",to.asString()),
					 							UrlQueryStringParam.of("loginId",loginId.asString())
					 							);
		}
		targetUrl  = authFactorValidationUrl
										.joinWith(qryStr);
		URI targetURI = UriBuilder.fromUri(targetUrl.asString())
											.build();

		log.warn("INFORMATIVE Purpose: END AFTER LOGIN. Login ID = {}. PENDING ON AUTH FACTOR (url: {})",
				 loginResponse.getLoginId(),
				 targetURI);

		return Response.seeOther(targetURI)
					   .header("x-r01-userpwd-pending-on-auth-factor",loginId)
					   .build();
	}
}
