package r01f.client.servicesproxy.rest.security;

import r01f.client.servicesproxy.rest.security.RESTResourcePathBuilderForSecurityBase.RESTServiceResourceUrlPathBuilderForSecurityPersistableObjectBase;
import r01f.model.security.pushtoken.PushTokenOID;
import r01f.services.client.servicesproxy.rest.RESTServiceResourceUrlPathBuilders.RESTServiceEndPointUrl;
import r01f.types.url.UrlPath;
import r01f.xmlproperties.XMLPropertiesForAppComponent;

public class RESTServiceResourceUrlPathBuilderForPhoneTokens
	 extends RESTServiceResourceUrlPathBuilderForSecurityPersistableObjectBase<PushTokenOID> {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public RESTServiceResourceUrlPathBuilderForPhoneTokens(final XMLPropertiesForAppComponent clientProps) {
		super(new RESTServiceEndPointUrl(clientProps,
		      "panicButton"),
			  UrlPath.from("phoneToken"));
	}

}
