package r01f.model.security.login;

import lombok.experimental.Accessors;
import r01f.model.search.SearchResultItemForModelObjectBase;
import r01f.objectstreamer.annotations.MarshallField;

@Accessors(prefix="_")
public class SearchResultItemForLoginBase<L extends Login>
	 extends SearchResultItemForModelObjectBase<L> {
	private static final long serialVersionUID = -3822071080335428100L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="login")
	protected L _login;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public SearchResultItemForLoginBase(final Class<L> loginType) {
		super(loginType);
	}
	public SearchResultItemForLoginBase(final L login) {
		super(login);
		_login = login;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	GET & SET
/////////////////////////////////////////////////////////////////////////////////////////
	public L getLogin() {
		return _login;
	}
	public void setUser(final L login) {
		_login = login;
		_setSearchResultItemDataFrom(login);
	}
}
