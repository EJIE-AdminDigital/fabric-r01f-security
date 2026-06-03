package r01f.cwt.verifier;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Collection;

import lombok.extern.slf4j.Slf4j;
import r01f.cwt.CWT;
import r01f.cwt.model.CWTDecoded;
import r01f.util.types.collections.Lists;

@Slf4j
public class CWTVerifierImpl
	extends CWTVerifierBase
  implements CWTVerifier {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public CWTVerifierImpl(final CWTVerifierForCOSE verifier,final String audienceToVerificate,
						   final boolean expirationVerificationEnabled) {
		super(verifier,audienceToVerificate,
			  expirationVerificationEnabled, 
			  false);
	}
	public CWTVerifierImpl(final CWTVerifierForCOSE coseVerifer,final String audienceToCheck,
						   final boolean  checkExpiration,final boolean checkExpirationAndIssue) {
		super(coseVerifer,audienceToCheck, 
			  checkExpiration,checkExpirationAndIssue);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public CWTVerificationResult verify(final CWT cwt) {
		boolean cwtIsOk = true;
		Collection<CWTVerificationResultInvalidCause> invalidCauses =  Lists.newArrayList();
		///////////////////// Check sign validations and others ( expirations, audience, etc....
		try {
			boolean cwtSignOK = _verifierForCOSE.verify(cwt);
			if (cwtSignOK) {
				log.warn("CWT signed ok: {}",
						 cwt);
			} else {
				cwtIsOk = false;
				invalidCauses.add(CWTVerificationResultInvalidCause.INVALID_SIGNATURE);
			}
			CWTDecoded cwtDecoded = _cwtParser.parse(cwt);
			if (_isCwtExpirationVerificationEnabled() ) {
				Instant expirationTime = cwtDecoded.getPayload().getExpirationTime();
				Instant issueTime =  cwtDecoded.getPayload().getIssuedAt();
				if (expirationTime == null) {
					cwtIsOk = false;
					invalidCauses.add(CWTVerificationResultInvalidCause.EXPIRED);
				} else if (_isCwtExpirationAndIssueVerificationEnabled() && issueTime == null) {
					cwtIsOk = false;
					invalidCauses.add(CWTVerificationResultInvalidCause.EXPIRED);
				} else {
					Instant now = Instant.now();
					if (now.equals(expirationTime) || now.isAfter(expirationTime)) {
						SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
						String expVal = sdf.format(expirationTime);
						String nowVal = sdf.format(now);
						log.warn("CWT expired at {}. Current time: {}",
								 expVal,nowVal);
						cwtIsOk = false;
						invalidCauses.add(CWTVerificationResultInvalidCause.EXPIRED);
					}

					/*if (_isCwtExpirationAndIssueVerificationEnabled()) {
						if ((now.getTime() + 300000 - issueTime.getTime()) < 0L) { // set 5 minutes of margin
							SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
							String expVal = sdf.format(issueTime);
							String nowVal = sdf.format(now);
							log.warn("CWT can't be issued after now. Issued at {}. Current time: {}",
									  expVal,nowVal);
							cwtIsOk = false;
							invalidCauses.add(CWTVerificationResultInvalidCause.INVALID_ISSUE_DATE);

						} else if ((now.getTime() - issueTime.getTime()) >= 86400000L) { // The CWT can't have more than 24 hours from issue date
							SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
							String expVal = sdf.format(issueTime);
							String nowVal = sdf.format(now);
							log.warn("CWT can't be issued more than 24 hours ago. Issued at {}. Current time: {}",
									 expVal,nowVal);
							cwtIsOk = false;
							invalidCauses.add(CWTVerificationResultInvalidCause.INVALID_ISSUE_DATE);
						}
					}*/
				}
			}
			if (_isCwtAudienceVerificationEnabled()) {
				boolean cwtAudienceVerifiedOK = cwtDecoded!= null
											 && cwtDecoded.getPayload().getAudience().contains(_audienceToVerificate);
				if (! cwtAudienceVerifiedOK) {
					cwtIsOk = false;
					invalidCauses.add(CWTVerificationResultInvalidCause.INVALID_AUDIENCE);
				}
			}
			//// BUILD RESULT
			return cwtIsOk ? new CWTVerificationResultAsValidToken()
						   : new CWTVerificationResultAsNotValidToken(invalidCauses);
		} catch (final Throwable e) {
			e.printStackTrace();
			invalidCauses.add(CWTVerificationResultInvalidCause.OTHER);
			return new CWTVerificationResultAsNotValidToken(invalidCauses);
		}
	}
}
