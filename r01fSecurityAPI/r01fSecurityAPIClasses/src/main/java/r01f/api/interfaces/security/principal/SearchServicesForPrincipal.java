package r01f.api.interfaces.security.principal;

import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.principal.SearchFilterForSecurityPrincipalBase;
import r01f.model.security.principal.SearchResultItemForSecurityPrincipalBase;
import r01f.services.interfaces.SearchServicesForModelObject;

public interface SearchServicesForPrincipal<P extends IsSecurityPrincipal,
									   		F extends SearchFilterForSecurityPrincipalBase<P,?>,I extends SearchResultItemForSecurityPrincipalBase<P>>
		 extends SearchServicesForModelObject<F,I> {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
}
