package r01f.jwt.verifier;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Collection;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.SignedJWT;

import lombok.extern.slf4j.Slf4j;
import r01f.jwt.JWT;
import r01f.util.types.collections.Lists;
import r01f.util.types.datetime.DateTimeUtils;

@Slf4j
public class JWTVerifierImpl
  implements JWTVerifier {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final JWSVerifier _verifier;
	private final String _audienceToVerificate;
	private final boolean _expirationVerificationEnabled;
	private final boolean _expirationAndIssuerVerificationEnabled;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public JWTVerifierImpl(final JWSVerifier verifier,final String audienceToVerificate,
						   final boolean expirationVerificationEnabled) {
		this(verifier,audienceToVerificate,
			 expirationVerificationEnabled,
			 false);	// expiration and issuer verification enabled
	}
	public JWTVerifierImpl(final JWSVerifier verifier,final String audienceToVerificate,
						   final boolean expirationVerificationEnabled,
						   final boolean expirationAndIssuerVerificationEnabled) {
		_verifier = verifier;
		_audienceToVerificate = audienceToVerificate;
		_expirationVerificationEnabled = expirationVerificationEnabled;
		_expirationAndIssuerVerificationEnabled = expirationAndIssuerVerificationEnabled;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public JWTVerificationResult verify(final JWT jwt) {
		boolean jwtIsOk = true;
		Collection<JWTVerificationResultInvalidCause> invalidCauses =  Lists.newArrayList();
		SignedJWT signedJWT;
		///////////////////// Check sign validations and others ( expirations, audience, etc....
		try {
			signedJWT = SignedJWT.parse(jwt.asString());
			boolean jwtSignOK = signedJWT.verify(_verifier);
			if (jwtSignOK) {
				log.debug("JWT signed ok: {}",
						 jwt);
			} else {
				jwtIsOk = false;
				invalidCauses.add(JWTVerificationResultInvalidCause.INVALID_SIGNATURE);
			}
			if (_isJwtExpirationVerificationEnabled() ) {
				Instant expirationTime = DateTimeUtils.asInstant(signedJWT.getJWTClaimsSet().getExpirationTime());
				Instant issueTime = DateTimeUtils.asInstant(signedJWT.getJWTClaimsSet().getIssueTime());
				if (expirationTime == null) {
					jwtIsOk = false;
					invalidCauses.add(JWTVerificationResultInvalidCause.EXPIRED);
				} else if (_isJwtExpirationAndIssueVerificationEnabled() && issueTime == null) {
					jwtIsOk = false;
					invalidCauses.add(JWTVerificationResultInvalidCause.EXPIRED);
				} else {
					Instant now = Instant.now();
					if (now.equals(expirationTime) || now.isAfter(expirationTime)) {
						SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
						String expVal = sdf.format(expirationTime);
						String nowVal = sdf.format(now);
						log.warn("JWT expired at {}. Current time: {}",
								 expVal,nowVal);
						jwtIsOk = false;
						invalidCauses.add(JWTVerificationResultInvalidCause.EXPIRED);
					}
					if (_isJwtExpirationAndIssueVerificationEnabled()) {
						if (now.isBefore(issueTime.minusMillis(300000))) { // set 5 minutes of margin
							SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
							String expVal = sdf.format(issueTime);
							String nowVal = sdf.format(now);
							log.warn("JWT can't be issued after now. Issued at {}. Current time: {}",
									  expVal,nowVal);
							jwtIsOk = false;
							invalidCauses.add(JWTVerificationResultInvalidCause.INVALID_ISSUE_DATE);
						} else if (!now.isBefore(issueTime.plusMillis(86400000L))) { // The JWT can't have more than 24 hours from issue date
							SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
							String expVal = sdf.format(issueTime);
							String nowVal = sdf.format(now);
							log.warn("JWT can't be issued more than 24 hours ago. Issued at {}. Current time: {}",
									 expVal,nowVal);
							jwtIsOk = false;
							invalidCauses.add(JWTVerificationResultInvalidCause.INVALID_ISSUE_DATE);
						}
					}
				}
			}
			if (_isJwtAudienceVerificationEnabled()) {
				boolean jwtAudienceVerifiedOK = signedJWT.getJWTClaimsSet().getAudience() != null
											 && signedJWT.getJWTClaimsSet().getAudience().contains(_audienceToVerificate);
				if (! jwtAudienceVerifiedOK) {
					jwtIsOk = false;
					invalidCauses.add(JWTVerificationResultInvalidCause.INVALID_AUDIENCE);
				}
			}
			//// BUILD RESULT
			return jwtIsOk ? new JWTVerificationResultAsValidToken()
						   : new JWTVerificationResultAsNotValidToken(invalidCauses);
		} catch (final ParseException e) {
			invalidCauses.add(JWTVerificationResultInvalidCause.OTHER);
			return new JWTVerificationResultAsNotValidToken(invalidCauses);
		} catch (final JOSEException e) {
			e.printStackTrace();
			invalidCauses.add(JWTVerificationResultInvalidCause.OTHER);
			return new JWTVerificationResultAsNotValidToken(invalidCauses);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	private boolean _isJwtExpirationVerificationEnabled() {
		return _expirationVerificationEnabled;
	}
	private boolean _isJwtExpirationAndIssueVerificationEnabled() {
		return _expirationAndIssuerVerificationEnabled;
	}
	private boolean _isJwtAudienceVerificationEnabled() {
		return ( _audienceToVerificate !=  null);
	}
}
