package r01f.db.search.security;

import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import r01f.model.security.user.SearchFilterForUserBase;
import r01f.model.security.user.SearchResultItemForUserBase;
import r01f.model.security.user.User;
import r01f.objectstreamer.Marshaller;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.search.db.DBSearcherProviderBase;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class DBSearcherProvidersForUser {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Accessors(prefix="_")
	public static abstract class DBSearcherProviderForUserBase<F extends SearchFilterForUserBase<? extends User,?>,
	                                                           I extends SearchResultItemForUserBase<? extends User>>
	            		 extends DBSearcherProviderBase<F,I> {
		public DBSearcherProviderForUserBase(final Marshaller marshaller,
											 final DBModuleConfig dbModuleConfig,
											 final Provider<EntityManager> entityManagerProvider) {
			super(marshaller,
				  dbModuleConfig,
				  entityManagerProvider);
		}
	}
}
