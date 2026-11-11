package r01f.model.security.login;

import r01f.model.search.SearchFilterForModelObjectBase;


public class SearchFilterForLogin
	 extends SearchFilterForModelObjectBase<SearchFilterForLogin> {

	private static final long serialVersionUID = 845597803175070484L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public SearchFilterForLogin() {
		super(Login.class);
	}
}
