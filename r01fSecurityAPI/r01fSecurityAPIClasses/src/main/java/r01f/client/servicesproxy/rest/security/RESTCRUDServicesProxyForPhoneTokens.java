package r01f.client.servicesproxy.rest.security;

import r01f.api.interfaces.security.pushtoken.CRUDServicesForPushToken;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.pushtoken.IsPushDestinationID;
import r01f.model.security.pushtoken.IsPushToken;
import r01f.model.security.pushtoken.PushTokenOID;
import r01f.objectstreamer.Marshaller;
import r01f.oids.CommonOIDs.SystemID;
import r01f.services.client.servicesproxy.rest.RESTServicesForDBCRUDProxyBase;
import r01f.xmlproperties.XMLPropertiesForAppComponent;

public abstract class RESTCRUDServicesProxyForPhoneTokens<T extends IsPushToken>
	 		  extends RESTServicesForDBCRUDProxyBase<PushTokenOID,T>
  		   implements CRUDServicesForPushToken<T>,
			 		  RESTServiceProxyForSecurity {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public RESTCRUDServicesProxyForPhoneTokens(final Class<T> modelObjType,
											   final XMLPropertiesForAppComponent clientProps,
											   final Marshaller marshaller) {
		super(marshaller,
			  modelObjType,
			  new RESTServiceResourceUrlPathBuilderForPhoneTokens(clientProps));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CRUDResult<T> loadBy(final SecurityContext securityContext,
								final SystemID srcSystemId,final IsPushDestinationID destId) {
		throw new UnsupportedOperationException("Not implemented!!");
	}
}
