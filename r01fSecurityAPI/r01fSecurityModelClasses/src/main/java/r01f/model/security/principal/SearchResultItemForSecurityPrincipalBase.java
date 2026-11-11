package r01f.model.security.principal;

import lombok.experimental.Accessors;
import r01f.model.search.SearchResultItemForModelObjectBase;
import r01f.model.security.IsSecurityPrincipal;
import r01f.objectstreamer.annotations.MarshallField;

@Accessors(prefix="_")
public class SearchResultItemForSecurityPrincipalBase<P extends IsSecurityPrincipal>
	 extends SearchResultItemForModelObjectBase<P> {

	private static final long serialVersionUID = 5613644249628044322L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="securityPrincipal")
	protected P _securityPrincipal; 
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public SearchResultItemForSecurityPrincipalBase(final Class<P> principalType) {
		super(principalType);
	}
	public SearchResultItemForSecurityPrincipalBase(final P principal) {
		super(principal);
		_securityPrincipal = principal;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	GET & SET
/////////////////////////////////////////////////////////////////////////////////////////
	public P getSecurityPrincipal() {
		return _securityPrincipal;
	}
	public void setSecurityPrincipal(final P principal) {
		_securityPrincipal = principal;
		_setSearchResultItemDataFrom(principal);
	}
}
