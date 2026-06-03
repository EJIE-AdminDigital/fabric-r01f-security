package r01f.db.search.security;

import java.util.Collection;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import r01f.db.entities.security.DBEntityForUserBase;
import r01f.locale.Language;
import r01f.model.search.query.SearchResultsOrdering;
import r01f.model.security.user.SearchFilterForUserBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.config.DBVendor;
import r01f.services.core.persistence.search.db.DBSearchJPQUtil;
import r01f.services.core.persistence.search.db.DBSearchQuery;
import r01f.services.core.persistence.search.db.DBSearchQueryToJPQLTranslator;
import r01f.services.core.persistence.search.db.TranslatesIndexableFieldIDToDBEntityField;
import r01f.services.core.persistence.search.db.TranslatesSearchFilterClauseToJPQLWherePredicate;
import r01f.util.types.Strings;

@Slf4j
public abstract class DBSearchQueryForUser<F extends SearchFilterForUserBase<?,?>,
										   DB extends DBEntityForUserBase>
		      extends DBSearchQuery<F,
					  				DB> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBSearchQueryForUser(final Class<DB> dbEntityType,
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
	public DBSearchQueryForUser(final Class<DB> dbEntityType,
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
	static class DBSearchQueryForUserToJPQLTranslator<F extends SearchFilterForUserBase<?,?>,
										   			  DB extends DBEntityForUserBase>
		 extends DBSearchQueryToJPQLTranslator<F,DB> {
		public DBSearchQueryForUserToJPQLTranslator(final Class<DB> dbEntityType,
													final DBModuleConfig dbModuleConfig,
												    final EntityManager entityManager) {
			super(dbEntityType,
				  dbModuleConfig,
				  entityManager);
		}
		@Override
	    protected String _composeWhereJpqlPredicates(final F filter,
	    											 final TranslatesSearchFilterClauseToJPQLWherePredicate filterClauseToJpqlPredicate) {
			final StringBuilder jpqlWhere = new StringBuilder();
			jpqlWhere.append("(");

			// Get the db colname for the name & surname
			String nameDBColName = "entity._name";
			String surnameDBColName = "entity._surname";

			// [1] - Filter by text
			if (filter.hasTextFilter() 
			 && Strings.isNOTNullOrEmpty(filter.getText())) {
				boolean fullTextSearchSupported = _dbModuleConfig.isFullTextSearchSupported(_entityManager);
				
				log.debug("FullText search enabled: {}",fullTextSearchSupported);
				if (fullTextSearchSupported) {
					// Full text search is ENABLED: the filter expression is db platform-dependent
					// 								use SQL operator available since eclipselink 2.5
					// 								see http://wiki.eclipse.org/EclipseLink/UserGuide/JPA/Basic_JPA_Development/Querying/Support_for_Native_Database_Functions#SQL
					DBVendor dbVendor = _dbModuleConfig.getDbSpec().getVendor();
					if (dbVendor.is(DBVendor.MySQL)) {
					    // IMPORTANT!! see: http://dev.mysql.com/doc/refman/5.0/en/fulltext-search.html / http://devzone.zend.com/26/using-mysql-full-text-searching/
					    //		Tables MUST be MyISAM (InnoDB)) type; to change the table type:
					    //			ALTER TABLE [table] engine=MyISAM;
					    //
					    //		also a FULLTEXT index must be added to the cols:
					    //			ALTER TABLE [table] ADD FULLTEXT [NOMBRE INDICE](col1,col2,...);
					    //
					    //		Once the above is done, a FULL-TEXT search can be executed like:
					    //			select *
					    //			  from [table]
					    //			 where MATCH(col1,col2) AGAINST ('[text]');

						// BEWARE!!! by default, the FULLTEXT index is used in "natural language mode", which, will IGNORE results that happen in 50% or more of the rows
						//			 ("words that are present in 50% or more of the rows are considered common and do not match.").
						//			 If there's only 1 row in the table, there will ALWAYS return 0 results

						// Generate:  SQL('MATCH(?) AGAINST(?),_entity._nameXX,:text
						jpqlWhere.append(Strings.customized("SQL('MATCH(?,?) " +
															   "AGAINST(? IN BOOLEAN MODE)',{},{},:text)",	// use IN NATURAL LANGUAGE MODE / IN BOOLEAN MODE
															nameDBColName,surnameDBColName));
					}
					else if (dbVendor.is(DBVendor.ORACLE)) {

						// IMPORTANT!! see: http://docs.oracle.com/cd/B28359_01/text.111/b28304/csql.htm#i997503
						// 		Oracle Text MUST be enabled

						// Generate: contains(?,?,1) > 0,entity._nameXX,:text
						// Make sure label number is unique: https://stackoverflow.com/questions/15474350/using-oracle-contains-clause-for-2-columns
						String multicolumnIndexSearchName = "entity._searchedColumns";
						jpqlWhere.append(Strings.customized("(" +
								 								"SQL('CONTAINS(?,?,1) > 0',{},:text) " +
								 							")",
								 							multicolumnIndexSearchName));

					}
				} else {
					// Full Text search is DISABLED: use LIKE operator
					jpqlWhere.append(Strings.customized("(" +
							 								"upper({}) LIKE :text " +
							 								"OR " +
							 								"upper({}) LIKE :text " +
							 							")",
							 							nameDBColName,surnameDBColName));
				}
			}
			jpqlWhere.append(") ");
			return jpqlWhere.toString();
	    }
		@Override
		protected String _composeJpqlOrderByClause(final F filter,
											   	   final Collection<SearchResultsOrdering> ordering,
											   	   final TranslatesIndexableFieldIDToDBEntityField translatesFieldToDBEntityField) {
			return "ORDER BY entity._surname ASC," +
							"entity._oid ASC";
		}
		@Override
	    public void setJPAQueryParameters(final F filter,
	    								  final Query qry) {
			DBVendor dbVendor = _dbModuleConfig.getDbSpec().getVendor();

			if (filter.hasTextFilter() 
			 && Strings.isNOTNullOrEmpty(filter.getText())) {
				boolean fullTextSearchSupported = _dbModuleConfig.isFullTextSearchSupported(_entityManager);
				String text = filter.getText()
									.trim().toUpperCase();	// important!!
				String filteringText = null;
				if (fullTextSearchSupported) {
					// when full text search is enabled, multiple words are supported
					String accumulateTermsText = dbVendor.is(DBVendor.ORACLE) ? text.replaceAll(" +", " ACCUM ")
																			  : text;
					filteringText = DBSearchJPQUtil.sanitizeFullTextQueryText(accumulateTermsText);
				}
				else {
					// when full text search is NOT enabled, LIKE operations ONLY supports a single word
					// (otherwise multiple LIKE clauses are needed:  X LIKE %..% OR X LIKE %..%)
					filteringText = "%" +
									DBSearchJPQUtil.sanitizeFullTextQueryText(text.split(" ")[0]) +		// use only the FIRST word (no multiple word is allowed)
								    "%";
				}
				qry.setParameter("text",filteringText);
			}
		}
	}
}
