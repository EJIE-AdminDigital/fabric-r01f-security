package r01f.client.api.sub.security.principal.impersonation;

import java.util.Map;

import jakarta.inject.Provider;
import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.principal.impersonation.CRUDServicesForPrincipalImpersonation;
import r01f.api.interfaces.security.principal.impersonation.FindServicesForPrincipalImpersonation;
import r01f.api.interfaces.security.principal.impersonation.SecurityPrincipalImpersonationServices;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.services.api.ClientSubAPIBase;
import r01f.services.interfaces.ServiceInterface;

@Accessors(prefix="_")
public class PrincipalImpersonationSubApi
	 extends ClientSubAPIBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	DELEGATE APIS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final ClientAPIDelegateForPrincipalImpersonationCRUDServices _forCrud;
	@Getter private final ClientAPIDelegateForPrincipalImpersonationFindServices _forFind;	
	@Getter private final ClientAPIDelegateForPrincipalImpersonationServices _forImpersonation;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("rawtypes")
	public PrincipalImpersonationSubApi(final Provider<SecurityContext> securityContextProvider,
				  		   				final Marshaller modelObjectsMarshaller,
				  		   				final Map<Class,ServiceInterface> srvcIfaceMappings,
				  		   				// services
				  		   				final Class<? extends CRUDServicesForPrincipalImpersonation> impersonationCrudType,final Class<? extends FindServicesForPrincipalImpersonation> impersonationFindType,final Class<? extends SecurityPrincipalImpersonationServices> impersonationServicesType) {
		super(securityContextProvider,
			  modelObjectsMarshaller,
			  srvcIfaceMappings);
		// Delegate for CRUD
		_forCrud = new ClientAPIDelegateForPrincipalImpersonationCRUDServices(securityContextProvider,
									                     	  	   			  modelObjectsMarshaller,
									                     	  	   			  (CRUDServicesForPrincipalImpersonation)srvcIfaceMappings.get(impersonationCrudType));
		  // Delegate for Find.
		_forFind = new ClientAPIDelegateForPrincipalImpersonationFindServices(securityContextProvider,
																			  modelObjectsMarshaller,
																			  (FindServicesForPrincipalImpersonation)srvcIfaceMappings.get(impersonationFindType));
		// Delegate for Services
		_forImpersonation = new ClientAPIDelegateForPrincipalImpersonationServices(securityContextProvider,
																				   modelObjectsMarshaller,
																				   (SecurityPrincipalImpersonationServices)srvcIfaceMappings.get(impersonationServicesType));
	}
}
