package r01f.api.interfaces.security.user;

import r01f.model.security.user.SearchFilterForUserBase;
import r01f.model.security.user.SearchResultItemForUserBase;
import r01f.model.security.user.User;
import r01f.services.interfaces.SearchServicesForModelObject;

public interface SearchServicesForUser<U extends User,
									   F extends SearchFilterForUserBase<U,?>,I extends SearchResultItemForUserBase<U>>
		 extends SearchServicesForModelObject<F,I> {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
}
