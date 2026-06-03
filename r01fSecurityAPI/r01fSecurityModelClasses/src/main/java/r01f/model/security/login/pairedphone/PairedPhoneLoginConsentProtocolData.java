package r01f.model.security.login.pairedphone;

import java.util.Collection;



import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.annotations.Immutable;
import r01f.guids.OIDBaseMutable;
import r01f.model.security.SecurityObject;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.url.Url;

@Accessors(prefix="_")
@NoArgsConstructor
@MarshallType(as="pairedPhoneLoginConsentProtocolData")
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
	@NoArgsConstructor
	@MarshallType(as="consentProtocolUri")
	public static class LoginConsentProtocolURI {

		@Immutable
		@MarshallType(as="loginConsentProtocolId")
		@NoArgsConstructor
		public static class LoginConsentProtocolID
				    extends OIDBaseMutable<String> {
			private static final long serialVersionUID = 4162366466990455545L;

			public LoginConsentProtocolID(final String id) {
				super(id);
			}
			public static LoginConsentProtocolID forId(final String id) {
				return new LoginConsentProtocolID(id);
			}
		}

		@MarshallField(as="id", whenXml=@MarshallFieldAsXml(attr=true))
		@Getter @Setter private LoginConsentProtocolID _id;

		@MarshallField(as="url",escape=true)
		@Getter @Setter private Url _url;
	}
}
