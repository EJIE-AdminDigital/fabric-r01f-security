package r01f.model.security.login.xlnets;

import lombok.experimental.Accessors;
import r01f.model.search.SearchResultItemForModelObjectBase;
import r01f.objectstreamer.annotations.MarshallField;

@Accessors(prefix="_")
public class SearchResultItemForXLNetsLogin
	 extends SearchResultItemForModelObjectBase<XLNetsLogin> {

	private static final long serialVersionUID = 5613644249628044322L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="xlnetsLoginWorkPlaceCodeSummary")
	protected XLNetsLoginWorkPlaceCodeSummary  _summary;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public SearchResultItemForXLNetsLogin() {
		super(XLNetsLogin.class);
	}
	public SearchResultItemForXLNetsLogin(final XLNetsLogin login) {
		super(login);
		_setSearchResultItemDataFrom(login);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	GET & SET
/////////////////////////////////////////////////////////////////////////////////////////
	public XLNetsLoginWorkPlaceCodeSummary getXlnetLoginSummary() {
		return _summary;
	}
	public void setXlnetLoginSummary(final XLNetsLogin login) {
		_setSearchResultItemDataFrom(login);
	}

	protected  void _setSearchResultItemDataFrom(final XLNetsLogin login) {
		super._setSearchResultItemDataFrom(login);
		_summary = XLNetsLoginWorkPlaceCodeSummary.createFrom(login);
	}
}
