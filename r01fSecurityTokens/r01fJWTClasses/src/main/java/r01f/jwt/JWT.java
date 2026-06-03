package r01f.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.annotations.Immutable;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.types.CanBeRepresentedAsString;

/**
 * JWT TOKEN .
 * The IETF Standard : http://tools.ietf.org/html/rfc7519
 *
 * JSON Web Token is just a String token :
 * <pre>
 *    	f.e eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
 * </pre>
 *
 * Use this class to wrap it. See alsep : https://jwt.io/
 * ---> To Build a valid JWT:
 * <pre class='brush:java'>
 *     JWT myJWT = JWTBuilder.createJWToken()
 *								 .forIssuer(AppCode.forId("z99"))
 *								 .toAudience("p12.audience")
 *								 .withExpirationSeconds(1)
 *								 .noId()
 *								 .usingCustomClaims(otherFieldsAsClaims)
 *								 .withKey(keyBytes) <==  PKI or HMAC Key.
 *							.build();
 * </pre>
 * ----> To Verify a valid JWT .
 * <pre class='brush:java'>
 *		JWTVerifier verifier = JWTVerifierBuilder.createVerifier()
 *                                                .usingKey(keyBytes)
 *                                                .checkingExpiration()
 *                                                .forAudience("theAudienceForWhoThisTokenWasIssued")
 *                                                .buid();
 *       verifier.verify(myJWT);
 * </pre>
 */
@Immutable
@NoArgsConstructor
@Accessors(prefix="_")
public class JWT
  implements CanBeRepresentedAsString {
	
	private static final long serialVersionUID = -3806666517358846110L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="value",
				   whenXml=@MarshallFieldAsXml(asParentElementValue=true))
	@Getter @Setter private String _value;
/////////////////////////////////////////////////////////////////////////////////////////
// 	CONSTRUCTOR & BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public JWT(final String value) {
		_value = value;
	}
	public static JWT forValue(final String value) {
		return new JWT(value);
	}
	public static JWT from(final String value) {
		return new JWT(value);
	}
	public static JWT fromString(final String value) {
		return new JWT(value);
	}
	public static JWT valueOf(final String value) {
		return new JWT(value);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	AS STRING
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public String asString() {
		return _value;
	}
	@Override
	public String  toString() {
		return asString();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EQUALS & HASHCODE
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null) return false;
		if (!(o instanceof JWT)) return false;
		
		JWT other = (JWT)o;
		return this.getValue() != null && other.getValue() != null ? this.getValue().equals(other.getValue())
																   : this.getValue() != null ? false
																		   					 : true;	// both null
	}
	@Override
	public int hashCode() {
		return this.getValue() != null ? this.getValue().hashCode()
									   : super.hashCode();
	}
}
