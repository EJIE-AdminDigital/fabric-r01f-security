package r01f.model.security.principal;

import r01f.model.search.SearchFilterForModelObjectBase;
import r01f.model.security.IsSecurityPrincipal;


public abstract class SearchFilterForSecurityPrincipalBase<P extends IsSecurityPrincipal,
										  	  			   SELF_TYPE extends SearchFilterForSecurityPrincipalBase<P,SELF_TYPE>> 
			  extends SearchFilterForModelObjectBase<SELF_TYPE> {

	private static final long serialVersionUID = -17240920165787897L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public SearchFilterForSecurityPrincipalBase(final Class<P> objType) {
		super(objType);
	}
}
