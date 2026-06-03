package r01f.security.provider;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.config.ContainsConfigData;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.patterns.Memoized;
import r01f.types.url.Url;

@MarshallType(as="securityProviderPublicConfig")
@Accessors(prefix="_")
@NoArgsConstructor
public class SecurityProviderPublicConfig
     extends SecurityProviderConfigBase
  implements ContainsConfigData {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="enabled")
	@Getter @Setter private boolean _enabled = true;
	/**
	 * The application login url
	 * BEWARE!!	Do NOT confuse with the one of the [security provider] (ie google, xlnets, etc)
	 * 			This url is the APPLICATION one where all the [security provider]s login options are listed
	 * 			(google, xlnets, user / password, etc)
	 */
	@MarshallField(as="loginUrl")
	@Setter String _loginUrl;
	private final transient Memoized<Url> _translatedLoginUrl = new Memoized<>() {
																		@Override
																		public Url supply() {
																			return _translate(_loginUrl);
																		}
																};
	public Url getLoginUrl() {
		return _translatedLoginUrl.get();
	}
	/**
	 * The LOCAL endpoint where the [user data] is exposed
	 * The LOCAL [user data] might have been created from the [security provider]'s 
	 * user data, BUT it's NOT the same! 
	 */
	@MarshallField(as="userDataFetchUrl")
	@Setter private String _userDataFetchUrl;
	private final transient Memoized<Url> _translatedUserDataFetchUrl = new Memoized<>() {
																				@Override
																				public Url supply() {
																					return _translate(_userDataFetchUrl);
																				}
																		};
	public Url getUserDataFetchUrl() {
		return _translatedUserDataFetchUrl.get();
	}
}
