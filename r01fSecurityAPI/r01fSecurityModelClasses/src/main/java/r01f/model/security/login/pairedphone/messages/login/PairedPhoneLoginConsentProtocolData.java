package r01f.model.security.login.pairedphone.messages.login;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.IsSecurityObject;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.url.UrlCollection;

@MarshallType(as="pairedPhoneLoginConsentProtocolData")
@Accessors(prefix="_")
public class PairedPhoneLoginConsentProtocolData
  implements IsSecurityObject {

	private static final long serialVersionUID = -792074157666404277L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="urls",
				   whenXml=@MarshallFieldAsXml(collectionElementName="url"))
	@Setter @Getter private UrlCollection _urls;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public PairedPhoneLoginConsentProtocolData() {
		_urls = new UrlCollection();
	}
	public PairedPhoneLoginConsentProtocolData(final UrlCollection urls) {
		_urls = urls;
	}
}
