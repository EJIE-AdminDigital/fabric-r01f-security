package r01f.client.servicesproxy.rest.security;

import r01f.api.interfaces.security.pushtoken.CRUDServicesForPushToken;
import r01f.guids.CommonOIDs.SystemID;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.pushtoken.PushToken;
import r01f.model.security.pushtoken.PushTokenID;
import r01f.model.security.pushtoken.PushTokenOID;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.client.servicesproxy.rest.RESTServicesForDBCRUDProxyBase;
import r01f.types.contact.Phone;
import r01f.xmlproperties.XMLPropertiesForAppComponent;

public abstract class RESTCRUDServicesProxyForPhoneTokens<T extends PushToken>
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
								final  PushTokenID id, 
								final SystemID systemOid) {
		throw new UnsupportedOperationException("Not implemented!!");
	}
}
