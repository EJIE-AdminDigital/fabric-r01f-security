package r01f.security.handler.usrpwd;

import java.net.URI;
import java.util.HashMap;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import lombok.extern.slf4j.Slf4j;
import r01f.jwt.JWT;
import r01f.model.security.login.userpassword.UserPasswordLoginAuthFactorConfig;
import r01f.model.security.login.userpassword.UserPasswordLoginErrorType;
import r01f.model.security.login.userpassword.UserPasswordLoginResponse;
import r01f.model.security.login.userpassword.UserPasswordLoginResponseOK;
import r01f.model.security.login.userpassword.UserPasswordLoginResponsePendingOnAuthFactor;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorUsingOTPBase;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorUsingOTPByEMail;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorUsingOTPBySMS;
import r01f.security.handler.CallBackOnLoginHandlerBase;
import r01f.security.provider.SecurityProvider;
import r01f.security.provider.SecurityProviderSecretConfig;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityOIDs.UserOID;
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
public abstract class UserPasswordCallBackOnLoginHandlerBase
     extends CallBackOnLoginHandlerBase {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public UserPasswordCallBackOnLoginHandlerBase(final SecurityProviderSecretConfig securityProviderSecretConfig) {
		super(securityProviderSecretConfig);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  METHODS TO IMPLEMENT
/////////////////////////////////////////////////////////////////////////////////////////
	public abstract Response loginCallBack(final UserPasswordLoginResponse loginResponse,
										   final Url to);
/////////////////////////////////////////////////////////////////////////////////////////
//  PROTECTED METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	protected Response _loginCallBackOnLoginResponseSuccess(final UserPasswordLoginResponse loginResponse,
															final Url to) {
		if ( ! loginResponse.hasSucceeded())
				throw new IllegalArgumentException("This method should only be called when the login has been successful");
		Url targetUrl = null;
		// The login & password was correct
		log.info("user & password login for loginId={} OK!",
				 loginResponse.getLoginId());

		// a) Create a jwt token
		UserOID  userOid = loginResponse.as(UserPasswordLoginResponseOK.class)
										.getUserOid();
		LoginID loginId = loginResponse.as(UserPasswordLoginResponseOK.class)
										.getLoginId();

		@SuppressWarnings("serial")
		JWT jwt = _createJWT(_securityProviderSecretConfig.getKeyData().asString(),
							 SecurityProvider.USER_PASSWORD.getCode(),
							 loginId,
							 new HashMap<String,String>() {{
								 		put(JWT_CUSTOM_CLAIM_FOR_USER_OID,userOid.asString());
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
		if ( ! loginResponse.hasFailed())
					throw new IllegalArgumentException("This method should only be called when the login has failed");
		Url targetUrl = null;
		// The login & password is NOT correct, redir again to the login url
		UserPasswordLoginErrorType errorType = loginResponse.asResponseNOK()
														    .getErrorType();
		log.info("user & password login for loginId={} NOK! > {}",
				 loginResponse.getLoginId(),
				 errorType);

		switch (errorType) {
				case INVALID_USER_CODE:
				case PASSWORD_ERROR:
					// redir to login url again
					targetUrl = _securityProviderSecretConfig.getLoginUrl();
					log.info("USER: {} >>> user-entered password is NOT correct: redir again to the login url: {}",
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
				case USER_NOT_CONFIRMED:
					// redir to login url again
					targetUrl = _securityProviderSecretConfig.getLoginUrl();
					log.info("USER: {} >>> user account is not confirmed: redir again to the login url: {}",
							 loginResponse.getLoginId(),
							 targetUrl);
					break;
				case AUTH_FACTOR_PHONE_OR_MAIL_NOT_REGISTRED:
					// redir to login url again
					targetUrl = _securityProviderSecretConfig.getLoginUrl();
					log.warn("USER: {} >>> auth factor, phone ore mail not registred: {}",
							 loginResponse.getLoginId(),
							 targetUrl);
					break;
				case AUTH_FACTOR_SECRET_ERROR:
					// redir to login url again
					targetUrl = _securityProviderSecretConfig.getLoginUrl();
					log.warn("USER: {} >>> auth factor, SECRET error: {}",
							 loginResponse.getLoginId(),
							 targetUrl);
					break;
				default:
					throw new IllegalStateException(loginResponse.getLoginId() + 
													" USER >>> (" + 
													errorType + 
													") is NOT a supported login response error!!");
				}

		// [99] - Redir
		URI targetURI = UriBuilder.fromUri(targetUrl.asString())
									.build();
		
		log.warn("INFORMATIVE Purpose: END AFTER LOGIN. Login ID = {}. FAILED ({})",
				 loginResponse.getLoginId(),
				 errorType);

		return  Response.seeOther(targetURI)
							.header("x-r01-userpwd-error",loginResponse.asResponseNOK()
																	   .getErrorType())
							.build();

	}

	protected Response  _loginCallBackOnLoginResponsePendingOnAuthFactor(final UserPasswordLoginResponse loginResponse,
																		 final Url to) {
		if ( ! loginResponse.isPendingOnAuthFactor())
				throw new IllegalArgumentException("This method should only be called when the login is pending on an auth factor");

		UserPasswordLoginAuthFactorConfig authFactorConfig =  UserPasswordLoginAuthFactorConfig.createFrom(_securityProviderSecretConfig.getProperties());

		Url targetUrl = null;

		log.info(" Login was ok, but is pending on auth factor, must request to the user ({}) a secret data, otp, etc.", loginResponse.getLoginId());

		String authFactorValidationUriProp = "auth_factors_validationUrl";
		UserPasswordLoginResponsePendingOnAuthFactor<?> loginResponsePendingOn = loginResponse.as(UserPasswordLoginResponsePendingOnAuthFactor.class);
		LoginID loginId = loginResponsePendingOn.getLoginId();
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
		if ( loginResponsePendingOn.getAuthFactor().isByOTP() ) {
			LoginAuthFactorUsingOTPBase authFactorByOTP = loginResponsePendingOn.getAuthFactor().as(LoginAuthFactorUsingOTPBase.class);
			if ( authFactorByOTP.isByMail()) {
				qryStr = UrlQueryString.fromParams(UrlQueryStringParam.of("to",to.asString()),
												   UrlQueryStringParam.of("loginId",loginId.asString()),
												   UrlQueryStringParam.of("email",authFactorByOTP.as(LoginAuthFactorUsingOTPByEMail.class).getMail().asString()));


			} else if ( authFactorByOTP.isBySms()) {
				qryStr = UrlQueryString.fromParams(UrlQueryStringParam.of("to",to.asString()),
												   UrlQueryStringParam.of("loginId",loginId.asString()),
												   UrlQueryStringParam.of("phone",authFactorByOTP.as(LoginAuthFactorUsingOTPBySMS.class).getPhone().asString()));

			} else throw new IllegalArgumentException(" Not valid OTP type :" + authFactorByOTP.getClass());
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
