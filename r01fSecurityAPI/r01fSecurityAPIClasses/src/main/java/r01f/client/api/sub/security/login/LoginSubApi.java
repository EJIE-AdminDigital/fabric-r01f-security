package r01f.client.api.sub.security.login;

import java.util.Map;

import jakarta.inject.Provider;
import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.api.interfaces.security.login.FindServicesForLogin;
import r01f.api.interfaces.security.login.PairedPhoneLoginServices;
import r01f.api.interfaces.security.login.UserPasswordLoginServices;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.services.api.ClientSubAPIBase;
import r01f.services.interfaces.ServiceInterface;

/**
 * @param <L> the login type
 */
@Accessors(prefix="_")
public class LoginSubApi
	 extends ClientSubAPIBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	DELEGATE APIS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final ClientAPIDelegateForLoginCRUDServices _forCrud;
	@Getter private final ClientAPIDelegateForLoginFindServices _forFind;	
	@Getter private final ClientAPIDelegateForUserPasswordLoginServices _forUserPasswordLogin;
	@Getter private final ClientAPIDelegateForPairedPhoneLoginServices _forPairedPhoneLogin;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("rawtypes")
	public LoginSubApi(final Provider<SecurityContext> securityContextProvider,
			  		   final Marshaller modelObjectsMarshaller,
			           final Map<Class,ServiceInterface> srvcIfaceMappings,
			           // services
			           final Class<? extends CRUDServicesForLogin> loginCrudType,final Class<? extends FindServicesForLogin> loginFindType,
			           final Class<? extends UserPasswordLoginServices> userPasswordLoginType,
			           final Class<? extends PairedPhoneLoginServices> pairedPhoneLoginType) {
		super(securityContextProvider,
			  modelObjectsMarshaller,
			  srvcIfaceMappings);
		// Delegate for CRUD
		_forCrud = new ClientAPIDelegateForLoginCRUDServices(securityContextProvider,
								                     	  	 modelObjectsMarshaller,
								                     	  	 (CRUDServicesForLogin)srvcIfaceMappings.get(loginCrudType));
		  // Delegate for Find.
		_forFind = new ClientAPIDelegateForLoginFindServices(securityContextProvider,
										             	  	 modelObjectsMarshaller,
										             	  	 (FindServicesForLogin)srvcIfaceMappings.get(loginFindType));
		// Delegate for UserPasswordLogin
		if (userPasswordLoginType != null) {
			_forUserPasswordLogin = new ClientAPIDelegateForUserPasswordLoginServices(securityContextProvider,
																					  modelObjectsMarshaller,
																					  (UserPasswordLoginServices)srvcIfaceMappings.get(userPasswordLoginType));
		} else {
			_forUserPasswordLogin = null;
		}
		// Delegate for PairedPhoneLogin
		if (pairedPhoneLoginType != null) {
			_forPairedPhoneLogin = new ClientAPIDelegateForPairedPhoneLoginServices(securityContextProvider,
																					modelObjectsMarshaller,
																					(PairedPhoneLoginServices)srvcIfaceMappings.get(pairedPhoneLoginType));
		} else {
			_forPairedPhoneLogin = null;
		}
	}
}
