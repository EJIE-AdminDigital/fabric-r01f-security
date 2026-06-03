package r01f.db.search.security;

import jakarta.persistence.EntityManager;
import r01f.db.entities.security.DBEntityForXLNetsLogin;
import r01f.locale.Language;
import r01f.model.security.login.SearchFilterForLoginBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.search.db.DBSearchQuery;
import r01f.services.core.persistence.search.db.DBSearchQueryToJPQLTranslator;


public class DBSearchQueryForXLNetsLogin<F extends  SearchFilterForLoginBase<?,?>,
                                         DB extends DBEntityForXLNetsLogin>
		      extends DBSearchQuery<F,
					  				DB> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBSearchQueryForXLNetsLogin(final Class<DB> dbEntityType,
								       final DBModuleConfig dbConfig,
							           final EntityManager entityManager,
   							           final Language uiLanguage) {
		this(dbEntityType,
			 dbConfig,
			 entityManager,
			 uiLanguage,
			 // translator by default
			 new DBSearchQueryForUserToJPQLTranslator<>(dbEntityType,
					 								    dbConfig,
					 								    entityManager));
	}
	public DBSearchQueryForXLNetsLogin(final Class<DB> dbEntityType,
									   final DBModuleConfig dbConfig,
									   final EntityManager entityManager,
									   final Language uiLanguage,
									   final DBSearchQueryForUserToJPQLTranslator<F,DB> searchQueryToJPQLTranslator) {
		super(dbEntityType,
			  dbConfig,
			  entityManager,
			  uiLanguage,
			  searchQueryToJPQLTranslator);
	}
////////////////////////////////////////////////////////////////////////////////////////
//  JPQL COMPOSING
////////////////////////////////////////////////////////////////////////////////////////
	static class DBSearchQueryForUserToJPQLTranslator<F extends SearchFilterForLoginBase<?,?>,
	                                                  DB extends DBEntityForXLNetsLogin>
		 extends DBSearchQueryToJPQLTranslator<F,DB> {
		public DBSearchQueryForUserToJPQLTranslator(final Class<DB> dbEntityType,
													final DBModuleConfig dbModuleConfig,
												    final EntityManager entityManager) {
			super(dbEntityType,
				  dbModuleConfig,
				  entityManager);
		}
	}
}
