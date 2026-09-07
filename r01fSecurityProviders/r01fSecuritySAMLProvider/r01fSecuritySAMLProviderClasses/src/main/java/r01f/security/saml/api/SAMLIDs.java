package r01f.security.saml.api;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import r01f.annotations.Immutable;
import r01f.guids.OIDTyped;
import r01f.objectstreamer.annotations.MarshallType;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class SAMLIDs {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static interface IsSAMLID
					extends OIDTyped<String> {
		// just a marker interface
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SAML Issuer Nick > a "sort" saml issuer nick like okta or something like that
/////////////////////////////////////////////////////////////////////////////////////////
	@Immutable
	@MarshallType(as="samlIdPId")
	public record SAMLIdPID(@Getter String id)
	   implements IsSAMLID {

		public static SAMLIdPID from(final String id) {
			return new SAMLIdPID(id);
		}
		public static SAMLIdPID forId(final String id) {
			return new SAMLIdPID(id);
		}
		public static SAMLIdPID valueOf(final String id) {
			return SAMLIdPID.forId(id);
		}
		public static SAMLIdPID fromString(final String id) {
			return SAMLIdPID.forId(id);
		}
		
		@Override
		public String asString() {
			return this.id;
		}
		@Override
		public String toString() {
			return this.id;
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	The SAML issuer id > ussually an uri
/////////////////////////////////////////////////////////////////////////////////////////
	@Immutable
	@MarshallType(as="samlIssuerId")
	public record SAMLIssuerID(@Getter String id)
	   implements IsSAMLID {

		public static SAMLIssuerID from(final String id) {
			return new SAMLIssuerID(id);
		}
		public static SAMLIssuerID forId(final String id) {
			return new SAMLIssuerID(id);
		}
		public static SAMLIssuerID valueOf(final String id) {
			return new SAMLIssuerID(id);
		}
		public static SAMLIssuerID fromString(final String id) {
			return new SAMLIssuerID(id);
		}
		
		@Override
		public String asString() {
			return this.id;
		}
		@Override
		public String toString() {
			return this.id;
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Immutable
	@MarshallType(as="samlRelyingPartyId")
	public record SAMLRelyingPartyID(@Getter String id)
	   implements IsSAMLID {
		
		public static SAMLRelyingPartyID from(final String id) {
			return new SAMLRelyingPartyID(id);
		}
		public static SAMLRelyingPartyID forId(final String id) {
			return new SAMLRelyingPartyID(id);
		}
		public static SAMLRelyingPartyID valueOf(final String id) {
			return new SAMLRelyingPartyID(id);
		}
		public static SAMLRelyingPartyID fromString(final String id) {
			return new SAMLRelyingPartyID(id);
		}
		
		@Override
		public String asString() {
			return this.id;
		}
		@Override
		public String toString() {
			return this.id;
		}
	}
}
