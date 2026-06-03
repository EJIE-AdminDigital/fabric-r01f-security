package r01f.client.servicesproxy.rest.security;

import r01f.api.interfaces.security.user.FindServicesForUser;
import r01f.model.persistence.FindResult;
import r01f.model.security.user.User;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.client.servicesproxy.rest.RESTServicesForDBFindProxyBase;
import r01f.types.url.Url;
import r01f.types.url.UrlQueryString;
import r01f.types.url.UrlQueryStringParam;

public abstract class RESTFindServicesProxyForUser<U extends User>
	 		  extends RESTServicesForDBFindProxyBase<UserOID,U>
  		   implements FindServicesForUser<U>,
  		   			  RESTServiceProxyForSecurity {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public RESTFindServicesProxyForUser(final Class<U> modelObjectType,
										final Marshaller marshaller) {
		super(marshaller,
			  modelObjectType,
			  new RESTServicesPathBuilderForUser(null));	// TODO load properties
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public FindResult<U> findAllUsers(final SecurityContext securityContext) {
		Url restResourceUrl = this.composeURIFor(this.getServicesRESTResourceUrlPathBuilderAs(RESTServicesPathBuilderForUser.class)
								  .pathOfEntityList());
		return _findDelegate.doFindEntities(securityContext,restResourceUrl);
	}
	@Override
	public FindResult<U> findUsersWithSystemWideRole(final SecurityContext securityContext) {
		Url restResourceUrl = this.composeURIFor(this.getServicesRESTResourceUrlPathBuilderAs(RESTServicesPathBuilderForUser.class)
								  .pathOfAllEntities())
								  .joinWith(UrlQueryString.fromParams(UrlQueryStringParam.of("hasSystemWideRole",true)));
		return _findDelegate.doFindEntities(securityContext,restResourceUrl);
	}
	@Override
	public FindResult<U> findByText(final SecurityContext securityContext,
									final String text) {
		Url restResourceUrl = this.composeURIFor(this.getServicesRESTResourceUrlPathBuilderAs(RESTServicesPathBuilderForUser.class)
								  .pathOfAllEntities())
								  .joinWith(UrlQueryString.fromParams(UrlQueryStringParam.of("fullText",text)));
		return _findDelegate.doFindEntities(securityContext,restResourceUrl);
	}
}
