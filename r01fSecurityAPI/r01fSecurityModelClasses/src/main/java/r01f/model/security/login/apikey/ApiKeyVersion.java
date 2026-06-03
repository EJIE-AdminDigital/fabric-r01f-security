package r01f.model.security.login.apikey;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.aspects.interfaces.dirtytrack.ConvertToDirtyStateTrackable;
import r01f.facets.HasID;
import r01f.guids.OID;
import r01f.model.security.PersistableSecurityObjectBase;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.securitycontext.SecurityOIDs.ApiKeyVersionOID;
import r01f.types.AppVersion;
import r01f.types.Range;

@MarshallType(as="apiKeyVersion")
@ConvertToDirtyStateTrackable
@Accessors(prefix="_")
public class ApiKeyVersion
	 extends PersistableSecurityObjectBase<ApiKeyVersionOID,ApiKeyVersion> 
  implements HasID<AppVersion> {

	private static final long serialVersionUID = 5092311480786334428L;

/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="code",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private AppVersion _id;

	@MarshallField(as="name",escape=true)
	@Getter @Setter private String _name;

	@MarshallField(as="description",escape=true)
	@Getter @Setter private String _description;

	@MarshallField(as="usageRange",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601))
	@Getter @Setter private Range<Instant> _usageRange;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ApiKeyVersion() {
		// default no-args constructor
	}
	public ApiKeyVersion(final String version) {
		_id = AppVersion.from(version);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  VALIDITY
/////////////////////////////////////////////////////////////////////////////////////////
	public boolean isValidAt(final Instant date) {
		return _usageRange != null ? _usageRange.contains(date)
							  : false;
	}
	public boolean isValidNow() {
		return this.isValidAt(Instant.now());
	}
	public void notValidFromNowOn() {
		this.notValidFrom(Instant.now());
	}
	public void notValidFrom(final Instant date) {
		if (_usageRange.hasUpperBound()) throw new IllegalStateException("The version usage range already has upper bound: " + _usageRange.getUpperBound());
		_usageRange = Range.closed(_usageRange.getLowerBound(),date);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	ID
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public void unsafeSetId(final OID id) {
		_id = (AppVersion)id;
	}
}