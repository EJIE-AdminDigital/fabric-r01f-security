package r01f.security.login.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import jakarta.inject.Provider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.entry.CRUDServicesForUserLoginEntry;
import r01f.api.interfaces.security.user.CRUDServicesForUser;
import r01f.debug.Debuggable;
import r01f.guids.CommonOIDs.WebSessionID;
import r01f.jwt.JWT;
import r01f.jwt.JWTDecoded;
import r01f.jwt.JWTDecoded.Payload;
import r01f.jwt.parser.JWTParser;
import r01f.jwt.parser.JWTParserBuilder;
import r01f.jwt.verifier.JWTVerificationResult;
import r01f.jwt.verifier.JWTVerifier;
import r01f.jwt.verifier.JWTVerifierBuilder;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.login.entry.UserLoginEntry;
import r01f.model.security.user.User;
import r01f.objectstreamer.Marshaller;
import r01f.security.context.SecurityContextStore;
import r01f.security.login.LoginSession;
import r01f.security.login.LoginSessionStore;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.UserLoginEntryOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.util.types.Strings;

/**
 * A [login session] servlet filter that supports [login impersonation]
 *
 * The UI requesting the impersonation makes a REST request with:
 * 		- The impersonating user oid > the user requesting the impersonation > usually an admin user
 * 		- The impersonated user oid > the user being impersonated
 * This method:
 * 		1.- Generates a RANDOM [security token]
 * 		2.- Creates in [impersonation login entry] that contains the previously-generated RANDOM [security token]
 *  	3.- Generates a [impersonation JWT] that contains:
 *  			a) the impersonating user oid
 *  			b) the impersonated user oid
 *  		The [impersonation JWT] is SIGNED with the previously-generated RANDOM [security token]
 * 		4.- Return
 * 				a) The [impersonation JWT]
 * 				b) the [impersonation login entry oid] (seed)
 *
 * Later, when requesting anything on behalf another user (impersonating another user) the UI MUST:
 * 		- include a [request parameter] named [impersonateJwt] that contains the generated [impersonation JWT]
 * 		- include a [request parameter] or a COOKIE named [impersonateToken] that contains the [impersonation login entry oid]
 *
 * At the [login filter] (see MT01UsersLoginServletFilter)
 * 		1.- Check if the [impersonateJwt] is present; if so, it's an impersonation request
 * 		2.- Load the [impersonation login entry] using the oid that comes at the [JWT], a [request parameter] or a COOKIE named [impersonateToken]
 * 		3.- Get the RANDOM [security token] at the [impersonation login entry] and use it to VERIFY the received [impersonation JWT]
 * 			(remember that the [impersonation JWT] was signed using the RANDOM [security token])
 * 		4.- Once the [impersonation JWT] is verified, extract the [impersonating user oid] and the [impersonated user oid]
 * 		5.- Check the users:
 * 				a) the [impersonating user] must be the logged user
 * 				b) the [impersonated user] must exist
 * 		6.- Replace the [login session] and [security context] by a NEW one where the user is the one being impersonated
 * @param <U>
 * @param <E>
 */
@Slf4j
public abstract class LoginSessionServletFilterWithImpersonationSupport<U extends User,E extends UserLoginEntry>
	 		  extends LoginSessionServletFilter<U> {
/////////////////////////////////////////////////////////////////////////////////////////
// 	CONSTANTS
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String IMPERSONATION_JWT_PARAM = "impersonateJwt";
	public static final String IMPERSONATION_LOGIN_ENTRY_OID_PARAM = "impersonateToken";
/////////////////////////////////////////////////////////////////////////////////////////
// 	SERVICES
/////////////////////////////////////////////////////////////////////////////////////////
	private final Provider<SecurityContext> _sysSecurityContextProvider;

	private final CRUDServicesForUser<U> _userCrud;
	private final CRUDServicesForUserLoginEntry<E> _loginEntryCrud;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginSessionServletFilterWithImpersonationSupport(final Class<U> userType,
															 final Marshaller marshaller,
															 final SecurityLoginFilterConfig loginFilterConfig,
															 // this filter-specific params
														     final Provider<SecurityContext> sysSecurityContextProvider,
														     final CRUDServicesForUser<U> userCrud,
														     final CRUDServicesForUserLoginEntry<E> loginEntryCrud) {
		this(userType,
		marshaller,
		loginFilterConfig,
		(SecurityProviderSessionFromRequestProvider) null,   //  No SecurityProviderSessionFromRequestProvider
		(Predicate<ServletRequest>)null,		              // notFilteredResourcePredicate
		// this filter-specific params
		sysSecurityContextProvider,
		userCrud,loginEntryCrud);
	}
	public LoginSessionServletFilterWithImpersonationSupport(final Class<U> userType,
							  		 						 final Marshaller marshaller,
							  		 						 final SecurityLoginFilterConfig loginFilterConfig,
							  		 						 final SecurityProviderSessionFromRequestProvider secProviderSessionFromRequestProvider,
							  		 						 // this filter-specific params
															 final Provider<SecurityContext> sysSecurityContextProvider,
															 final CRUDServicesForUser<U> userCrud,
															 final CRUDServicesForUserLoginEntry<E> loginEntryCrud) {
		this(userType,
			 marshaller,
			 loginFilterConfig,
			 secProviderSessionFromRequestProvider,   //  SecurityProviderSessionFromRequestProvider
			 (Predicate<ServletRequest>)null,		   // notFilteredResourcePredicate
			 // this filter-specific params
			 sysSecurityContextProvider,
			 userCrud,loginEntryCrud);
	}
	public LoginSessionServletFilterWithImpersonationSupport(final Class<U> userType,
							  		 						 final Marshaller marshaller,
							  		 						 final SecurityLoginFilterConfig loginFilterConfig,
							  		 						 final Collection<Pattern> notFilteredResourcesPatterns,
							  		 						 // this filter-specific params
															 final Provider<SecurityContext> sysSecurityContextProvider,
															 final CRUDServicesForUser<U> userCrud,
															 final CRUDServicesForUserLoginEntry<E> loginEntryCrud) {
		this(userType,
			 marshaller,
			 loginFilterConfig,null,
			 // create a predicate using the filtered resources patterns
			 _createServletRequestPredicateFrom(notFilteredResourcesPatterns),
			 // this filter-specific params
			 sysSecurityContextProvider,
			 userCrud,loginEntryCrud);
	}
	public LoginSessionServletFilterWithImpersonationSupport(final Class<U> userType,
															 final Marshaller marshaller,
															 final SecurityLoginFilterConfig securityAppClientConfig,
															 final SecurityProviderSessionFromRequestProvider securityProviderSessionFromRequestProvider,
															 final Predicate<ServletRequest> notFilteredResourcePredicate,
															 // this filter-specific params
															 final Provider<SecurityContext> sysSecurityContextProvider,
															 final CRUDServicesForUser<U> userCrud,
															 final CRUDServicesForUserLoginEntry<E> loginEntryCrud) {

		super(userType,
		      marshaller,
		      securityAppClientConfig,
		      securityProviderSessionFromRequestProvider,
		      notFilteredResourcePredicate);
		_sysSecurityContextProvider = sysSecurityContextProvider;
		_userCrud = userCrud;
		_loginEntryCrud = loginEntryCrud;
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	DO FILTER
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void doFilter(final ServletRequest req,final ServletResponse res,
						 final FilterChain chain) throws IOException,
														 ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;

		// [0] - Check if it's an [impersonation request]
		JWT jwt = _impersonationJWTTokenFromRequest(request);
		if (jwt == null) {
			// NOT an impersonation request
			super.doFilter(request,response,
						   chain);
			return;
		}
		log.warn("[login filter]: Impersonation request....");

		// [1] -  When the [impersonation] was pushed, an special [login entry] at the [db] was created
		// 		  The [login entry oid] is received at the [impersonation jwt]
		E popedImpersonationLoginEntry = _popImpersonationLoginEntry(request);
		if (popedImpersonationLoginEntry == null) {
			// NOT a VALID impersonation request
			super.doFilter(request,response,
						   chain);
			return;
		}
		// [2] - check if the JWT is valid
		// 		 Verify the JWT token: use a [random key] stored at the [impersonation login entry] created when the [impersonation login entry]
		//		 was pushed (see REST services)
		boolean jwtValid = _isValidImpersonationJWT(jwt,popedImpersonationLoginEntry);
		if (!jwtValid) {
			// NOT a VALID impersonation request
			super.doFilter(request,response,
						   chain);
			return;
		}
		// [3] - Decode the JWT
		//		The JWT token contains
		//			- the impersonatingUserOid > the user who is impersonating on behalf another user
		//			- the impersonatedUserOid  > the user which is being impersonated
		JWTParser parserJWT = JWTParserBuilder.createParser()
											  .build();
		JWTDecoded decodedJWT = parserJWT.parse(jwt);

		// [3] - Get the [impersonation actors]: impersonating & impersonated
		LoginImpersonationActors impersonationActors = _getImpersonationActorsFromJWT(decodedJWT);
		log.warn(" LoginImpersonationActors Impersonating {}, Impersonated {}",
				                               impersonationActors.getImpersonatingUserOid(),
				                               impersonationActors.getImpersontatedUserOid());
		if (!impersonationActors.isValid()) {
			// NOT a VALID impersonation request
			super.doFilter(request,response,
						   chain);
			return;
		}
		// [4] - Load the [impersonated user]
		CRUDResult<U> impersonatedUserLoad = _userCrud.load(_sysSecurityContextProvider.get(),
																		  impersonationActors.getImpersontatedUserOid());
		// [5] - Check the actors
		boolean impersonatingUserValid = _checkImpersonatingUser(impersonationActors.getImpersonatingUserOid(),
																 popedImpersonationLoginEntry);						// Check that the [impersonating user] is really the one stored at the [login entry]
		boolean impersonatedUserValid = _checkImpersonatedUser(impersonatedUserLoad,
															   popedImpersonationLoginEntry);						// check that an [impersonated user] is received and it really exists
		if (!impersonatingUserValid
		 || !impersonatedUserValid) {
			// NOT a VALID impersonation request
			super.doFilter(request,response,
						   chain);
			return;
		}

		// [6] - Get the impersonated user
		U impersonatedUser = impersonatedUserLoad.getOrThrow();	// get the impersonated user

		// [7] - Finally create an impersonated [login session] and [security context]
		 //    Warn! This is important, the SecurityProviderID & LoginID will be taken from previously created LoginEntry
		// ... because otherwise there is no SecurityProviderID or LoginID available in theis impersonation filter.
		LoginSession<U> impersonatedSession = new LoginSession<>(WebSessionID.forId(request.getSession(true).getId()),	// get the web session id
				                                                  popedImpersonationLoginEntry.getSecurityProviderId(),
				                                                  popedImpersonationLoginEntry.getLoginId(), // currentSession.getLoginId(),	// the impersonating login
															   	  impersonatedUser);													// BUT about the impersonated user
		SecurityContext securityContext = this.createSecurityContextFromLoginSession(impersonatedSession);

		// [8] - Store both the [login session] and the [security context]
		LoginSessionStore.storeAtHttpSession(request,
							 	 			 impersonatedSession);
		SecurityContextStore.storeAtHttpSession(request,
												securityContext);

		// [99] - Redirect to the original url WITHOUT the JWT param
		WrappedHttpRequestRemovingJWTParams wrapedRequest = new WrappedHttpRequestRemovingJWTParams(request,IMPERSONATION_JWT_PARAM);	// remove the JWT param
		response.sendRedirect(wrapedRequest.getRequestURI());
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	VALIDATION METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	private static JWT _impersonationJWTTokenFromRequest(final HttpServletRequest request) {
		String jwtAsString = request.getParameter(IMPERSONATION_JWT_PARAM);
		if (Strings.isNullOrEmpty(jwtAsString)) return null;		// not an impersonation request

		JWT jwt = JWT.forValue(jwtAsString);
		return jwt;
	}
	private E _popImpersonationLoginEntry(final HttpServletRequest request) {
		// When the [impersonation] was pushed, an special [login entry] at the [db] was created
		// The [login entry oid] is received in a [cookie] or in a [request parameter]

		// [1] - get the [user login entry oid] from the cookie
		UserLoginEntryOID userLoginEntryOid = null;

		// a) try a request param
		String impersonationLoginEntryOidStr = request.getParameter(IMPERSONATION_LOGIN_ENTRY_OID_PARAM);
		userLoginEntryOid = Strings.isNOTNullOrEmpty(impersonationLoginEntryOidStr) ? UserLoginEntryOID.forId(impersonationLoginEntryOidStr) : null;	// the request parameter contains the [user login entry oid]

		// b) try a cookie
		if (userLoginEntryOid == null) {
		Cookie[] cookies = request.getCookies();
			for (final Cookie cookie : cookies) {
				if (IMPERSONATION_LOGIN_ENTRY_OID_PARAM.equals(cookie.getName())) {
					userLoginEntryOid = Strings.isNOTNullOrEmpty(cookie.getValue()) ? UserLoginEntryOID.forId(cookie.getValue()) : null;	// the cookie contains the [user login entry oid]
					cookie.setMaxAge(0); 	// remove the cookie
					break;
				}
			}
		}
		// c) check
		if (userLoginEntryOid == null) {
			log.warn("\t-A request parameter or a cookie named={} containing the [user login entry oid] was NOT received or it's value is null!!",
					 IMPERSONATION_LOGIN_ENTRY_OID_PARAM);
			return null;
		}
		log.warn("\t-[user login entry oid]={} contained at the cookie named={}",
				 userLoginEntryOid,IMPERSONATION_LOGIN_ENTRY_OID_PARAM);

		// [2] - Load the previously pushed [impersonation login entry]
		CRUDResult<E> pushedImpersonationLoginEntryLoad = _loginEntryCrud.popImpersonationLogin(_sysSecurityContextProvider.get(),
															 	  							 				 userLoginEntryOid);
		if (pushedImpersonationLoginEntryLoad.hasFailed()) {
			log.warn("\t-the [user login entry oid]={} was NOT found or could NOT be loaded: {}",
					 userLoginEntryOid,
					 pushedImpersonationLoginEntryLoad.asCRUDError().getDetailedMessage());
			return null;
		}

		// [99] - return the [impersonation login entry]
		return pushedImpersonationLoginEntryLoad.getOrThrow();
	}
	private boolean _isValidImpersonationJWT(final JWT jwt,final E pushedImpersonationLoginEntry) {
		// Verify the JWT token: use a [random key] stored at the [impersonation login entry] created
		// when the [impersonation login entry] was pushed (see REST services)
		JWTVerifier jwtVerfier = JWTVerifierBuilder.createVerifier()
														.usingKey(pushedImpersonationLoginEntry.getToken().getBytes())
														.checkingExpiration()
														.forAudience("met01.impersonalization")
													.build();
		JWTVerificationResult verificationResult = jwtVerfier.verify(jwt);
		boolean outValid = verificationResult.isValidJWT();

		log.info("\t-impersonation JWT is  {}",outValid ? "VALID" : "NOT VALID");
		return outValid;
	}
	private LoginImpersonationActors _getImpersonationActorsFromJWT(final JWTDecoded decodedJWT) {
		Payload payload = decodedJWT.getPayload();
		UserOID impersonatingUserOid = UserOID.forId(payload.getCustomClaim("impersonatingUserOid").getValue());
		UserOID impersonatedUser = UserOID.forId(payload.getCustomClaim("impersonatedUserOid").getValue());

		LoginImpersonationActors outActors = new LoginImpersonationActors(impersonatingUserOid,impersonatedUser);
		log.info("\t-impersonation actors > {}",outActors.debugInfo());
		return outActors;
	}
	private boolean _checkImpersonatingUser(final UserOID impersonatingUserOid,
										    final E popedImpersonationLoginEntry) {
		// Check that the [impersonating user] is really the one stored at the [login entry]
		if (impersonatingUserOid.isNOT(popedImpersonationLoginEntry.getUserOid())) {
			log.warn("\t-The [impersonating user] received at the [jwt token] {} does NOT match the one previously pushed at the [login entries] table: {}",
					 impersonatingUserOid,
					 popedImpersonationLoginEntry.getUserOid());
			return false;
		}
		return true;
	}
	private boolean _checkImpersonatedUser(final CRUDResult<U> impersonatedUserLoad,
										   final E popedImpersonationLoginEntry) {

		// check that an [impersonated user] is received and it really exists
		boolean impersonatedUserNotExists = impersonatedUserLoad.hasFailed()
									  &&    impersonatedUserLoad.asCRUDError()
									  						 .wasBecauseClientRequestedEntityWasNOTFound();
		if (impersonatedUserNotExists ) {
			log.warn("\t-The [impersonated user] received at the [jwt token] could NOT be loaded: {}",
					 popedImpersonationLoginEntry.getImpersonatedUserOid());
			return false;
		}
		return true; 	// valid
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Accessors(prefix="_")
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	private class LoginImpersonationActors
	   implements Debuggable {
		@Getter private final UserOID _impersonatingUserOid;
		@Getter private final UserOID _impersontatedUserOid;

		public boolean isValid() {
			return _impersonatingUserOid != null
				&& _impersontatedUserOid != null;
		}
		@Override
		public CharSequence debugInfo() {
			return Strings.customized("impersonating userOid={} / impersonated userOid={}",
									  _impersonatingUserOid,_impersontatedUserOid);
		}
	}
}