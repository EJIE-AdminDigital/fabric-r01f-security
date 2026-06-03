package r01f.client.api.sub.security.authorization;

import java.util.Map;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.model.security.authorization.AuthorizationTargetResource;
import r01f.model.security.summaries.SummarizedAuthorizationResource;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.api.ClientSubAPIBase;
import r01f.services.interfaces.ServiceInterface;

@Singleton
@Accessors(prefix="_")
public class AuthorizationTargetResourceSubApiBase<R extends AuthorizationTargetResource,S extends SummarizedAuthorizationResource<R>,
												   C extends ClientAPIDelegateForAuthorizationTargetResourceCRUDServices<R>,
												   F extends ClientAPIDelegateForAuthorizationTargetResourceFindServices<R,S>,
												   T extends ClientAPIDelegateForAuthorizationTargetResourceCountServices<R>>
	extends ClientSubAPIBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final C _forCrud;
	@Getter private final F _forFind;
	@Getter private final T _forCount;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes" })
	public AuthorizationTargetResourceSubApiBase(final Provider<SecurityContext>  securityContextProvider,
							   		  			 final Marshaller marshaller,
							   		  			 final Map<Class,ServiceInterface> usersServiceInterfaceTypesToImplOrProxy,
							   		  			 //	services
							   		  			 final C crudForResourceDelegate,
							   		  			 final F findForResourceDelegate,
							   		  			 final T countForResourceDelegate) {

		super(securityContextProvider,
			  marshaller,
			  usersServiceInterfaceTypesToImplOrProxy);	// reference to other client apis

		_forCrud = crudForResourceDelegate;
		_forFind = findForResourceDelegate;
		_forCount = countForResourceDelegate;
	}
}
