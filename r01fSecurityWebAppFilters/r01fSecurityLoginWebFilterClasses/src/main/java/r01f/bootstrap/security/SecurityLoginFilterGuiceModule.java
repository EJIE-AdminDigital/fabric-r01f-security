package r01f.bootstrap.security;

import java.nio.charset.Charset;

import javax.xml.transform.TransformerException;

import com.google.inject.Binder;
import com.google.inject.Provides;
import com.google.inject.Module;
import jakarta.inject.Singleton;
import r01f.guids.CommonOIDs.AppCode;
import r01f.model.annotations.ModelObjectsMarshaller;
import r01f.objectstreamer.Marshaller;
import r01f.security.login.filter.SecurityLoginFilterConfig;
import r01f.types.url.UrlPath;
import r01f.xml.XMLStringSerializer;
import r01f.xmlproperties.XMLPropertiesBuilder;
import r01f.xmlproperties.XMLPropertiesForAppComponent;


public class SecurityLoginFilterGuiceModule
  implements Module {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final AppCode _appCode;
	private final UrlPath _webAppUrlPath;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public SecurityLoginFilterGuiceModule(final AppCode appCode,
										  final UrlPath webAppUrlPath) {
		_appCode = appCode;
		_webAppUrlPath = webAppUrlPath;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	GUICE MODULE
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void configure(final Binder binder) {
		// nothing
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  SECURITY
/////////////////////////////////////////////////////////////////////////////////////////


	/**
	 * Provider "client" security login config
	 * @param props
	 * @param marshaller
	 * @return
	 * @throws TransformerException
	 */
	@Singleton
	@Provides
	SecurityLoginFilterConfig _provideLoginFilterConfig( @ModelObjectsMarshaller final Marshaller marshaller) throws TransformerException {
		 XMLPropertiesForAppComponent xmlProperties = XMLPropertiesBuilder.create()
																			.notUsingCache()
																			.forApp(_appCode)
																		  .forComponent("security.login.filter");

		 SecurityLoginFilterConfig outCfg = marshaller.forReading()
													  .fromXml(XMLStringSerializer.writeNode(xmlProperties.propertyAt("/*").node(),
																							 Charset.defaultCharset()),
															   SecurityLoginFilterConfig.class);
		 // BEWARE!! since the security config might be shared (common) by many apps (service catalog, directory, portal, etc)
		 //			 the appUrlPath MUST be "overridden" for every individual app
		 outCfg.getUrlVars()
		 	   .put("appUrlPath",_webAppUrlPath.asAbsoluteString());
		 return outCfg;
	}

}
