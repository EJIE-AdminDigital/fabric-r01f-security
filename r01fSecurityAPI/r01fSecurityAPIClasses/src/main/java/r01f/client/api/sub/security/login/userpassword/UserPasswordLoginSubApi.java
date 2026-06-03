package r01f.client.api.sub.security.login.userpassword;

import java.util.Map;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import lombok.experimental.Accessors;
import r01f.api.interfaces.security.login.userpassword.CRUDServicesForUserPasswordLogin;
import r01f.api.interfaces.security.login.userpassword.FindServicesForUserPasswordLogin;
import r01f.api.interfaces.security.login.userpassword.UserPasswordLoginServices;
import r01f.model.security.login.userpassword.UserPasswordLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.interfaces.ServiceInterface;

/**
 * Client implementation of the User Login Config
 */
@Singleton
@Accessors(prefix="_")
public class UserPasswordLoginSubApi<L extends UserPasswordLogin>
	 extends UserPasswordLoginSubApiBase<L,ClientAPIDelegateForUserPasswordLoginCRUDServices<L>,ClientAPIDelegateForUserPasswordLoginFindServices<L>,ClientAPIDelegateForUserPasswordLoginServices<?,L>> {
	// a sub API uses the proxy to access the services layer
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public UserPasswordLoginSubApi(final Provider<SecurityContext>  securityContextProvider,
							   	   final Marshaller modelObjectsMarshaller,
							   	   final Map<Class,ServiceInterface> serviceInterfaceTypesToImplOrProxy,
							   	   final Class<? extends CRUDServicesForUserPasswordLogin<L>> crudType,
							   	   final Class<? extends FindServicesForUserPasswordLogin<L>> findType,
							   	   final Class<? extends UserPasswordLoginServices> loginType)  {
		super(securityContextProvider,
		      modelObjectsMarshaller,
		      // Reference to other client apis
		      serviceInterfaceTypesToImplOrProxy,
		      // Delegate for CRUD
		      new ClientAPIDelegateForUserPasswordLoginCRUDServices<>(securityContextProvider,
								                         			  modelObjectsMarshaller,
								                                      (CRUDServicesForUserPasswordLogin<L>)serviceInterfaceTypesToImplOrProxy.get(crudType)),
		      // Delegate for Find.
			  new ClientAPIDelegateForUserPasswordLoginFindServices<>(securityContextProvider,
										                     		  modelObjectsMarshaller,
										                              (FindServicesForUserPasswordLogin<L>)serviceInterfaceTypesToImplOrProxy.get(findType)),

			  // Delegate for Login Services
			  new ClientAPIDelegateForUserPasswordLoginServices(securityContextProvider,
										                        modelObjectsMarshaller,
										                        (UserPasswordLoginServices)serviceInterfaceTypesToImplOrProxy.get(loginType)));
	}

}
