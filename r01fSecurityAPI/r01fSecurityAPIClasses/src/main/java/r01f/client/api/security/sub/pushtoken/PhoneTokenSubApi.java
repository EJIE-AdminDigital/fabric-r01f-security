package r01f.client.api.security.sub.pushtoken;

import java.util.Map;

import jakarta.inject.Provider;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.pushtoken.CRUDServicesForPushToken;
import r01f.api.interfaces.security.pushtoken.CountServicesForPushToken;
import r01f.api.interfaces.security.pushtoken.FindServicesForPushToken;
import r01f.model.security.pushtoken.PushToken;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.api.ClientSubAPIBase;
import r01f.services.interfaces.ServiceInterface;

@Accessors(prefix="_")
public class PhoneTokenSubApi<T extends PushToken>
	 extends ClientSubAPIBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	DELEGATE APIS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final ClientAPIDelegateForPushTokenCRUDServices<T> _forCrud;
	@Getter private final ClientAPIDelegateForPushTokenFindServices<T> _forFind;
	@Getter private final ClientAPIDelegateForPushTokenCountServices<T> _forCount;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes","unchecked" })
	public PhoneTokenSubApi(final Provider<SecurityContext>  securityContextProvider,
			   		  		final Marshaller marshaller,
			   		  		final Map<Class,ServiceInterface> serviceInterfaceTypesToImplOrProxy,
			   		  		//	services
			   		  		final Class<? extends CRUDServicesForPushToken<T>> crudForPhoneTokenType,
			   		  		final Class<? extends FindServicesForPushToken<T>> findForPhoneTokenType,
			   		  		final Class<? extends CountServicesForPushToken<T>> countForPhoneTokenType) {
		this(securityContextProvider,
			 marshaller,
			 serviceInterfaceTypesToImplOrProxy,
			 // services);
			 (CRUDServicesForPushToken<T>)serviceInterfaceTypesToImplOrProxy.get(crudForPhoneTokenType),
			 (FindServicesForPushToken<T>)serviceInterfaceTypesToImplOrProxy.get(findForPhoneTokenType),
			 (CountServicesForPushToken<T>)serviceInterfaceTypesToImplOrProxy.get(countForPhoneTokenType));
	}
	@SuppressWarnings({ "rawtypes" })
	public PhoneTokenSubApi(final Provider<SecurityContext>  securityContextProvider,
			   		  		final Marshaller marshaller,
			   		  		final Map<Class,ServiceInterface> serviceInterfaceTypesToImplOrProxy,
			   		  		//	services
			   		  		final CRUDServicesForPushToken<T> crudForPhoneToken,
			   		  		final FindServicesForPushToken<T> findForPhoneToken,
			   		  		final CountServicesForPushToken<T> countForPhoneToken) {
		super(securityContextProvider,
			  marshaller,
			  serviceInterfaceTypesToImplOrProxy);
		_forCrud = new ClientAPIDelegateForPushTokenCRUDServices<>(securityContextProvider,
																	marshaller,
																	crudForPhoneToken);
		_forFind = new ClientAPIDelegateForPushTokenFindServices<>(securityContextProvider,
																	marshaller,
																	findForPhoneToken);
		_forCount = new ClientAPIDelegateForPushTokenCountServices<>(securityContextProvider,
																	  marshaller,
																	  countForPhoneToken);
	}
}
