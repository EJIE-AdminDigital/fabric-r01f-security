package r01f.client.api.sub.security.principal;

import java.util.Map;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;
import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.principal.CRUDServicesForPrincipal;
import r01f.api.interfaces.security.principal.FindServicesForPrincipal;
import r01f.api.interfaces.security.principal.SearchServicesForPrincipal;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.principal.SearchFilterForSecurityPrincipalBase;
import r01f.model.security.principal.SearchResultItemForSecurityPrincipalBase;
import r01f.objectstreamer.Marshaller;
import r01f.services.api.ClientSubAPIBase;
import r01f.services.interfaces.ServiceInterface;

/**
 * Client implementation of the Users
 */
@Singleton
@Accessors(prefix="_")
public class PrincipalSubApi<P extends IsSecurityPrincipal>
	 extends ClientSubAPIBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	DELEGATE APIS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final ClientAPIDelegateForPrincipalCRUDServices<P> _forCrud;
	@Getter private final ClientAPIDelegateForPrincipalFindServices<P> _forFind;
	@Getter private final ClientAPIDelegateForPrincipalSearchServices<P,
															  		  ? extends SearchFilterForSecurityPrincipalBase<P,?>,
															  		  ? extends SearchResultItemForSecurityPrincipalBase<P>> _forSearch;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes" })
	public PrincipalSubApi(final Provider<SecurityContext>  securityContextProvider,
						   final Marshaller modelObjectsMarshaller,
						   final Map<Class,ServiceInterface> usersServiceInterfaceTypesToImplOrProxy,
						   final Class<? extends CRUDServicesForPrincipal<P>> crudType,
						   final Class<? extends FindServicesForPrincipal<P>> findType) {

		this(securityContextProvider,
			 modelObjectsMarshaller,
			 // Reference to other client apis
			 usersServiceInterfaceTypesToImplOrProxy,
			 crudType,findType,null);	// no search
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PrincipalSubApi(final Provider<SecurityContext>  securityContextProvider,
						   final Marshaller modelObjectsMarshaller,
						   final Map<Class,ServiceInterface> usersServiceInterfaceTypesToImplOrProxy,
						   final Class<? extends CRUDServicesForPrincipal<P>> crudType,
						   final Class<? extends FindServicesForPrincipal<P>> findType,
						   final Class<? extends SearchServicesForPrincipal<P,?,?>> searchType) {

		super(securityContextProvider,
			  modelObjectsMarshaller,
			  // Reference to other client apis
			  usersServiceInterfaceTypesToImplOrProxy);
	
		// Delegate for CRUD
		_forCrud = new ClientAPIDelegateForPrincipalCRUDServices<>(securityContextProvider,
								                     	  		   modelObjectsMarshaller,
								                     	  		   (CRUDServicesForPrincipal<P>)usersServiceInterfaceTypesToImplOrProxy.get(crudType));
		  // Delegate for Find.
		_forFind = new ClientAPIDelegateForPrincipalFindServices<>(securityContextProvider,
										             	  		   modelObjectsMarshaller,
										             	  		   (FindServicesForPrincipal<P>)usersServiceInterfaceTypesToImplOrProxy.get(findType));
		// Delegate for Search
		if (searchType != null) {
			_forSearch = new ClientAPIDelegateForPrincipalSearchServices<>(securityContextProvider,
											               				   modelObjectsMarshaller,
											               				   (SearchServicesForPrincipal<P,?,?>)usersServiceInterfaceTypesToImplOrProxy.get(searchType));
		} else {
			_forSearch = null;
		}
	}
}
