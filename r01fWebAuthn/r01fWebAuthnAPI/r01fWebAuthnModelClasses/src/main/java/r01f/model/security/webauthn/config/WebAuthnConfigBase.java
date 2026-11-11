package r01f.model.security.webauthn.config;

import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.types.url.Url;
import r01f.util.types.StringCustomizeUtils;

@Slf4j
@Accessors(prefix="_")
public abstract class WebAuthnConfigBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	ID
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="id",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter protected IdentityProvider _id;

/////////////////////////////////////////////////////////////////////////////////////////
//	URL VARS: avoid repeating things in other urls
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="urlVars")
	@Getter @Setter protected Properties _urlVars;

	public PropertiesAsStep getUrlVar(final String key) {
		return new PropertiesAsStep(_urlVars,key);
	}
	public void debugUrlVars() {
		_debugProperties(_urlVars);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	General Props: general purpose properties.
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="properties")
    @Setter @Getter protected Properties _properties;

	public PropertiesAsStep getProperty(final String key) {
		 return new PropertiesAsStep(_properties,key);
	}
	public void debugProperties() {
		_debugProperties(_properties);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
    public class PropertiesAsStep {
		private final Properties _props;
		private final String _key;

		public String asString() {
			return _props != null ? _props.getProperty(_key)
								  : null;
		}
		public String asString(final String defVal) {
			String propVal = this.asString();
			return propVal != null ? propVal : defVal;
		}
		public Url asUrl() {
			String urlAsString = _props != null ? _props.getProperty(_key)
												: null;
			return urlAsString != null ? _translate(urlAsString) : null;
		}
		public Url asUrl(final Url defVal) {
			Url propVal = this.asUrl();
			return propVal != null ? propVal : defVal;
		}
	}
	private static void _debugProperties(final Properties props) {
		if (props == null) {
			log.warn("NO url vars");
		} else {
			if (log.isDebugEnabled()) {
				log.debug("warn vars:");
				Set<Entry<Object,Object>> entries = props.entrySet();
				for (Entry<Object,Object> entry : entries) {
					log.warn("\t-{} = {}",
							 entry.getKey(),entry.getValue());
				}
			}
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	PRIVATE METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	protected Url _translate(final String urlAsString) {
		String theTranslatedString = StringCustomizeUtils.replaceDollarDelimitedVariableValues(urlAsString,
																							   _urlVars);
		Url theTranslatedAsUrl = Url.from(theTranslatedString);
		// debug
		if (theTranslatedString.contains("$")) {
			// debug...
			log.info("Could NOT translate url {} with vars:\n" +
	 				 "{}",
	 				 urlAsString,
	 				 StringCustomizeUtils.createVariableVarsDebugInfo(_urlVars));
		} else {
			log.debug("the translated url {} ",
					 theTranslatedAsUrl);
		}
		return theTranslatedAsUrl;
	}
}
