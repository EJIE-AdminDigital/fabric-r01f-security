package r01f.security.saml.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.annotations.Immutable;
import r01f.guids.OIDBaseMutable;
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
	/**
	 * Base for every SAML id objects
	 */
	@Immutable
	@NoArgsConstructor
	public static abstract class SAMLIDBase
						 extends OIDBaseMutable<String>
					  implements IsSAMLID {
		private static final long serialVersionUID = 4162366466990455545L;

		public SAMLIDBase(final String id) {
			super(id);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SAML Issuer Nick > a "sort" saml issuer nick like okta or something like that
/////////////////////////////////////////////////////////////////////////////////////////
	@Immutable
	@MarshallType(as="samlIdPId")
	@NoArgsConstructor
	public static class SAMLIdPID
				extends SAMLIDBase {

		private static final long serialVersionUID = 9190261083110952566L;

		public SAMLIdPID(final String oid) {
			super(oid);
		}
		public static SAMLIdPID forId(final String id) {
			return new SAMLIdPID(id);
		}
		public static SAMLIdPID valueOf(final String id) {
			return SAMLIdPID.forId(id);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	The SAML issuer id > ussually an uri
/////////////////////////////////////////////////////////////////////////////////////////
	@Immutable
	@MarshallType(as="samlIssuerId")
	@NoArgsConstructor
	public static class SAMLIssuerID
				extends SAMLIDBase {
		private static final long serialVersionUID = -5818330851015602242L;
		public SAMLIssuerID(final String oid) {
			super(oid);
		}
		public static SAMLIssuerID forId(final String id) {
			return new SAMLIssuerID(id);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Immutable
	@MarshallType(as="samlRelyingPartyId")
	@NoArgsConstructor
	public static class SAMLRelyingPartyID
				extends SAMLIDBase {
		private static final long serialVersionUID = -5818330851015602242L;
		public SAMLRelyingPartyID(final String oid) {
			super(oid);
		}
		public static SAMLRelyingPartyID forId(final String id) {
			return new SAMLRelyingPartyID(id);
		}
	}
}
