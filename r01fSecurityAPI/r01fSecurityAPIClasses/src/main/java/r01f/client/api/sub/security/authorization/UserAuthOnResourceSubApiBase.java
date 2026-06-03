package r01f.client.api.sub.security.authorization;

import java.util.Map;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.model.security.authorization.UserAuthorizationOnResource;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.api.ClientSubAPIBase;
import r01f.services.interfaces.ServiceInterface;

@Singleton
@Accessors(prefix="_")
public class UserAuthOnResourceSubApiBase<A extends UserAuthorizationOnResource,
										  C extends ClientAPIDelegateForUserAuthorizationOnResourceCRUDServices<A>,
										  F extends ClientAPIDelegateForUserAuthorizationOnResourceFindServices<A>,
										  S extends ClientAPIDelegateForUserAuthorizationOnResourceCountServices<A>>
	 extends ClientSubAPIBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final C _forCrud;
	@Getter private final F _forFind;
	@Getter private final S _forCount;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes" })
	public UserAuthOnResourceSubApiBase(final Provider<SecurityContext>  securityContextProvider,
							   		    final Marshaller marshaller,
							   		    final Map<Class,ServiceInterface> usersServiceInterfaceTypesToImplOrProxy,
								   		// services
								   		final C crudForUserAuthOnResourceTypeDelegate,
								   		final F findForUserAuthOnResourceTypeDelegate,
								   		final S countForUserAuthOnResourceTypeDelegate) {

		super(securityContextProvider,
			  marshaller,
			  usersServiceInterfaceTypesToImplOrProxy);	// reference to other client apis
		_forCrud = crudForUserAuthOnResourceTypeDelegate;
		_forFind = findForUserAuthOnResourceTypeDelegate;
		_forCount = countForUserAuthOnResourceTypeDelegate;
	}
}
