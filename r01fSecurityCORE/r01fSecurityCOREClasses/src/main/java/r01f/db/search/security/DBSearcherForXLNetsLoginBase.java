package r01f.db.search.security;

import jakarta.persistence.EntityManager;
import r01f.api.interfaces.security.login.xlnets.SearchServicesForXLNetsLogin;
import r01f.db.entities.security.DBEntityForXLNetsLogin;
import r01f.model.security.login.SearchFilterForLoginBase;
import r01f.model.security.login.xlnets.SearchResultItemForXLNetsLogin;
import r01f.model.security.login.xlnets.XLNetsLogin;
import r01f.patterns.Factory;
import r01f.services.core.persistence.search.db.DBSearchQuery;
import r01f.services.core.persistence.search.db.DBSearcherBase;
import r01f.services.core.persistence.search.db.TransformsDBEntityToSearchResultItem;

public abstract class DBSearcherForXLNetsLoginBase<L extends XLNetsLogin,
                                                   F extends SearchFilterForLoginBase<L,?>,
									  	           DB extends DBEntityForXLNetsLogin>
			  extends DBSearcherBase<F,SearchResultItemForXLNetsLogin,
			  						 DB>
		   implements SearchServicesForXLNetsLogin<L,F> {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	protected <S extends DBSearchQuery<F,DB>> DBSearcherForXLNetsLoginBase(final EntityManager entityManager,
							  								  	                               final Factory<S> searchQueryFactory,
							  								  	                               final TransformsDBEntityToSearchResultItem<DB,SearchResultItemForXLNetsLogin> transformsDBEntityToSearchResultItem) {
		super(entityManager,
			  searchQueryFactory,
			  transformsDBEntityToSearchResultItem);
	}
}
