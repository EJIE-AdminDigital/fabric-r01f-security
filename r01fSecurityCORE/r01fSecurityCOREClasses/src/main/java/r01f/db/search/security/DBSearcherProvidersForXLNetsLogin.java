package r01f.db.search.security;

import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import r01f.model.security.login.Login;
import r01f.model.security.login.xlnets.SearchFilterForXLNetsLoginBase;
import r01f.model.security.login.xlnets.SearchResultItemForXLNetsLogin;
import r01f.objectstreamer.Marshaller;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.search.db.DBSearcherProviderBase;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class DBSearcherProvidersForXLNetsLogin {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Accessors(prefix="_")
	public static abstract class DBSearcherProviderForXLNetsLoginBase<F extends SearchFilterForXLNetsLoginBase<? extends Login,?>>
	            		 extends DBSearcherProviderBase<F,SearchResultItemForXLNetsLogin> {
		public DBSearcherProviderForXLNetsLoginBase(final Marshaller marshaller,
											        final DBModuleConfig dbModuleConfig,
											        final Provider<EntityManager> entityManagerProvider) {
			super(marshaller,
				  dbModuleConfig,
				  entityManagerProvider);
		}
	}
}
