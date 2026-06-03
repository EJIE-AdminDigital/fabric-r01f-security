package r01f.cwt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.annotations.Immutable;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.types.CanBeRepresentedAsString;
import r01f.types.StringHex;

/*
 * CBOR Web Token (CWT) is a compact means of representing claims to be  transferred between two parties.
 * The claims in a CWT are encoded in the Concise Binary Object Representation (CBOR),   and CBOR Object Signing and Encryption (COSE) is used for added application-layer
 *  security protection

 * The IETF Standard : http://tools.ietf.org/html/rfc7519
 *
 *  CWT Web Token is just a byte token :
 *
 *    f.e a601654b616c6c65041a6669ecd5061a66425fd518636576616c75656239386576616c75656b637573746f6d436c61696d694f6c61626172726961
 *
 * Use this class to wrap it. See alsep : https://cwt.me/
 *
 * ---> To Build a valid CWT:
 * 				              CWT myCWT = CWTBuilder.createJWToken()
														 .forIssuer(AppCode.forId("z99"))
														 .toAudience("p12.audience")
														 .withExpirationSeconds(1)
														 .noId()
														 .usingCustomClaims(otherFieldsAsClaims)
														 .withKey(keyBytes) <==  PKI or HMAC Key.
													.build();
 * ----> To Verify a valid CWT .
		  					CWTVerifier verifier = CWTVerifierBuilder.createVerifier()
		                                                             .usingKey(keyBytes)
		                                                             .checkingExpiration()
		                                                             .forAudience("theAudienceForWhoThisTokenWasIssued")
		                                                             .buid();
		                    verifier.verify(myCWT);
 *
 *
 */
@Immutable
@NoArgsConstructor
@Accessors(prefix="_")
public class CWT
  implements CanBeRepresentedAsString {

	private static final long serialVersionUID = -3806666517358846110L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="value",
				   whenXml=@MarshallFieldAsXml(asParentElementValue=true))
	@Getter @Setter private StringHex  _value;
/////////////////////////////////////////////////////////////////////////////////////////
// 	CONSTRUCTOR & BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public CWT(final StringHex value) {
		_value = value;
	}
	public static CWT forValue(final StringHex value) {
		return new CWT(value);
	}
	public static CWT forValue(final byte[] value) {
		return new CWT(StringHex.encode(value));
	}

	public static CWT from(final StringHex value) {
		return new CWT(value);
	}
	public static CWT fromString(final StringHex value) {
		return new CWT(value);
	}
	public static CWT valueOf(final StringHex value) {
		return new CWT(value);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	AS STRING
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public String asString() {
		return _value.asString();
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
		if (!(o instanceof CWT)) return false;

		CWT other = (CWT)o;
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
