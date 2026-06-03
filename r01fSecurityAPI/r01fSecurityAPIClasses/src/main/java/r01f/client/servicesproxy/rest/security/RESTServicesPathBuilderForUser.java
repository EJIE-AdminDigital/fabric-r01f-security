package r01f.client.servicesproxy.rest.security;

import r01f.client.servicesproxy.rest.security.RESTResourcePathBuilderForSecurityBase.RESTServiceResourceUrlPathBuilderForSecurityPersistableObjectBase;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.client.servicesproxy.rest.RESTServiceResourceUrlPathBuilders.RESTServiceEndPointUrl;
import r01f.types.contact.EMail;
import r01f.types.contact.Phone;
import r01f.types.url.UrlPath;
import r01f.xmlproperties.XMLPropertiesForAppComponent;

public class RESTServicesPathBuilderForUser
	 extends RESTServiceResourceUrlPathBuilderForSecurityPersistableObjectBase<UserOID> {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public RESTServicesPathBuilderForUser(final XMLPropertiesForAppComponent clientProps) {
		super(new RESTServiceEndPointUrl(clientProps,
										 "users"),
										 UrlPath.from("users"));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	public UrlPath pathOfEntityByLoginId(final SecurityProviderID securityProviderId,final LoginID loginId) {
		return this.pathOfAllEntities().joinedWith("byLoginId",
												   securityProviderId,loginId);
	}
	public UrlPath pathOfEntityByEMail(final EMail email) {
		return this.pathOfAllEntities().joinedWith("byEMail",
												   email);
	}
	public UrlPath pathOfEntityByPhone(final Phone phone) {
		return this.pathOfAllEntities().joinedWith("byPhone",
												   phone);
	}
}