package r01f.security.accessgrantedtoken;

import java.io.Serializable;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Objects;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.nimbusds.jwt.SignedJWT;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.crypto.CryptoKeyFactoryFromSpec;
import r01f.crypto.CryptoKeyPair;
import r01f.crypto.CryptoKeySpecFactory;
import r01f.crypto.CryptoKeys;
import r01f.debug.Debuggable;
import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.jwt.JWT;
import r01f.jwt.JWTBuilder;
import r01f.jwt.verifier.JWTVerificationResult;
import r01f.jwt.verifier.JWTVerificationResultAsNotValidToken;
import r01f.jwt.verifier.JWTVerifier;
import r01f.jwt.verifier.JWTVerifierBuilder;
import r01f.types.Path;
import r01f.util.types.Strings;

/**
 * A service that generates JWT Tokens and also acts as verifier
 * It have an internal cache that avoids verification on "live" tokens
 * Usage:
 * <pre class='brush:java'>
 * 		AccessGrantedTokenSpec spec = new AccessGrantedTokenSpec(AppCode.forId("myAppCode"),AppComponent.forId("myAppComponent"),
 *												   			     60 * 30);	// token expiration = 30 min
 * 		CryptoKeyPair rsaKeyPair = CryptoKeyPairBuilder.createRSAKeyPair();
 *		AccessGrantedTokenService jwtService = new AccessGrantedTokenService(spec,
 *															   				 rsaKeyPair);
 *		// [1] - Generate a token
 *		AccessGrantedToken token1 = jwtService.generate();
 *		// ....
 *		// [2] - Verify
 *		AccessGrantedToken notVerifiedToken = jwtService.verifyJWT(token1.getJwt());
 * </pre>
 */
@Singleton
@Slf4j
@Accessors(prefix="_")
public class AccessGrantedTokenService
  implements Provider<AccessGrantedToken>,
			 Serializable {

	private static final long serialVersionUID = -1684189239284599814L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTANTS
/////////////////////////////////////////////////////////////////////////////////////////
	// Generate keys using: ApiKeyPairBuilder.createRSA()
	protected static final Path PRIVATE_KEY_CLASSPATH_LOCATION = Path.from("key/privateKey.txt");
	protected static final Path PUBLIC_KEY_CLASSPATH_LOCATION = Path.from("key/publicKey.txt");

/////////////////////////////////////////////////////////////////////////////////////////
//	CACHE
/////////////////////////////////////////////////////////////////////////////////////////
	private static final int JWT_CACHE_MAX_SIZE = 200;		// cache 200 jwts

	protected final JWTVerifier _jwtVerifier;
	protected final LoadingCache<JWT,CachedAccessGrantedToken> _jwtCache;

	@Accessors(prefix="_")
	private class CachedAccessGrantedToken
	   implements Debuggable {
		////////// Mutable state
		@Getter @Setter private Instant _lastAccessedAt;

		////////// Immutable state
		@Getter private final Instant _cachedAt;
		@Getter private final Instant _jwtIssuedAt;
		@Getter private final Instant _jwtExpiresAt;
		@Getter private final boolean _jwtValid;

		public CachedAccessGrantedToken(final Instant cacheDate,final Instant expireDate) {
			this(cacheDate,cacheDate,expireDate,true);
		}
		public CachedAccessGrantedToken(final Instant cacheDate,final Instant issueDate,final Instant expireDate,final boolean valid) {
			_cachedAt = cacheDate;
			_jwtIssuedAt = issueDate;
			_jwtExpiresAt = expireDate;
			_jwtValid = valid;

			_lastAccessedAt = cacheDate;
		}

		public boolean needsToBeVerified() {
			if (!_jwtValid) return false;	// if it was cached as NOT valid, it'll still be NOT valid

			// If the expiration time has passed, it needs to be re-validated
			Instant now = Instant.now();
			return _jwtExpiresAt != null ? now.isAfter(_jwtExpiresAt) : true;
		}
		@Override
		public CharSequence debugInfo() {
			return Strings.customized("jwtIssuedAt={} jwtExpiresAt={} jwtValid={} (cachedAt={} last accessed at={})",
					 				  _jwtIssuedAt,_jwtExpiresAt,_jwtValid,
					 				  _cachedAt,_lastAccessedAt);
		}
		@Override
		public boolean equals(final Object o) {
			if (this == o) return true;
			if (o == null) return false;
			if (!(o instanceof CachedAccessGrantedToken other)) return false;
			boolean cachedAtEq = this.getCachedAt() != null && other.getCachedAt() != null ? this.getCachedAt().equals(other.getCachedAt())
																						   : this.getCachedAt() != null ? false : true;
			boolean issuedAtEq = this.getJwtIssuedAt() != null && other.getJwtIssuedAt() != null ? this.getJwtIssuedAt().equals(other.getJwtIssuedAt())
																								 : this.getJwtIssuedAt() != null ? false : true;
			boolean expiresAtEq = this.getJwtExpiresAt() != null && other.getJwtExpiresAt() != null ? this.getJwtExpiresAt().equals(other.getJwtExpiresAt())
																									: this.getJwtExpiresAt() != null ? false : true;
			boolean validEq = this.isJwtValid() == other.isJwtValid();
			return cachedAtEq && issuedAtEq && expiresAtEq && validEq;
		}
		@Override
		public int hashCode() {
			return Objects.hashCode(_cachedAt,_jwtIssuedAt,_jwtExpiresAt,_jwtValid);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	private class ValidAccessGrantedTokenCacheLoader
		  extends CacheLoader<JWT,CachedAccessGrantedToken> {

			@Override
			public CachedAccessGrantedToken load(final JWT jwt) { // no checked exception
				return _doParseJWT(jwt);
			}
			@Override
			public ListenableFuture<CachedAccessGrantedToken> reload(final JWT jwt,
														  	  		 final CachedAccessGrantedToken prevValue) {
				// [1] - Get the time when the [jwt] was generated
				if (log.isInfoEnabled()) log.info("[access token verifier cache]: verify [jwt] ({})..................",
						 prevValue.debugInfo());

				// [2] - Return a future
				if (prevValue.needsToBeVerified()) {
					if (log.isInfoEnabled()) log.info("... the [jwt] {} needs to be verified since it migh have expired (the updated value will be available the NEXT time it's requested!)",
							 prevValue.debugInfo());
					ListenableFutureTask<CachedAccessGrantedToken> task = ListenableFutureTask.create(() -> _doParseJWT(jwt));	// callable

					// load in a new thread > ASYNC (the data will be available next time the jwt is requested)
					Executors.newSingleThreadExecutor()
						 	 .execute(task);
					return task;
				}
				// return a "fake" future with the previous value
				if (log.isDebugEnabled()) log.debug("... the [jwt] DOES NOT need to be verified since it has NOT expired");
				return Futures.immediateFuture(prevValue);
			}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	VERIFY
/////////////////////////////////////////////////////////////////////////////////////////
	@SneakyThrows
	protected CachedAccessGrantedToken _doParseJWT(final JWT jwt)  {
		// get data from JWT
		SignedJWT signedJWT = SignedJWT.parse(jwt.asString());
		Instant jwtIssuedAt = signedJWT.getJWTClaimsSet().getIssueTime() != null ? signedJWT.getJWTClaimsSet().getIssueTime().toInstant()
																				 : Instant.now();
		Instant jwtExpiresAt = signedJWT.getJWTClaimsSet().getExpirationTime() != null ? signedJWT.getJWTClaimsSet().getExpirationTime().toInstant()
																			   		   : Instant.now();
		JWTVerificationResult verficationResult = _jwtVerifier.verify(jwt);
		boolean jwtValid = verficationResult.isValidJWT();
		if (verficationResult instanceof JWTVerificationResultAsNotValidToken) {

			if (log.isInfoEnabled()) log.info("[access token verifier cache]: [jwt] > valid={}, Causes={}",
					 jwtValid,
					 ((JWTVerificationResultAsNotValidToken)verficationResult).debugInfo());
		} else {
			if (log.isInfoEnabled()) log.info("[access token verifier cache]: [jwt] > valid={}",
					 jwtValid);
		}
		return new CachedAccessGrantedToken(Instant.now(), 	// cached at
								 			jwtIssuedAt,jwtExpiresAt,jwtValid);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final AccessGrantedTokenSpec _authTokenSpec;

	////////// private key
	protected final RSAPrivateKey _privateKey;

	////////// public key
	protected final RSAPublicKey _publicKey;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public AccessGrantedTokenService() {
		this(AccessGrantedTokenSpec.defaultSpec());
	}
	public AccessGrantedTokenService(final AccessGrantedTokenSpec authTokenSpec) {
		this(authTokenSpec,
			 // private key
			 CryptoKeys.PRIVATE_KEY_SPEC_FACTORY,CryptoKeys.PRIVATE_KEY_FROM_SPEC_FACTORY,PRIVATE_KEY_CLASSPATH_LOCATION,
			 // public key
			 CryptoKeys.PUBLIC_KEY_SPEC_FACTORY,CryptoKeys.PUBLIC_KEY_FROM_SPEC_FACTORY,PUBLIC_KEY_CLASSPATH_LOCATION);
	}
	public AccessGrantedTokenService(final AccessGrantedTokenSpec authTokenSpec,
							  final Path privateKeyClassPathLoc,final Path publicKeyClassPathLoc) {
		this(authTokenSpec,
			 // private key
			 CryptoKeys.PRIVATE_KEY_SPEC_FACTORY,CryptoKeys.PRIVATE_KEY_FROM_SPEC_FACTORY,privateKeyClassPathLoc,
			 // public key
			 CryptoKeys.PUBLIC_KEY_SPEC_FACTORY,CryptoKeys.PUBLIC_KEY_FROM_SPEC_FACTORY,publicKeyClassPathLoc);
	}
	public AccessGrantedTokenService(final AccessGrantedTokenSpec authTokenSpec,
								 	 // private key
									 final CryptoKeySpecFactory privateKeySpecFactory,final CryptoKeyFactoryFromSpec<RSAPrivateKey> privateKeyFactory,
									 final Path privateKeyClassPathLocation,
									 // public key
									 final CryptoKeySpecFactory publicKeySpecFactory,final CryptoKeyFactoryFromSpec<RSAPublicKey> publicKeyFactory,
									 final Path publicKeyClassPathLocation) {
		this(authTokenSpec,
			 CryptoKeys.loadRSAKeyFromClasspath(privateKeyClassPathLocation,
												privateKeySpecFactory,privateKeyFactory),
			 CryptoKeys.loadRSAKeyFromClasspath(publicKeyClassPathLocation,
												publicKeySpecFactory,publicKeyFactory));
	}
	public AccessGrantedTokenService(final AccessGrantedTokenSpec authTokenSpec,
									 final CryptoKeyPair rsaKeyPair) {
		this(authTokenSpec,
			 rsaKeyPair.getPrivateKey(),rsaKeyPair.getPublicKey());
	}
	public AccessGrantedTokenService(final AccessGrantedTokenSpec authTokenSpec,
							  		 final RSAPrivateKey privateKey,final RSAPublicKey publicKey) {
		_authTokenSpec = authTokenSpec;
		// private key
		_privateKey = privateKey;
		_publicKey = publicKey;

		if (log.isInfoEnabled()) log.info("[access token verifier cache]: PARAMETERS, token:{}, prK:{}, puK:{}", _authTokenSpec, _privateKey, _publicKey);

		// jwt verifier (uses public key)
		_jwtVerifier = JWTVerifierBuilder.createVerifier()
										 .usingPublicKey(_publicKey)
										 .checkingExpiration()
										 .ignoreAudience()
										 .build();
		// jwt cache
		// see: https://github.com/google/guava/wiki/CachesExplained
		RemovalListener<JWT,CachedAccessGrantedToken> cacheRemovalListener = notif -> log.info("[access token verifier cache] > remove cached [jwt] ({}) because {}",
																							   notif.getValue().debugInfo(),notif.getCause());
		_jwtCache = CacheBuilder.newBuilder()
								.maximumSize(JWT_CACHE_MAX_SIZE)	// cache a maximum of N jwt
								// EXPIRATION / REFRESH
								// BEWARE!!! do NOT use both expireAfterWrite and refreshAfterWrite
								// .expireAfterWrite(_authTokenSpec.getTokenExpirationSeconds(),TimeUnit.SECONDS)	// expire the cached [token] EXACTLY when the [token] expires
								.refreshAfterWrite(_authTokenSpec.getTokenExpirationSeconds(),TimeUnit.SECONDS)		// refresh the cached [token] EXACTLY when the [token] expires
								.removalListener(cacheRemovalListener)
								.build(new ValidAccessGrantedTokenCacheLoader());

		if (log.isInfoEnabled()) log.info("[access token verifier cache]: create object, hascode: {}, verifier: {}, jwt cache: {}", this.toString(), _jwtVerifier.toString(), _jwtCache.toString());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	PROVIDER
/////////////////////////////////////////////////////////////////////////////////////////
	public AccessGrantedToken generate(final AppCode issuer,final AppComponent audience) {
		return this.generate(issuer,audience,
							 _authTokenSpec.getTokenExpirationSeconds());
	}
	public AccessGrantedToken generate(final AppCode issuer,final AppComponent audience,
									   final long expirationSeconds) {
		AccessGrantedToken outToken = null;
		try {
			// [1] - Generate the JWT token
			SignedJWT signedJWT = JWTBuilder.createJWToken()
												 .forIssuer(issuer)
												 .toAudience(audience)
												 .withExpirationSeconds(expirationSeconds)
												 .noId()
												 .withoutCustomClaims()
												 .withPrivateKey(_privateKey)
											.buildSignedJWT();
			// [2] - Serialize
			JWT jwt = JWTBuilder.jwtFrom(signedJWT);

			// [3] - cache
			Instant jwtIssuedAt = signedJWT.getJWTClaimsSet().getIssueTime() != null ? signedJWT.getJWTClaimsSet().getIssueTime().toInstant()
																   					 : Instant.now();
			Instant jwtExpiresAt = signedJWT.getJWTClaimsSet().getExpirationTime() != null ? signedJWT.getJWTClaimsSet().getExpirationTime().toInstant()
																				   		   : Instant.now();
			_jwtCache.put(jwt,new CachedAccessGrantedToken(jwtIssuedAt,jwtExpiresAt));

			if (log.isInfoEnabled()) log.info("[access token verifier cache]: generate issued at:{}, expire at:{}, JWT token:{}",
					 jwtIssuedAt.toString(),
					 jwtExpiresAt.toString(),
					 jwt.toString());

			// [4] - Return
			outToken = new AccessGrantedToken(jwt,
									   		  jwtIssuedAt,jwtExpiresAt);		// valid
		} catch (Throwable th) {
			log.error("Error while GENERATING [web content access token]: {}",
					  th.getMessage(),th);
		}
		// [99] - Return
		return outToken;
	}
	public AccessGrantedToken generate() {
		return this.get();
	}
	@Override
	public AccessGrantedToken get() {
		return this.generate(_authTokenSpec.getIssuer(),_authTokenSpec.getAudience(),
							 _authTokenSpec.getTokenExpirationSeconds());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	VERIFY
/////////////////////////////////////////////////////////////////////////////////////////
	public AccessGrantedToken verifyJWT(final JWT jwt) {
		if (log.isInfoEnabled()) log.info("[access token verifier cache]: verify JWT token:{}", jwt.toString());

		AccessGrantedToken outToken = null;
		try {
			CachedAccessGrantedToken cachedJWT = _jwtCache.getIfPresent(jwt);
			if (cachedJWT != null) {
				// existing JWT
				cachedJWT.setLastAccessedAt(Instant.now());
				if (log.isTraceEnabled()) log.trace("[access token verifier cache]:  cache exists. {}", cachedJWT.debugInfo());
			} else {
				// new JWT
				cachedJWT = _jwtCache.get(jwt);	// this forces a NEW cache entry to be generated for the JWT (see R01PValidJWTCacheLoader#load method above)
				if (log.isTraceEnabled()) log.trace("[access token verifier cache]:  new JWT. {}", cachedJWT.debugInfo());
			}
			// convert the cache entry
			if (cachedJWT != null) {
				outToken = new AccessGrantedToken(jwt,
										   		  cachedJWT.getJwtIssuedAt(),cachedJWT.getJwtExpiresAt(),
										   		  cachedJWT.isJwtValid());
				if (log.isTraceEnabled()) log.trace("[access token verifier cache]:  new AccessGrantedToken. {}", outToken.debugInfo());
			}
		} catch (Throwable th) {
			log.error("Error while verifying [jwt]={} > {}",
					  jwt,
					  th.getMessage(),th);
		}
		return outToken;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	USED IN TESTING
/////////////////////////////////////////////////////////////////////////////////////////
	public AccessGrantedToken getCachedJWTOrNull(final JWT jwt) {
		CachedAccessGrantedToken cachedJWT = null;
		try {
			cachedJWT = _jwtCache.getIfPresent(jwt);
		} catch (Throwable th) {
			log.error("Error while verifying [jwt]={} > {}",
					  jwt,
					  th.getMessage(),th);
		}
		return cachedJWT != null ? new AccessGrantedToken(jwt,
											 	   		  cachedJWT.getJwtIssuedAt(),cachedJWT.getJwtExpiresAt(),
											 	   		  cachedJWT.isJwtValid())
								 : null;
	}
	public boolean isCachedJWT(final JWT jwt) {
		return this.getCachedJWTOrNull(jwt) != null;
	}
}
