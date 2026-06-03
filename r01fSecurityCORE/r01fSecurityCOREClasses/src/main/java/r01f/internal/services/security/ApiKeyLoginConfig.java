package r01f.internal.services.security;

import java.nio.charset.Charset;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.config.ContainsConfigData;
import r01f.debug.Debuggable;
import r01f.objectstreamer.Marshaller;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallFrom;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.AppVersion;
import r01f.util.types.Strings;
import r01f.validation.ObjectValidationResult;
import r01f.validation.ObjectValidationResultBuilder;
import r01f.validation.SelfValidates;
import r01f.xml.XMLStringSerializer;
import r01f.xmlproperties.XMLPropertiesForAppComponent;


@MarshallType(as="apiKeyLoginConfig")
@Slf4j
@Accessors(prefix="_")
public class ApiKeyLoginConfig
  implements Debuggable,
	         ContainsConfigData,
	         SelfValidates<ApiKeyLoginConfig> {
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS..properties....
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="appLastVersion")
	@Getter @Setter private AppVersion _apiKeyLastVersion;

	@MarshallField(as="disableApiKeyFactsAccounting",
			   whenXml=@MarshallFieldAsXml(attr=true))
    @Getter @Setter private Boolean  _disableApiKeyFactsAccounting = Boolean.FALSE;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public ApiKeyLoginConfig() {
		// default NO-args constructor
	}
	public ApiKeyLoginConfig(@MarshallFrom("appLastVersion") final AppVersion apiKeyLastVarsion,
							 @MarshallFrom("disableApiKeyFactsAccounting") final boolean disableApiKeyFactsAccounting) {
		_apiKeyLastVersion = apiKeyLastVarsion;
		_disableApiKeyFactsAccounting = disableApiKeyFactsAccounting;
	}
	public static ApiKeyLoginConfigLoadStep loadUsing(final Marshaller marshaller) {
		return new ApiKeyLoginConfigLoadStep(marshaller);
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public static class ApiKeyLoginConfigLoadStep {
		private final Marshaller _marsaller;

		public ApiKeyLoginConfig from(final XMLPropertiesForAppComponent xmlProps) {
			ApiKeyLoginConfig outConfig = null;
			try {
				ApiKeyLoginConfig apiKeyLoginConfig =_marsaller.forReading()
														   		.fromXml(XMLStringSerializer.writeNode(xmlProps.propertyAt("/*").node(),Charset.defaultCharset()),
														   				 ApiKeyLoginConfig.class);
				outConfig = apiKeyLoginConfig;
			} catch (Throwable th) {
				log.error("Unknown error while loading [api key login] config: {}",
						  th.getMessage(),th);
			}
			return outConfig;
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  METHODS
/////////////////////////////////////////////////////////////////////////////////////////
 	@Override
	public ObjectValidationResult<ApiKeyLoginConfig> validate() {
 		boolean valid =  (_apiKeyLastVersion != null );
		return valid ? ObjectValidationResultBuilder.on(this)
												   .isValid()
					 : ObjectValidationResultBuilder.on(this)
												   .isNotValidBecause(" Some of the client configurarion are not valid, fe {}",_apiKeyLastVersion);


	}
/////////////////////////////////////////////////////////////////////////////////////////
//	DEBUG
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CharSequence debugInfo() {
		return Strings.customized("[api key config]: last version id={} disableFactsAccounting={}",
								  _apiKeyLastVersion,_disableApiKeyFactsAccounting);
	}
}
