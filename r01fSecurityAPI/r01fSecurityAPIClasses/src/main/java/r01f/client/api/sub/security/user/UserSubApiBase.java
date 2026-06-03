package r01f.client.api.sub.security.user;

import java.util.Map;

import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import lombok.experimental.Accessors;
import r01f.model.security.user.SearchFilterForUserBase;
import r01f.model.security.user.SearchResultItemForUserBase;
import r01f.model.security.user.User;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.interfaces.ServiceInterface;

/**
 * Client implementation of the Users
 */
@Singleton
@Accessors(prefix="_")
public class UserSubApiBase<U extends User,
						    C extends ClientAPIDelegateForUserCRUDServices<U>,
						    F extends ClientAPIDelegateForUserFindServices<U>,
						    S extends ClientAPIDelegateForUserSearchServices<U,? extends SearchFilterForUserBase<U,?>,? extends SearchResultItemForUserBase<U>>>
	 extends UserSubApiAbstractBase<U,C,F> {		// a sub API uses the proxy to access the services layer
/////////////////////////////////////////////////////////////////////////////////////////
//	DELEGATE APIS
/////////////////////////////////////////////////////////////////////////////////////////
	private final S _forSearch;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings({ "rawtypes" })
	public UserSubApiBase(final Provider<SecurityContext>  securityContextProvider,
					      final Marshaller modelObjectsMarshaller,
					      final Map<Class,ServiceInterface> usersServiceInterfaceTypesToImplOrProxy,
					      final C crudDelegate,
					      final F findDelegate,
					      final S searchDelegate) {

		super(securityContextProvider,
			  modelObjectsMarshaller,
			  usersServiceInterfaceTypesToImplOrProxy,// reference to other client apis
			  crudDelegate,findDelegate
			  );
		_forSearch = searchDelegate;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings({ "unchecked","hiding" })
	public <F extends SearchFilterForUserBase<U,F>,I extends SearchResultItemForUserBase<U>> ClientAPIDelegateForUserSearchServices<U,F,I> getForSearch() {
		return (ClientAPIDelegateForUserSearchServices<U,F,I>)_forSearch;
	}
	@SuppressWarnings({ "unchecked","hiding" })
	public <F extends SearchFilterForUserBase<U,F>,I extends SearchResultItemForUserBase<U>> ClientAPIDelegateForUserSearchServices<U,F,I> getForSearch(final Class<F> filterType,final Class<I> itemType) {
		return (ClientAPIDelegateForUserSearchServices<U,F,I>)_forSearch;
	}
}
