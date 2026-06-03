package r01f.jwt;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.annotations.Immutable;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.types.CanBeRepresentedAsString;

/** 
 * JWT BEARER .
 * The standard way to use JWT token is to use a HTTP header for your client to send
 * an access token (JWT or any other token) in the the Authorization header with the Bearer authentication scheme.
 * This scheme is described by the RFC6750.
 * 
 * Example:
 * <pre>
 * 		GET /resource HTTP/1.1
 * 		Host: server.example.com
 * 		Authorization: Bearer eyJhbGciOiJIUzI1NiIXVCJ9TJV...r7E20RMHrHDcEfxjoYZgeFONFh7HgQ
 * </pre>
 * So :
 *  Use this class for :
 *     1.  Receive JWT Bearer.
 *     2.  Get JWT
 *  Important :
 *  --------------
 * The no standard way used in some legacy PCI Application is :
 * 			GET /resource HTTP/1.1
 * 			Host: server.example.com
 * 			Bearer :  eyJhbGciOiJIUzI1NiIXVCJ9TJV...r7E20RMHrHDcEfxjoYZgeFONFh7HgQ
 * 
 * This non-standard REST Methods has this signature:
 * 			public Response myMethod(@HeaderParam("bearer") final JWT jwt)
 * This should be updated to:
 * 			public Response myMethod(@HeaderParam(JWTBearer.HEADER_PARAM_NAME) final JWTBearer jwt)
 * 
 * In the filters may be applied a similar change.
 * Non-standard Filters has this signature:
 * <pre class='brush:java'>
 * 			String jwtBearer = ctx.getHeaderString("bearer");
 * 			JWT signedJWT = JWT.forValue(jwtBearer);
 * </pre>
 * 	
 * This should be updated to:
 * <pre class='brush:java'>
 * 			String jwtBearer = ctx.getHeaderString(JWTBearer.HEADER_PARAM_NAME);
 * 			JWT signedJWT = JWTBearer.forValue(jwtBearer)
 * 									 .getJWT();
 * </pre>
 */
@Immutable
@NoArgsConstructor
@Accessors(prefix="_")
public class JWTBearer
  implements CanBeRepresentedAsString,
			 Serializable {
	
	private static final long serialVersionUID = 968582404014830810L;
/////////////////////////////////////////////////////////////////////////////////////////
// CONSTANTS
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String PREFIX = "Bearer ";
	public static final String HEADER_PARAM_NAME = "Authorization";
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="value",
				   whenXml=@MarshallFieldAsXml(asParentElementValue=true))
	@Getter @Setter private String _value;
/////////////////////////////////////////////////////////////////////////////////////////
// 	SOME CREATE METHODS METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	public JWTBearer(final String value) {
		_value = value;
	}
	public static JWTBearer forValue(final String value) {
		return new JWTBearer(value);
	}
	@Override
	public String asString() {
		return _value;
	}
	@Override
	public String toString() {
		return asString();
	}
/////////////////////////////////////////////////////////////////////////////////////////
// FROM
/////////////////////////////////////////////////////////////////////////////////////////
	public JWT getJWT() {
		String jwtTokenAsString = _value.substring(PREFIX.length());
		return new JWT(jwtTokenAsString);
	}
	public static JWTBearer from(final JWT jwt) {
		return  new JWTBearer ( new StringBuffer(PREFIX).append(jwt.getValue()).toString());
	}
}
