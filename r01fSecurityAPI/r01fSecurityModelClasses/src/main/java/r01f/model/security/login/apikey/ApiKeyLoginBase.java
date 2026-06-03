package r01f.model.security.login.apikey;


import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.aspects.interfaces.dirtytrack.ConvertToDirtyStateTrackable;
import r01f.facets.Summarizable;
import r01f.facets.Summarizable.HasSummaryFacet;
import r01f.facets.builders.SummarizableBuilder;
import r01f.model.security.login.LoginBase;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.types.AppVersion;
import r01f.types.Range;
import r01f.types.summary.SummaryBuilder;
import r01f.util.types.Strings;


@ConvertToDirtyStateTrackable
@Accessors(prefix="_")
public abstract class ApiKeyLoginBase<SELF_TYPE extends ApiKeyLoginBase<SELF_TYPE>>
	 		  extends LoginBase<SELF_TYPE>
  		   implements ApiKeyLogin,
  		   			  HasSummaryFacet {

	private static final long serialVersionUID = 6811283082734109884L;
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="apiKeyVersion",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private AppVersion _apiKeyVersion;

	@MarshallField(as="readOnly",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private boolean _readOnly = true;	// readonly by default

	@MarshallField(as="apiKeyUsageRange",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601))
	@Getter @Setter private Range<Instant> _apiKeyUsageRange;

	@MarshallField(as="apiKeyPair")
	@Getter @Setter private ApiKeyPair _apiKeyPair;	// BEWARE! it only contains the private key when issued
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ApiKeyLoginBase() {
		// default no-args constructor
	}
	public <K extends ApiKeyLoginBase<?>> ApiKeyLoginBase(final K other) {
		_oid  = other.getOid();
		_loginId = other.getLoginId();
		_userOid = other.getUserOid();
		_readOnly = other.isReadOnly();
		_apiKeyVersion = other.getApiKeyVersion();
		_apiKeyUsageRange = other.getApiKeyUsageRange();
		_apiKeyPair = other.getApiKeyPair() != null ? new ApiKeyPair(other.getApiKeyPair())	// clone
											  		: null;
	}
	public ApiKeyLoginBase(final UserOID owner,
					  	   final AppVersion version,final boolean readOnly,final Range<Instant> usageRange,
					  	   final ApiKeyPair keyPair) {
		if (keyPair == null || keyPair.getPublicKey() == null) throw new IllegalArgumentException("ApiKey keypair MUST not be null and at least the [public key] MUST be present!");
		_loginId = keyPair.asLoginId();
		_userOid = owner;
		_apiKeyVersion = version;
		_readOnly = readOnly;
		_apiKeyUsageRange = usageRange;
		_apiKeyPair = keyPair;
	}
	public ApiKeyLoginBase(final UserOID owner,
					  	   final AppVersion version,final Range<Instant> usageRange,
					  	   final ApiKeyPair keyPair) {
		this(owner,
			 version,false,usageRange,
			 keyPair);
	}
	public ApiKeyLoginBase(final UserOID owner,
					  	   final AppVersion version,final boolean readOnly,
					  	   final ApiKeyPair keyPair) {
		this(owner,
			 version,readOnly,
			 Range.greaterThan(Instant.now()),	// usable from now on
			 keyPair);
	}
	public ApiKeyLoginBase(final UserOID owner,
					  	   final AppVersion version,
					  	   final ApiKeyPair keyPair) {
		this(owner,
			 version,false,Range.greaterThan(Instant.now()),	// usable from now on
			 keyPair);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	OID
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public LoginOID getApiKeyOid() {
		return _oid;
	}
	@Override
	public void setApiKeyOid(final LoginOID oid) {
		_oid = oid;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	OWNER
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public UserOID getApiKeyOwnerOid() {
		return _userOid;
	}
	@Override
	public void setApiKeyOwnerOid(final UserOID userOid) {
		_userOid = userOid;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  VALIDITY
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean isActiveAt(final Instant date) {
		return _apiKeyUsageRange != null ? _apiKeyUsageRange.contains(date)
							  			 : false;
	}
	@Override
	public boolean isActiveNow() {
		return this.isActiveAt(Instant.now());
	}
	@Override
	public void notActiveFromNowOn() {
		this.notActiveFrom(Instant.now());
	}
	@Override
	public void notActiveFrom(final Instant date) {
		if (_apiKeyUsageRange.hasUpperBound()) throw new IllegalStateException("The key usage range already has upper bound: " + _apiKeyUsageRange.getUpperBound());
		_apiKeyUsageRange = Range.closed(_apiKeyUsageRange.getLowerBound(),date);
	}
////////////////////////////////////////////////////////////////////////////////////////
//	SUMMARIES
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public Summarizable asSummarizable() {
		return SummarizableBuilder.summarizableFrom(SummaryBuilder.languageInDependent()
															.create(Strings.customized("api key with oid={} owned by user oid={} usable withing {}",
																	_oid,_userOid,_apiKeyUsageRange)));

	}
}