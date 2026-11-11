package r01f.client.servicesproxy.rest.security;

import r01f.client.servicesproxy.rest.security.RESTResourcePathBuilderForSecurityBase.RESTServiceResourceUrlPathBuilderForSecurityPersistableObjectBase;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.SecurityPrincipalID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.client.servicesproxy.rest.RESTServiceResourceUrlPathBuilders.RESTServiceEndPointUrl;
import r01f.types.contact.EMail;
import r01f.types.url.UrlPath;
import r01f.xmlproperties.XMLPropertiesForAppComponent;

public class RESTServicesPathBuilderForSecurityPrincipal
	 extends RESTServiceResourceUrlPathBuilderForSecurityPersistableObjectBase<SecurityPrincipalOID> {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public RESTServicesPathBuilderForSecurityPrincipal(final XMLPropertiesForAppComponent clientProps) {
		super(new RESTServiceEndPointUrl(clientProps,
										 "principals"),
										 UrlPath.from("principals"));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	public UrlPath pathOfEntityByPrincipalId(final IdentityProvider securityProviderId,final SecurityPrincipalID principalId) {
		return this.pathOfAllEntities().joinedWith("byPrincipalId",
												   securityProviderId,principalId);
	}
	public UrlPath pathOfEntityByEMail(final EMail principalEmail) {
		return this.pathOfAllEntities().joinedWith("byEmail",
												   principalEmail);
	}
}