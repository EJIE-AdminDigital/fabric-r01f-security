package r01f.model.security.login.pairedphone;

import java.util.Collection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.annotations.Immutable;
import r01f.guids.OIDTyped;
import r01f.model.security.SecurityObject;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.url.Url;

@MarshallType(as="pairedPhoneLoginConsentProtocolData")
@Accessors(prefix="_")
@NoArgsConstructor
public class PairedPhoneLoginConsentProtocolData
  implements SecurityObject {

	private static final long serialVersionUID = -792074157666404277L;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="urls",
				   whenXml=@MarshallFieldAsXml(collectionElementName="url"))
	@Setter @Getter private Collection<LoginConsentProtocolURI> _uri ;

/////////////////////////////////////////////////////////////////////////////////////////
//  Inner Class
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallType(as="consentProtocolUri")
	@NoArgsConstructor
	public static class LoginConsentProtocolURI {

		@Immutable
		@MarshallType(as="loginConsentProtocolId")
		@Accessors(prefix="")	// beware
		public record LoginConsentProtocolID(@Getter String id)
		   implements OIDTyped<String> {
			public static LoginConsentProtocolID from(final String id) {
				return new LoginConsentProtocolID(id);
			}
			public static LoginConsentProtocolID forId(final String id) {
				return new LoginConsentProtocolID(id);
			}
			public static LoginConsentProtocolID valueOf(final String id) {
				return new LoginConsentProtocolID(id);
			}
			public static LoginConsentProtocolID fromString(final String id) {
				return new LoginConsentProtocolID(id);
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

		@MarshallField(as="id", whenXml=@MarshallFieldAsXml(attr=true))
		@Getter @Setter private LoginConsentProtocolID _id;

		@MarshallField(as="url",escape=true)
		@Getter @Setter private Url _url;
	}
}
