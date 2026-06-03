package r01f.cwt.verifier;

import r01f.cwt.parser.CWTParser;
import r01f.cwt.parser.CWTParserImpl;


public abstract class CWTVerifierBase
  implements CWTVerifier {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	protected final CWTVerifierForCOSE _verifierForCOSE;
	protected final CWTParser _cwtParser;
	protected final String _audienceToVerificate;
	protected final boolean _expirationVerificationEnabled;
	protected final boolean _expirationAndIssueVerificationEnabled;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public CWTVerifierBase(final CWTVerifierForCOSE verifier, final String audienceToVerificate, final boolean expirationVerificationEnabled) {
		this(verifier, audienceToVerificate, expirationVerificationEnabled, false);
	}
	public CWTVerifierBase(final CWTVerifierForCOSE verifier, final String audienceToVerificate, final boolean expirationVerificationEnabled, final boolean expirationAndIssueVerificationEnabled) {
		_verifierForCOSE = verifier;
		_cwtParser = new CWTParserImpl();
		_audienceToVerificate = audienceToVerificate;
		_expirationVerificationEnabled = expirationVerificationEnabled;
		_expirationAndIssueVerificationEnabled = expirationAndIssueVerificationEnabled;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	protected boolean _isCwtExpirationVerificationEnabled() {
		return _expirationVerificationEnabled;
	}
	protected boolean _isCwtExpirationAndIssueVerificationEnabled() {
		return _expirationAndIssueVerificationEnabled;
	}
	protected boolean _isCwtAudienceVerificationEnabled() {
		return ( _audienceToVerificate !=  null);
	}
}
