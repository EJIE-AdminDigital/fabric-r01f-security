package r01f.security.accessgrantedtoken;

import java.io.Serializable;
import java.time.Instant;

import com.google.common.base.Objects;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.debug.Debuggable;
import r01f.jwt.JWT;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.util.types.Strings;

@MarshallType(as="accessGrantedToken")
@Accessors(prefix="_")
public class AccessGrantedToken
  implements Debuggable,
  			 Serializable {

	private static final long serialVersionUID = -680541598477718099L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter @Setter private JWT _jwt;
	@Getter @Setter private Instant _jwtIssuedAt;
	@Getter @Setter private Instant _jwtExpiresAt;
	@Getter @Setter private boolean _jwtValid;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public AccessGrantedToken() {
		// default no-args constructor
	}
	public AccessGrantedToken(final JWT jwt,
					   		  final Instant jwtIssuedAt,final Instant jwtExpiresAt) {
		this(jwt,
			 jwtIssuedAt,jwtExpiresAt,
			 true);	// valid by default
	}
	public AccessGrantedToken(final JWT jwt,
					   		  final Instant jwtIssuedAt,final Instant jwtExpiresAt,
					   		  final boolean jwtValid) {
		_jwt = jwt;
		_jwtIssuedAt = jwtIssuedAt;
		_jwtExpiresAt = jwtExpiresAt;
		_jwtValid = jwtValid;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EQUALS & HASHCODE
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null) return false;
		if (!(o instanceof AccessGrantedToken)) return false;
		AccessGrantedToken other = (AccessGrantedToken)o;
		boolean jwtEq = this.getJwt() != null && other.getJwt() != null ? this.getJwt().equals(other.getJwt())
															    		: this.getJwt() != null ? false : true;
		boolean issuedAtEq = this.getJwtIssuedAt() != null && other.getJwtIssuedAt() != null ? this.getJwtIssuedAt().equals(other.getJwtIssuedAt())
																							 : this.getJwtIssuedAt() != null ? false : true;
		boolean expiresAtEq = this.getJwtExpiresAt() != null && other.getJwtExpiresAt() != null ? this.getJwtExpiresAt().equals(other.getJwtExpiresAt())
																								: this.getJwtExpiresAt() != null ? false : true;
		boolean validEq = this.isJwtValid() == other.isJwtValid();
		return jwtEq && issuedAtEq && expiresAtEq && validEq;
	}
	@Override
	public int hashCode() {
		return Objects.hashCode(_jwt,_jwtIssuedAt,_jwtExpiresAt,_jwtValid);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	DEBUG
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CharSequence debugInfo() {
		return Strings.customized("access granted issuedAt={} expiresAt={} valid={} > jwt={}",
								  _jwtIssuedAt,_jwtExpiresAt,_jwtValid,
								  _jwt);
	}
}
