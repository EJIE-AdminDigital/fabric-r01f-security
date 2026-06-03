package r01f.db.transfer.security;



import java.util.function.Function;

import lombok.RequiredArgsConstructor;
import r01f.db.entities.security.DBEntityForXLNetsLogin;
import r01f.locale.Language;
import r01f.model.security.login.xlnets.SearchResultItemForXLNetsLogin;
import r01f.model.security.login.xlnets.XLNetsLogin;
import r01f.patterns.FactoryFrom;
import r01f.securitycontext.SecurityContext;
import r01f.services.core.persistence.db.TransformsDBEntityIntoModelObject;
import r01f.services.core.persistence.search.db.TransformsDBEntityToSearchResultItem;

@RequiredArgsConstructor
public class XLNetsDBEntityToSearchResultTranformer<L extends XLNetsLogin,
												    DB extends DBEntityForXLNetsLogin>
  implements TransformsDBEntityToSearchResultItem<DB,SearchResultItemForXLNetsLogin> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final TransformsDBEntityIntoModelObject<DB,L> _dbUserToModelObj;
	private final FactoryFrom<L,SearchResultItemForXLNetsLogin> _searchResultItemFromUserFactory;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns this transformer as a {@link Function}
	 * @param securityContext
	 * @return
	 */
	public Function<DB,SearchResultItemForXLNetsLogin> asTransformFuncion(final SecurityContext securityContext,
											                              final Language lang) {
		return dbEntity -> XLNetsDBEntityToSearchResultTranformer.this.dbEntityToSearchResultItem(securityContext,
													  											dbEntity,
													  											lang);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public SearchResultItemForXLNetsLogin dbEntityToSearchResultItem(final SecurityContext securityContext,
					   				                               	final DB dbEntity,
										                            final Language lang) {
		// [1] - Transform the [db entity] into a [model object] (xlnets login)
		L xlnetsLogin = _dbUserToModelObj.dbEntityToModelObject(securityContext,
														        dbEntity);
		// [2] - Transform the [model object] into a [search result item]
		SearchResultItemForXLNetsLogin searchResultItem = _searchResultItemFromUserFactory.from(xlnetsLogin);

		// [99] - Return
		return searchResultItem;
	}
}
