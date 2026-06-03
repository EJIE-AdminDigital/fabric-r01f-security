package r01f.security.login.filter;

import jakarta.servlet.http.HttpServletRequest;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.jwt.JWT;

/**
 * Stores the [jwt] token created just after being logged in
 */
@Accessors(prefix="_")
@Slf4j
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class AfterLoginJWTStore {

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTANTS
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String JWT_PARAM  = "jwt";

/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	public static JWT getFromRequest(final HttpServletRequest request) {
		log.trace("Get JWT from request param");
		JWT jwt = (request.getParameter(JWT_PARAM) != null) ? JWT.forValue(request.getParameter(JWT_PARAM))
															: null;
		if (jwt == null) {
			log.info("No JWT as request parameter, try to get it from a request header");
			jwt = (request.getHeader(JWT_PARAM) != null) ? JWT.forValue(request.getHeader(JWT_PARAM))
														 : null;
			if (jwt == null) {
				log.info("...neither jwt at request header");
			} else {
				log.info("...JWT found at request header");
			}
		} else {
			log.info(" JWT is present as a request parameter ");
		}
		return jwt;
	}
}
