package r01f.model.security.login.impersonation;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.SecurityPersistableObjectBase;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.SecurityIDs.SecurityToken;
import r01f.oids.SecurityOIDs.SecurityPrincipalImpersonationOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.types.Range;

@MarshallType(as="loginImpersonation")
@Accessors(prefix="_")
public class SecurityPrincipalImpersonation
  	 extends SecurityPersistableObjectBase<SecurityPrincipalImpersonationOID,
  									       SecurityPrincipalImpersonation> {

	private static final long serialVersionUID = -3274082364797625088L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
////////// --- IMPERSONATED 	
 	@MarshallField(as="impersonatedPrincipalOid",
				   whenXml=@MarshallFieldAsXml(attr=true))
 	@Getter @Setter private SecurityPrincipalOID _impersonatedPrincipalOid;
 	
 ////////// --- IMPERSONATING (the one that is logged in and requesting the impersonation)
 	@MarshallField(as="impersonatingPrincipalOid",
				   whenXml=@MarshallFieldAsXml(attr=true))
 	@Getter @Setter private SecurityPrincipalOID _impersonatingPrincipalOid;

////////// --- DATE RANGE
 	// The [date range] when the impersonation took place 
 	// (from the IMPERSONATION_BEGINS [event] to the IMPERSONATION_ENDS [event])
 	@MarshallField(as="dateRange")
 	@Getter @Setter private Range<Instant> _dateRange;
 	 
////////// --- TOKEN
 	// a [token] used to validate the impersonation
 	// (the [impersonationJWT] is signed using this [token]
 	@MarshallField(as="token",escape=true)
 	@Getter @Setter SecurityToken _token;
/////////////////////////////////////////////////////////////////////////////////////////
//	
///////////////////////////////////////////////////////////////////////////////////////// 	
 	public boolean isActive() {
 		if (_dateRange == null) throw new IllegalStateException("The impersonation [date range] is NULL");
 		return _dateRange.getUpperBound() == null;
 	}
 	public boolean isExpired() {
 		if (_dateRange == null) throw new IllegalStateException("The impersonation [date range] is NULL");
 		return _dateRange.getUpperBound() != null;
 	}
 	
 	
 	public void startImpersonation(final Instant startedAt) {
 		if (_dateRange != null) throw new IllegalStateException("The impersonation [date range] is NOT NULL");
 		_dateRange = new Range<Instant>(startedAt,null);
 	}
 	public void startImpersonation() {
 		this.startImpersonation(Instant.now());
 	}
 	public void endImpersonation(final Instant endedAt) {
 		if (_dateRange == null) throw new IllegalStateException("The impersonation [date range] is NULL");
 		_dateRange = new Range<Instant>(_dateRange.getLowerBound(),endedAt);
 	}
 	public void endImpersonation() {
 		this.endImpersonation(Instant.now());
 	}
}
