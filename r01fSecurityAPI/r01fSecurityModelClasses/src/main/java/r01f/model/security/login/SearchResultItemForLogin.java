package r01f.model.security.login;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.search.SearchResultItemForModelObjectBase;
import r01f.objectstreamer.annotations.MarshallField;

@Accessors(prefix="_")
public class SearchResultItemForLogin
	 extends SearchResultItemForModelObjectBase<Login> {
	private static final long serialVersionUID = -3822071080335428100L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="login")
	@Getter @Setter protected Login _login;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public SearchResultItemForLogin() {
		super(Login.class);
	}
	public SearchResultItemForLogin(final Login login) {
		this();
		_login = login;
	}
}
