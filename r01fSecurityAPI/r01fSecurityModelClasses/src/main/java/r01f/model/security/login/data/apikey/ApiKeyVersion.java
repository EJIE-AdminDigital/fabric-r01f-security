package r01f.model.security.login.data.apikey;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.aspects.interfaces.dirtytrack.ConvertToDirtyStateTrackable;
import r01f.model.security.IsSecurityObject;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.Range;

@MarshallType(as="apiKeyVersion")
@ConvertToDirtyStateTrackable
@Accessors(prefix="_")
public class ApiKeyVersion
  implements IsSecurityObject {

	private static final long serialVersionUID = 5092311480786334428L;

/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="apiKey")
	@Getter @Setter private ApiKey _apiKey;

	@MarshallField(as="usageDateRange",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601))
	@Getter @Setter private Range<Instant> _usageDateRange;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ApiKeyVersion() {
		// default no-args constructor
	}
	public ApiKeyVersion(final ApiKey apiKey,final Range<Instant> usageDateRange) {
		if (apiKey == null) throw new IllegalArgumentException("The apiKey version must be NOT null");
		if (usageDateRange == null
		 || !usageDateRange.hasLowerBound()
		 || !usageDateRange.hasUpperBound()) throw new IllegalArgumentException("The usageDateRange must be NOT null and must have lower and upper bounds");
		_apiKey = apiKey;
		_usageDateRange = usageDateRange;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  VALIDITY
/////////////////////////////////////////////////////////////////////////////////////////
	public boolean isValidAt(final Instant date) {
		return _usageDateRange != null ? _usageDateRange.contains(date)
							  		   : false;
	}
	public boolean isValidNow() {
		return this.isValidAt(Instant.now());
	}
}
