package r01f.model.security.login;

import r01f.model.search.SearchFilterForModelObjectBase;


public abstract class SearchFilterForLoginBase<L extends Login,
										  	   SELF_TYPE extends SearchFilterForLoginBase<L,SELF_TYPE>>
			  extends SearchFilterForModelObjectBase<SELF_TYPE> {

	private static final long serialVersionUID = 845597803175070484L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public SearchFilterForLoginBase(final Class<L> objType) {
		super(objType);
	}
}
