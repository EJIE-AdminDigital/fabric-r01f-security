package r01f.client.servicesproxy.rest.security;

import r01f.api.interfaces.security.principal.CRUDServicesForPrincipal;
import r01f.httpclient.HttpResponseResult;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.PersistenceRequestedOperation;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.SecurityPrincipalID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.client.servicesproxy.rest.DelegateForRawREST;
import r01f.services.client.servicesproxy.rest.RESTServicesForDBCRUDProxyBase;
import r01f.types.contact.EMail;
import r01f.types.url.Url;
import r01f.util.types.Strings;

public abstract class RESTCRUDServicesProxyForSecurityPrincipal<P extends IsSecurityPrincipal>
	 		  extends RESTServicesForDBCRUDProxyBase<SecurityPrincipalOID,P>
  		   implements CRUDServicesForPrincipal<P>,
  		   			  RESTServiceProxyForSecurity {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public RESTCRUDServicesProxyForSecurityPrincipal(final Class<P> modelObjectType,
								 					 final Marshaller marshaller) {
		super(marshaller,
			  modelObjectType,
			  new RESTServicesPathBuilderForSecurityPrincipal(null));	// TODO load properties
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LOAD
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CRUDResult<P> loadBy(final SecurityContext securityContext,
								final IdentityProvider securityProviderId,final SecurityPrincipalID principalId) {
		Url restResourceUrl = this.composeURIFor(this.getServicesRESTResourceUrlPathBuilderAs(RESTServicesPathBuilderForSecurityPrincipal.class)
													 .pathOfEntityByPrincipalId(securityProviderId,principalId));
		String ctxXml = _marshaller.forWriting().toXml(securityContext);
		HttpResponseResult httpResponse = DelegateForRawREST.GET(restResourceUrl,
										 				         ctxXml);		// map the response
		CRUDResult<P> outResponse = this.getResponseToCRUDResultMapperForModelObject()
										.mapHttpResponseForEntity(securityContext,
																  PersistenceRequestedOperation.LOAD,
																  restResourceUrl,
																  httpResponse)
										.identifiedOnErrorBy(Strings.customized("{}/{}",
																				securityProviderId,principalId));
		// log & return
		_logResponse(restResourceUrl,outResponse);
		return outResponse;
	}
	@Override
	public CRUDResult<P> loadBy(final SecurityContext securityContext,
								final EMail principalEmail) {
		Url restResourceUrl = this.composeURIFor(this.getServicesRESTResourceUrlPathBuilderAs(RESTServicesPathBuilderForSecurityPrincipal.class)
													 .pathOfEntityByEMail(principalEmail));
		String ctxXml = _marshaller.forWriting().toXml(securityContext);
		HttpResponseResult httpResponse = DelegateForRawREST.GET(restResourceUrl,
										 				         ctxXml);		// map the response
		CRUDResult<P> outResponse = this.getResponseToCRUDResultMapperForModelObject()
										.mapHttpResponseForEntity(securityContext,
																  PersistenceRequestedOperation.LOAD,
																  restResourceUrl,
																  httpResponse)
										.identifiedOnErrorBy(Strings.customized("{}",
																				principalEmail));
		// log & return
		_logResponse(restResourceUrl,outResponse);
		return outResponse;
	}
}
