package r01f.client.servicesproxy.rest.security;

import r01f.api.interfaces.security.user.CRUDServicesForUser;
import r01f.httpclient.HttpResponseResult;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.PersistenceRequestedOperation;
import r01f.model.security.user.User;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.client.servicesproxy.rest.DelegateForRawREST;
import r01f.services.client.servicesproxy.rest.RESTServicesForDBCRUDProxyBase;
import r01f.types.contact.EMail;
import r01f.types.contact.Phone;
import r01f.types.url.Url;
import r01f.util.types.Strings;

public abstract class RESTCRUDServicesProxyForUser<U extends User>
	 		  extends RESTServicesForDBCRUDProxyBase<UserOID,U>
  		   implements CRUDServicesForUser<U>,
  		   			  RESTServiceProxyForSecurity {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public RESTCRUDServicesProxyForUser(final Class<U> modelObjectType,
								 		final Marshaller marshaller) {
		super(marshaller,
			  modelObjectType,
			  new RESTServicesPathBuilderForUser(null));	// TODO load properties
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LOAD
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CRUDResult<U> loadBy(final SecurityContext securityContext,
								final SecurityProviderID securityProviderId,final LoginID loginId) {
		Url restResourceUrl = this.composeURIFor(this.getServicesRESTResourceUrlPathBuilderAs(RESTServicesPathBuilderForUser.class)
													 .pathOfEntityByLoginId(securityProviderId,loginId));
		String ctxXml = _marshaller.forWriting().toXml(securityContext);
		HttpResponseResult httpResponse = DelegateForRawREST.GET(restResourceUrl,
										 				         ctxXml);		// map the response
		CRUDResult<U> outResponse = this.getResponseToCRUDResultMapperForModelObject()
										.mapHttpResponseForEntity(securityContext,
																  PersistenceRequestedOperation.LOAD,
																  restResourceUrl,
																  httpResponse)
										.identifiedOnErrorBy(Strings.customized("{}/{}",
																				securityProviderId,loginId));
		// log & return
		_logResponse(restResourceUrl,outResponse);
		return outResponse;
	}

	@Override
	public CRUDResult<U> loadBy(final SecurityContext securityContext,
	  							final EMail email) {
		Url restResourceUrl = this.composeURIFor(this.getServicesRESTResourceUrlPathBuilderAs(RESTServicesPathBuilderForUser.class)
													 .pathOfEntityByEMail(email));
		String ctxXml = _marshaller.forWriting().toXml(securityContext);
		HttpResponseResult httpResponse = DelegateForRawREST.GET(restResourceUrl,
														         ctxXml);		// map the response
		CRUDResult<U> outResponse = this.getResponseToCRUDResultMapperForModelObject()
										.mapHttpResponseForEntity(securityContext,
																  PersistenceRequestedOperation.LOAD,
																  restResourceUrl,
																  httpResponse)
										.identifiedOnErrorBy(email.asString());
		// log & return
		_logResponse(restResourceUrl,outResponse);
		return outResponse;
	}
	@Override
	public CRUDResult<U> loadBy(final SecurityContext securityContext,
								final Phone phone) {
		Url restResourceUrl = this.composeURIFor(this.getServicesRESTResourceUrlPathBuilderAs(RESTServicesPathBuilderForUser.class)
													 .pathOfEntityByPhone(phone));
		String ctxXml = _marshaller.forWriting().toXml(securityContext);
		HttpResponseResult httpResponse = DelegateForRawREST.GET(restResourceUrl,
														         ctxXml);		// map the response
		CRUDResult<U> outResponse = this.getResponseToCRUDResultMapperForModelObject()
										.mapHttpResponseForEntity(securityContext,
																  PersistenceRequestedOperation.LOAD,
																  restResourceUrl,
																  httpResponse)
										.identifiedOnErrorBy(phone.asString());
		// log & return
		_logResponse(restResourceUrl,outResponse);
		return outResponse;
	}
}
