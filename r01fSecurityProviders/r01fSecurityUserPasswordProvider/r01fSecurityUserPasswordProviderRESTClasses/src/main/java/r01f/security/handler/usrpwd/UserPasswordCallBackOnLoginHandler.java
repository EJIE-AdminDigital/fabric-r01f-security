package r01f.security.handler.usrpwd;

import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import r01f.model.security.login.userpassword.UserPasswordLoginResponse;
import r01f.security.provider.SecurityProviderSecretConfig;
import r01f.types.url.Url;

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
     extends UserPasswordCallBackOnLoginHandlerBase {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public UserPasswordCallBackOnLoginHandler(final SecurityProviderSecretConfig securityProviderSecretConfig) {
		super(securityProviderSecretConfig);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
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
		else if (loginResponse.isPendingOnAuthFactor()) {
			return _loginCallBackOnLoginResponsePendingOnAuthFactor(loginResponse, to);
		}
		throw new IllegalStateException(" Not valid LoginResponse type : " + loginResponse.getClass());
	}
}
