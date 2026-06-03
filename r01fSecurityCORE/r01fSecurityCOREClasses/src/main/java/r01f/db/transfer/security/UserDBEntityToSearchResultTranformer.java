package r01f.db.transfer.security;



import java.util.function.Function;

import lombok.RequiredArgsConstructor;
import r01f.db.entities.security.DBEntityForUserBase;
import r01f.locale.Language;
import r01f.model.security.user.SearchResultItemForUserBase;
import r01f.model.security.user.User;
import r01f.patterns.FactoryFrom;
import r01f.securitycontext.SecurityContext;
import r01f.services.core.persistence.db.TransformsDBEntityIntoModelObject;
import r01f.services.core.persistence.search.db.TransformsDBEntityToSearchResultItem;

@RequiredArgsConstructor
public class UserDBEntityToSearchResultTranformer<U extends User,I extends SearchResultItemForUserBase<U>,
												  DB extends DBEntityForUserBase> 
  implements TransformsDBEntityToSearchResultItem<DB,I> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final TransformsDBEntityIntoModelObject<DB,U> _dbUserToModelObj;
	private final FactoryFrom<U,I> _searchResultItemFromUserFactory;
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
		return dbEntity -> UserDBEntityToSearchResultTranformer.this.dbEntityToSearchResultItem(securityContext,
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
		// [1] - Transform the [db entity] into a [model object] (user)
		U user = _dbUserToModelObj.dbEntityToModelObject(securityContext,
														 dbEntity);
		// [2] - Transform the [model object] into a [search result item]
		I searchResultItem = _searchResultItemFromUserFactory.from(user);
		
		// [99] - Return
		return searchResultItem;
	}
}
