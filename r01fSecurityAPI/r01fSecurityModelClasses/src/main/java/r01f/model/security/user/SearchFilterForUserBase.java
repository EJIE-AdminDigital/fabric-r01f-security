package r01f.model.security.user;

import r01f.model.search.SearchFilterForModelObjectBase;


public abstract class SearchFilterForUserBase<U extends User,
										  	  SELF_TYPE extends SearchFilterForUserBase<U,SELF_TYPE>> 
			  extends SearchFilterForModelObjectBase<SELF_TYPE> {

	private static final long serialVersionUID = -17240920165787897L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public SearchFilterForUserBase(final Class<U> objType) {
		super(objType);
	}
}
