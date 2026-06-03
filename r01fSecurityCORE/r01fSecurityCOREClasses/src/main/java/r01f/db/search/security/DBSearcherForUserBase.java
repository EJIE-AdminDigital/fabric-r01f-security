package r01f.db.search.security;

import jakarta.persistence.EntityManager;

import r01f.api.interfaces.security.user.SearchServicesForUser;
import r01f.db.entities.security.DBEntityForUserBase;
import r01f.model.security.user.SearchFilterForUserBase;
import r01f.model.security.user.SearchResultItemForUserBase;
import r01f.model.security.user.User;
import r01f.patterns.Factory;
import r01f.services.core.persistence.search.db.DBSearchQuery;
import r01f.services.core.persistence.search.db.DBSearcherBase;
import r01f.services.core.persistence.search.db.TransformsDBEntityToSearchResultItem;

public abstract class DBSearcherForUserBase<U extends User,
									  	    F extends SearchFilterForUserBase<U,?>,I extends SearchResultItemForUserBase<U>,
									  	    DB extends DBEntityForUserBase>
			  extends DBSearcherBase<F,I,
			  						 DB>
		   implements SearchServicesForUser<U,F,I> {

/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	protected <S extends DBSearchQuery<F,DB>> DBSearcherForUserBase(final EntityManager entityManager,
							  								  	    final Factory<S> searchQueryFactory,
							  								  	    final TransformsDBEntityToSearchResultItem<DB,I> transformsDBEntityToSearchResultItem) {
		super(entityManager,
			  searchQueryFactory,
			  transformsDBEntityToSearchResultItem);
	}
}
