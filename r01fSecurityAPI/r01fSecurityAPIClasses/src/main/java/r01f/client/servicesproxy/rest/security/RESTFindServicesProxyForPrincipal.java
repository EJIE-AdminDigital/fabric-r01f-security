package r01f.client.servicesproxy.rest.security;

import r01f.api.interfaces.security.principal.FindServicesForPrincipal;
import r01f.model.persistence.FindResult;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.client.servicesproxy.rest.RESTServicesForDBFindProxyBase;
import r01f.types.url.Url;
import r01f.types.url.UrlQueryString;
import r01f.types.url.UrlQueryStringParam;

public abstract class RESTFindServicesProxyForPrincipal<P extends IsSecurityPrincipal>
	 		  extends RESTServicesForDBFindProxyBase<SecurityPrincipalOID,P>
  		   implements FindServicesForPrincipal<P>,
  		   			  RESTServiceProxyForSecurity {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public RESTFindServicesProxyForPrincipal(final Class<P> modelObjectType,
											 final Marshaller marshaller) {
		super(marshaller,
			  modelObjectType,
			  new RESTServicesPathBuilderForSecurityPrincipal(null));	// TODO load properties
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public FindResult<P> findAllPrincipals(final SecurityContext securityContext) {
		Url restResourceUrl = this.composeURIFor(this.getServicesRESTResourceUrlPathBuilderAs(RESTServicesPathBuilderForSecurityPrincipal.class)
								  .pathOfEntityList());
		return _findDelegate.doFindEntities(securityContext,restResourceUrl);
	}
	@Override
	public FindResult<P> findHavingAnySystemWideRole(final SecurityContext securityContext) {
		Url restResourceUrl = this.composeURIFor(this.getServicesRESTResourceUrlPathBuilderAs(RESTServicesPathBuilderForSecurityPrincipal.class)
								  .pathOfAllEntities())
								  .joinWith(UrlQueryString.fromParams(UrlQueryStringParam.of("hasSystemWideRole",true)));
		return _findDelegate.doFindEntities(securityContext,restResourceUrl);
	}
	@Override
	public FindResult<P> findByText(final SecurityContext securityContext,
									final String text) {
		Url restResourceUrl = this.composeURIFor(this.getServicesRESTResourceUrlPathBuilderAs(RESTServicesPathBuilderForSecurityPrincipal.class)
								  .pathOfAllEntities())
								  .joinWith(UrlQueryString.fromParams(UrlQueryStringParam.of("fullText",text)));
		return _findDelegate.doFindEntities(securityContext,restResourceUrl);
	}
}
