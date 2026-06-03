package r01f.client.api.sub.security.login;

import java.util.Map;

import jakarta.inject.Provider;

import r01f.model.security.login.Login;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.api.ClientSubAPIBase;
import r01f.services.interfaces.ServiceInterface;

/**
 * @param <L> the login type
 */
public abstract class SecurityProviderLoginSubApiBase<L extends Login>
			  extends ClientSubAPIBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("rawtypes")
	public SecurityProviderLoginSubApiBase(final Provider<SecurityContext> securityContextProvider,
								  		   final Marshaller modelObjectsMarshaller,
								           final Map<Class,ServiceInterface> srvcIfaceMappings) {
		super(securityContextProvider,
			  modelObjectsMarshaller,
			  srvcIfaceMappings);
	}
}
