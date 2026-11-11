package r01f.db.transfer.security;



import java.util.function.Function;

import lombok.RequiredArgsConstructor;
import r01f.db.entities.security.DBEntityForSecurityPrincipalBase;
import r01f.locale.Language;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.principal.SearchResultItemForSecurityPrincipalBase;
import r01f.patterns.FactoryFrom;
import r01f.services.core.persistence.db.TransformsDBEntityIntoModelObject;
import r01f.services.core.persistence.search.db.TransformsDBEntityToSearchResultItem;

@RequiredArgsConstructor
public class SecurityPrincipalDBEntityToSearchResultTranformer<P extends IsSecurityPrincipal,I extends SearchResultItemForSecurityPrincipalBase<P>,
												  			   DB extends DBEntityForSecurityPrincipalBase> 
  implements TransformsDBEntityToSearchResultItem<DB,I> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final TransformsDBEntityIntoModelObject<DB,P> _dbPrincipalToModelObj;
	private final FactoryFrom<P,I> _searchResultItemFromPrincipalFactory;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns this transformer as a {@link Function}
	 * @param securityContext
	 * @return
	 */
	public Function<DB,I> asTransformFuncion(final SecurityContext securityContext,
											 final Language lang) {
		return dbEntity -> SecurityPrincipalDBEntityToSearchResultTranformer.this.dbEntityToSearchResultItem(securityContext,
													  											dbEntity,
													  											lang);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public I dbEntityToSearchResultItem(final SecurityContext securityContext,
					   				  	final DB dbEntity,
										final Language lang) {
		// [1] - Transform the [db entity] into a [model object] (security principal)
		P securityPrincipal = _dbPrincipalToModelObj.dbEntityToModelObject(securityContext,
														 				   dbEntity);
		// [2] - Transform the [model object] into a [search result item]
		I searchResultItem = _searchResultItemFromPrincipalFactory.from(securityPrincipal);
		
		// [99] - Return
		return searchResultItem;
	}
}
