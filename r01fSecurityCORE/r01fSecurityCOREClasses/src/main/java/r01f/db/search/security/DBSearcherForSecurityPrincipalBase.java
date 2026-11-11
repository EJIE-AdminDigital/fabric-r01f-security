package r01f.db.search.security;

import jakarta.persistence.EntityManager;
import r01f.api.interfaces.security.principal.SearchServicesForPrincipal;
import r01f.db.entities.security.DBEntityForSecurityPrincipalBase;
import r01f.db.transfer.security.SecurityPrincipalDBEntityToSearchResultTranformer;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.principal.SearchFilterForSecurityPrincipalBase;
import r01f.model.security.principal.SearchResultItemForSecurityPrincipalBase;
import r01f.patterns.Factory;
import r01f.patterns.FactoryFrom;
import r01f.services.core.persistence.db.TransformsDBEntityIntoModelObject;
import r01f.services.core.persistence.search.db.DBSearchQuery;
import r01f.services.core.persistence.search.db.DBSearcherBase;

public abstract class DBSearcherForSecurityPrincipalBase<P extends IsSecurityPrincipal,
									  	    	 		 F extends SearchFilterForSecurityPrincipalBase<P,?>,I extends SearchResultItemForSecurityPrincipalBase<P>,
									  	    	 		 DB extends DBEntityForSecurityPrincipalBase>
			  extends DBSearcherBase<F,I,
			  						 DB>
		   implements SearchServicesForPrincipal<P,F,I> {

/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	protected <S extends DBSearchQuery<F,DB>> DBSearcherForSecurityPrincipalBase(final EntityManager entityManager,
									  								  	    	 final Factory<S> searchQueryFactory,
									  								  	    	 final TransformsDBEntityIntoModelObject<DB,P> dbPrincipalToModelObj,
									  								  	    	 final FactoryFrom<P,I> searchResultItemFromPrincipalFactory) {
		super(entityManager,
			  searchQueryFactory,
			  new SecurityPrincipalDBEntityToSearchResultTranformer<>(dbPrincipalToModelObj,
					  					 							  searchResultItemFromPrincipalFactory));
	}
}
