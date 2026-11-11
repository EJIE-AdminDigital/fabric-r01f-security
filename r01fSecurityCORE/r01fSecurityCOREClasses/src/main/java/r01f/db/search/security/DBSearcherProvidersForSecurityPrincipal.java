package r01f.db.search.security;

import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.principal.SearchFilterForSecurityPrincipalBase;
import r01f.model.security.principal.SearchResultItemForSecurityPrincipalBase;
import r01f.objectstreamer.Marshaller;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.search.db.DBSearcherProviderBase;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class DBSearcherProvidersForSecurityPrincipal {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Accessors(prefix="_")
	public static abstract class DBSearcherProviderForSecurityPrincipalBase<F extends SearchFilterForSecurityPrincipalBase<? extends IsSecurityPrincipal,?>,
	                                                           			    I extends SearchResultItemForSecurityPrincipalBase<? extends IsSecurityPrincipal>>
	            		 extends DBSearcherProviderBase<F,I> {
		public DBSearcherProviderForSecurityPrincipalBase(final Marshaller marshaller,
														  final DBModuleConfig dbModuleConfig,
														  final Provider<EntityManager> entityManagerProvider) {
			super(marshaller,
				  dbModuleConfig,
				  entityManagerProvider);
		}
	}
}
