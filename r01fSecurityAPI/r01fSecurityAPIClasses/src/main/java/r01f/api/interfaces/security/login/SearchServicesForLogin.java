package r01f.api.interfaces.security.login;

import r01f.model.security.login.Login;
import r01f.model.security.login.SearchFilterForLoginBase;
import r01f.model.security.login.SearchResultItemForLoginBase;
import r01f.services.interfaces.SearchServicesForModelObject;

public interface SearchServicesForLogin<L extends Login,
									    F extends SearchFilterForLoginBase<L,?>,I extends SearchResultItemForLoginBase<L>>
		 extends SearchServicesForModelObject<F,I> {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
}
