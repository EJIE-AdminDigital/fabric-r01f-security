package r01f.client.servicesproxy.rest.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.oids.OID;
import r01f.services.client.servicesproxy.rest.RESTServiceResourceUrlPathBuilders.RESTServiceEndPointUrl;
import r01f.services.client.servicesproxy.rest.RESTServiceResourceUrlPathBuilders.RESTServiceResourceUrlPathBuilderBase;
import r01f.services.client.servicesproxy.rest.RESTServiceResourceUrlPathBuilders.RESTServiceResourceUrlPathBuilderForModelObjectPersistenceBase;
import r01f.types.url.UrlPath;

/**
 * Base types for REST resources path building
 */
@NoArgsConstructor(access=AccessLevel.PRIVATE)
abstract class RESTResourcePathBuilderForSecurityBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public static class RESTServiceResourceUrlPathBuilderForSecurityBase
			    extends RESTServiceResourceUrlPathBuilderBase {
		public RESTServiceResourceUrlPathBuilderForSecurityBase(final RESTServiceEndPointUrl endPointUrl,
																final UrlPath resourceUrlPath) {
			super(endPointUrl,
				  resourceUrlPath);
		}
	}
	public static class RESTServiceResourceUrlPathBuilderForSecurityPersistableObjectBase<O extends OID>
				extends RESTServiceResourceUrlPathBuilderForModelObjectPersistenceBase<O> {
		public RESTServiceResourceUrlPathBuilderForSecurityPersistableObjectBase(final RESTServiceEndPointUrl endPointUrl,
																   				 final UrlPath resource) {
			super(endPointUrl,
				  resource);
		}
	}
}
