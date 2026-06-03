package r01f.model.security.webauthn.config;



import java.util.Collection;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.config.ContainsConfigData;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.patterns.Memoized;
import r01f.securitycontext.SecurityIDS.SecurityToken;
import r01f.types.url.Url;

@MarshallType(as="webAuthnConfig")
@Accessors(prefix="_")
@NoArgsConstructor
public class WebAuthnConfig
	 extends WebAuthnConfigBase
  implements ContainsConfigData {
/////////////////////////////////////////////////////////////////////////////////////////
//	SECRET key used to create JWT tokens after webauthn assertion( authentication)
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="keyData")
	@Getter @Setter private SecurityToken _keyData;
/////////////////////////////////////////////////////////////////////////////////////////
//	Security provider (ie google, xlnets, etc) urls
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Base URIS for flow
	 */
	@MarshallField(as="authUrl")
	@Setter private String _authUrl;
	private final transient Memoized<Url> _translatedLoginUrl = new Memoized<>() {
																		@Override
																		public Url supply() {
																			return _translate(_authUrl);
																		}
																};
	public Url getLoginUrl() {
		return _translatedLoginUrl.get();
	}

	/**
	 * Origins
	 */
	@MarshallField(as="origins")
	@Setter private Collection<String> _origins;
	private final transient Memoized<Collection<Url>> _translatedOriginsUrl = new Memoized<>() {
																						@Override
																						public Collection<Url> supply() {
																						  return	_origins.stream()
																									.map( o -> _translate(o)).collect(Collectors.toList());
																						}
	};

	public Collection<Url> getOrigins() {
		return _translatedOriginsUrl.get();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
}
