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
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.principal.CRUDServicesForPrincipal;
import r01f.api.interfaces.security.principal.impersonation.CRUDServicesForPrincipalImpersonation;
import r01f.debug.Debuggable;
import r01f.jwt.JWT;
import r01f.jwt.JWTDecoded;
import r01f.jwt.JWTDecoded.Payload;
import r01f.jwt.parser.JWTParser;
import r01f.jwt.parser.JWTParserBuilder;
import r01f.jwt.verifier.JWTVerificationResult;
import r01f.jwt.verifier.JWTVerifier;
import r01f.jwt.verifier.JWTVerifierBuilder;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.impersonation.SecurityPrincipalImpersonation;
import r01f.objectstreamer.Marshaller;
import r01f.oids.CommonOIDs.WebSessionID;
import r01f.oids.SecurityOIDs.SecurityPrincipalImpersonationOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.security.context.SecurityContextStore;
import r01f.security.login.LoginSession;
import r01f.security.login.LoginSessionStore;
import r01f.util.types.Strings;

/**
 * A [login session] servlet filter that supports [security principal impersonation]
 *
 * The UI requesting the impersonation makes a REST request with:
 * 		- The impersonating [principal] oid > the [principal] requesting the impersonation > usually an admin user
 * 		- The impersonated [principal] oid > the user being impersonated
 * 
 * This method:
 * 		1.- Generates a RANDOM [security token]
 * 		2.- Creates in [impersonation] record that contains the previously-generated RANDOM [security token]
 *  	3.- Generates a [impersonation JWT] that contains:
 *  			a) the impersonating [principal] oid
 *  			b) the impersonated [principal] oid
 *  		The [impersonation JWT] is SIGNED with the previously-generated RANDOM [security token]
 * 		4.- Return
 * 				a) The [impersonation JWT]
 * 				b) the [impersonation] record oid (seed)
 *
 * Later, when requesting anything on behalf another [principal] (impersonating another [principal]) the UI MUST:
 * 		- include a [request parameter] named [impersonateJwt] that contains the generated [impersonation JWT]
 * 		- include a [request parameter] or a COOKIE named [impersonateToken] that contains the [impersonation] record oid
 *
 * At the [login filter] 
 * 		1.- Check if the [impersonateJwt] is present; if so, it's an [impersonation] request
 * 		2.- Load the [impersonation] record using the oid that comes at the [JWT], a [request parameter] or a COOKIE named [impersonateToken]
 * 		3.- Get the RANDOM [security token] at the [impersonation] record and use it to VERIFY the received [impersonation JWT]
 * 			(remember that the [impersonation JWT] was signed using the RANDOM [security token])
 * 		4.- Once the [impersonation JWT] is verified, extract the impersonating [principal] oid and the impersonated [principal] oid
 * 		5.- Check the [principal]:
 * 				a) the impersonating [principal] must be the logged [principal]
 * 				b) the impersonated [principal] must exist
 * 		6.- Replace the [login session] and [security context] by a NEW one where the [principal] is the one being impersonated
 * @param <P>
 * @param <E>
 */
@Slf4j
public abstract class LoginSessionServletFilterWithImpersonationSupport<P extends IsSecurityPrincipal>
	 		  extends LoginSessionServletFilter<P> {
/////////////////////////////////////////////////////////////////////////////////////////
// 	CONSTANTS
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String IMPERSONATION_JWT_PARAM = "impersonateJwt";
	public static final String IMPERSONATION_OID_PARAM = "impersonationOid";
	
	public static final String IMPERSONATION_JWT_AUDIENCE = "r01f.impersonalization";
	public static final String IMPERSONATION_JWT_IMPERSONATED_OID_CLAIM = "impersonatedPrincipalOid";
	public static final String IMPERSONATION_JWT_IMPERSONATING_OID_CLAIM = "impersonatingPrincipalOid";
/////////////////////////////////////////////////////////////////////////////////////////
// 	SERVICES
/////////////////////////////////////////////////////////////////////////////////////////
	private final Provider<SecurityContext> _sysSecurityContextProvider;

	private final CRUDServicesForPrincipal<P> _principalCrud;
	private final CRUDServicesForPrincipalImpersonation _impersonationCrud;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginSessionServletFilterWithImpersonationSupport(final Class<P> userType,
															 final Marshaller marshaller,
															 final SecurityLoginFilterConfig loginFilterConfig,
															 // this filter-specific params
														     final Provider<SecurityContext> sysSecurityContextProvider,
														     final CRUDServicesForPrincipal<P> principalCrud,
														     final CRUDServicesForPrincipalImpersonation impersonationCrud) {
		this(userType,
			 marshaller,
			 loginFilterConfig,
			 (SecurityProviderSessionFromRequestProvider) null,   //  No SecurityProviderSessionFromRequestProvider
			 (Predicate<ServletRequest>)null,		              // notFilteredResourcePredicate
			 // this filter-specific params
			 sysSecurityContextProvider,
			 principalCrud,impersonationCrud);
	}
	public LoginSessionServletFilterWithImpersonationSupport(final Class<P> userType,
							  		 						 final Marshaller marshaller,
							  		 						 final SecurityLoginFilterConfig loginFilterConfig,
							  		 						 final SecurityProviderSessionFromRequestProvider secProviderSessionFromRequestProvider,
							  		 						 // this filter-specific params
															 final Provider<SecurityContext> sysSecurityContextProvider,
															 final CRUDServicesForPrincipal<P> principalCrud,
															 final CRUDServicesForPrincipalImpersonation impersonationCrud) {
		this(userType,
			 marshaller,
			 loginFilterConfig,
			 secProviderSessionFromRequestProvider,   //  SecurityProviderSessionFromRequestProvider
			 (Predicate<ServletRequest>)null,		   // notFilteredResourcePredicate
			 // this filter-specific params
			 sysSecurityContextProvider,
			 principalCrud,impersonationCrud);
	}
	public LoginSessionServletFilterWithImpersonationSupport(final Class<P> userType,
							  		 						 final Marshaller marshaller,
							  		 						 final SecurityLoginFilterConfig loginFilterConfig,
							  		 						 final Collection<Pattern> notFilteredResourcesPatterns,
							  		 						 // this filter-specific params
															 final Provider<SecurityContext> sysSecurityContextProvider,
															 final CRUDServicesForPrincipal<P> principalCrud,
															 final CRUDServicesForPrincipalImpersonation impersonationCrud) {
		this(userType,
			 marshaller,
			 loginFilterConfig,null,
			 // create a predicate using the filtered resources patterns
			 _createServletRequestPredicateFrom(notFilteredResourcesPatterns),
			 // this filter-specific params
			 sysSecurityContextProvider,
			 principalCrud,impersonationCrud);
	}
	public LoginSessionServletFilterWithImpersonationSupport(final Class<P> userType,
															 final Marshaller marshaller,
															 final SecurityLoginFilterConfig securityAppClientConfig,
															 final SecurityProviderSessionFromRequestProvider securityProviderSessionFromRequestProvider,
															 final Predicate<ServletRequest> notFilteredResourcePredicate,
															 // this filter-specific params
															 final Provider<SecurityContext> sysSecurityContextProvider,
															 final CRUDServicesForPrincipal<P> principalCrud,
															 final CRUDServicesForPrincipalImpersonation impersonationCrud) {
		super(userType,
		      marshaller,
		      securityAppClientConfig,
		      securityProviderSessionFromRequestProvider,
		      notFilteredResourcePredicate);
		_sysSecurityContextProvider = sysSecurityContextProvider;
		_principalCrud = principalCrud;
		_impersonationCrud = impersonationCrud;
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

		// [1] -  When the [impersonation] was pushed, an [impersonation] record was created at the DB
		// 		  The [login entry oid] is received at the [impersonation jwt]
		SecurityPrincipalImpersonation existingImpersonation = _loadImpersonation(request);
		if (existingImpersonation == null) {
			// NOT a VALID impersonation request
			super.doFilter(request,response,
						   chain);
			return;
		}
		// [2] - check if the JWT is valid
		// 		 Verify the JWT token: use a [random key] stored at the [impersonation login entry] created when the [impersonation login entry]
		//		 was pushed (see REST services)
		boolean jwtValid = _isValidImpersonationJWT(jwt,existingImpersonation);
		if (!jwtValid) {
			// NOT a VALID impersonation request
			super.doFilter(request,response,
						   chain);
			return;
		}
		// [3] - Decode the JWT
		//		The JWT token contains
		//			- the impersonating [principal] oid > the [principal] who is impersonating on behalf another user
		//			- the impersonated [principal] oid  > the [principal] which is being impersonated
		JWTParser parserJWT = JWTParserBuilder.createParser()
											  .build();
		JWTDecoded decodedJWT = parserJWT.parse(jwt);

		// [3] - Get the [impersonation actors] from the JWT impersonating & impersonated
		SecurityPrincipalImpersonationActors impersonationActorsFromJWT = _getImpersonationActorsFromJWT(decodedJWT);
		log.warn("impersonation actors: {}",
				 impersonationActorsFromJWT.debugInfo());
		
		if (!impersonationActorsFromJWT.isValid()) {
			// NOT a VALID impersonation request
			super.doFilter(request,response,
						   chain);
			return;
		}
		// [4] - Load the impersonated [principal]
		CRUDResult<P> impersonatedLoad = _principalCrud.load(_sysSecurityContextProvider.get(),
															 impersonationActorsFromJWT.getImpersonatedPrincipalOid());
		// [5] - Check the actors
		boolean impersonatingValid = _checkImpersonatingPrincipal(impersonationActorsFromJWT.getImpersonatingPrincipalOid(),
																   existingImpersonation);	
		boolean impersonatedValid = _checkImpersonatedPrincipal(impersonatedLoad,
															    existingImpersonation);	
		if (!impersonatingValid
		 || !impersonatedValid) {
			// NOT a VALID impersonation request
			super.doFilter(request,response,
						   chain);
			return;
		}

		// [6] - Get the impersonated user
		P impersonated = impersonatedLoad.getOrThrow();	// get the impersonated [principal] 

		// [7] - Finally create an impersonated [login session] and [security context]
		 //      Warn! This is important, the SecurityProviderID & LoginID will be taken from previously created LoginEntry
		//       ... because otherwise there is no SecurityProviderID or LoginID available in theis impersonation filter.
		LoginSession<P> impersonatedSession = new LoginSession<>(WebSessionID.forId(request.getSession(true).getId()),	// get the web session id
				                                                 null,null,		// TODO where find [identity provider] and [authn provider]
															   	 impersonated,											// BUT about the impersonated [principal]
															   	 null);				// sys-wide roles not set at this point (they will be set later)
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
	private SecurityPrincipalImpersonation _loadImpersonation(final HttpServletRequest request) {
		// When the [impersonation] was pushed, an [impersonation was created at the DB
		// The [impersonation] OID is received in a [cookie] or in a [request parameter]

		// [1] - get the [impersonation] OID
		SecurityPrincipalImpersonationOID impersonationOid = null;

		// a) try a request param
		String impersonationOidStr = request.getParameter(IMPERSONATION_OID_PARAM);
		impersonationOid = Strings.isNOTNullOrEmpty(impersonationOidStr) ? SecurityPrincipalImpersonationOID.forId(impersonationOidStr) 
																		 : null;	// the request parameter contains the [impersonation] OID

		// b) try a cookie
		if (impersonationOid == null) {
			Cookie[] cookies = request.getCookies();
				for (Cookie cookie : cookies) {
					if (IMPERSONATION_OID_PARAM.equals(cookie.getName())) {
						impersonationOid = Strings.isNOTNullOrEmpty(cookie.getValue()) ? SecurityPrincipalImpersonationOID.forId(cookie.getValue()) 
																					   : null;	// the cookie contains the [impersonation] OID
						cookie.setMaxAge(0); 	// remove the cookie!!!!
						break;
					}
				}
		}
		// c) check
		if (impersonationOid == null) {
			log.warn("\t-A request parameter or a cookie named={} containing the [impersonation] oid was NOT received or it's value is null!!",
					 IMPERSONATION_OID_PARAM);
			return null;
		}
		log.warn("\t-[impersonation]={} contained at the cookie or request parameter named={}",
				 impersonationOid,IMPERSONATION_OID_PARAM);

		// [2] - Load the previously pushed [impersonation login entry]
		CRUDResult<SecurityPrincipalImpersonation> impersonationLoad = _impersonationCrud.load(_sysSecurityContextProvider.get(),
															 	  							   impersonationOid);
		if (impersonationLoad.hasFailed()) {
			log.error("\t-the [impersonation]={} was NOT found or could NOT be loaded: {}",
					 impersonationOid,
					 impersonationLoad.asCRUDError().getDetailedMessage(),impersonationLoad.asCRUDError().getError());
			return null;
		}
		// [99] - return the [impersonation login entry]
		return impersonationLoad.getOrThrow();
	}
	private boolean _isValidImpersonationJWT(final JWT jwt,
											 final SecurityPrincipalImpersonation existingImpersonation) {
		// Verify the JWT token: use a [random key] stored at the [impersonation login entry] created
		// when the [impersonation login entry] was pushed (see REST services)
		JWTVerifier jwtVerfier = JWTVerifierBuilder.createVerifier()
														.usingKey(existingImpersonation.getToken().getBytes())
														.checkingExpiration()
														.forAudience(IMPERSONATION_JWT_AUDIENCE)
													.build();
		JWTVerificationResult verificationResult = jwtVerfier.verify(jwt);
		boolean outValid = verificationResult.isValidJWT();

		log.info("\t-impersonation JWT is  {}",
				 outValid ? "VALID" : "NOT VALID");
		return outValid;
	}
	private SecurityPrincipalImpersonationActors _getImpersonationActorsFromJWT(final JWTDecoded decodedJWT) {
		Payload payload = decodedJWT.getPayload();
		SecurityPrincipalOID impersonatingUserOid = SecurityPrincipalOID.forId(payload.getCustomClaim(IMPERSONATION_JWT_IMPERSONATING_OID_CLAIM).getValue());
		SecurityPrincipalOID impersonatedUser = SecurityPrincipalOID.forId(payload.getCustomClaim(IMPERSONATION_JWT_IMPERSONATED_OID_CLAIM).getValue());

		SecurityPrincipalImpersonationActors outActors = new SecurityPrincipalImpersonationActors(impersonatedUser,impersonatingUserOid);
		log.info("\t-impersonation actors > {}",
				outActors.debugInfo());
		if (!outActors.isValid()) log.error("\t-the impersonation actors received from the JWT {} are NOT valid",
										    outActors.debugInfo());
		return outActors;
	}
	private boolean _checkImpersonatingPrincipal(final SecurityPrincipalOID impersonatingPrincipalOid,
										    	 final SecurityPrincipalImpersonation existingImpersonation) {
		// Check that the impersonating [principal] is really the one stored at the [impersonation] record
		if (impersonatingPrincipalOid.isNOT(existingImpersonation.getImpersonatingPrincipalOid())) {
			log.warn("\t-The impersonating [principal] received at the [jwt token] {} does NOT match the one previously stored at the [impersonation] table: {}",
					 impersonatingPrincipalOid,
					 existingImpersonation.getImpersonatingPrincipalOid());
			return false;
		}
		return true;
	}
	private boolean _checkImpersonatedPrincipal(final CRUDResult<P> impersonatedPrincipalLoad,
										   		final SecurityPrincipalImpersonation existingImpersonation) {

		// check that an impersonated [principal] is received and it really exists
		boolean impersonatedPrincipalNotExists = impersonatedPrincipalLoad.hasFailed()
									  	      && impersonatedPrincipalLoad.asCRUDError()
									  						 			   .wasBecauseClientRequestedEntityWasNOTFound();
		if (impersonatedPrincipalNotExists ) {
			log.warn("\t-The impersonated [principal] received at the [jwt token] could NOT be loaded: {}",
					 existingImpersonation.getImpersonatedPrincipalOid());
			return false;
		}
		return true; 	// valid
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	private record SecurityPrincipalImpersonationActors(@Getter SecurityPrincipalOID impersonatedPrincipalOid,
														@Getter SecurityPrincipalOID impersonatingPrincipalOid) 	
	   implements Debuggable {

		public boolean isValid() {
			return this.impersonatedPrincipalOid != null
				&& this.impersonatingPrincipalOid != null;
		}
		@Override
		public CharSequence debugInfo() {
			return Strings.customized("impersonating principal={} / impersonated principal={}",
									  this.impersonatingPrincipalOid,this.impersonatedPrincipalOid);
		}
	}
}