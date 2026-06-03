package r01f.model.security.login.xlnets;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.debug.Debuggable;
import r01f.guids.CommonOIDs.WorkPlaceCode;
import r01f.model.metadata.annotations.ModelObjectData;
import r01f.model.security.SecurityObject;
import r01f.model.security.metadata.MetaDataForSecurityObjectBase;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.objectstreamer.annotations.MarshallType;



@ModelObjectData(MetaDataForSecurityObjectBase.class)
@Accessors(prefix="_")
@NoArgsConstructor
@MarshallType(as="xLNetsLoginWorkPlaceCodeSummary")
public class XLNetsLoginWorkPlaceCodeSummary
  implements SecurityObject,
              Debuggable {
	private static final long serialVersionUID = 5828730087359671056L;
/////////////////////////////////////////////////////////////////////////////////////////
// FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * The Workplace Code
	 */
	@MarshallField(as="workPlaceCode",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private WorkPlaceCode _workPlaceCode;

	/**
	 * related user oid.
	 */
	@MarshallField(as="userOID",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private UserOID _userOID;

	/**
	 * The login oid
	 */
	@MarshallField(as="loginOid",
			   	   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter protected LoginOID _loginOID;

	/**
	 * The login id
	 */
	@MarshallField(as="loginId",
			   	   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter protected LoginID _loginId;
/////////////////////////////////////////////////////////////////////////////////////////
// METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	public static XLNetsLoginWorkPlaceCodeSummary createFor(final WorkPlaceCode code) {
		XLNetsLoginWorkPlaceCodeSummary summary = new XLNetsLoginWorkPlaceCodeSummary();
		summary.setWorkPlaceCode(code);
		return summary;
	}

	public static XLNetsLoginWorkPlaceCodeSummary createFrom(final XLNetsLogin login) {
		XLNetsLoginWorkPlaceCodeSummary summary = new XLNetsLoginWorkPlaceCodeSummary();
		summary.setWorkPlaceCode(login.getWorkPlaceCode());
		summary.setLoginId(login.getLoginId());
		summary.setLoginOID(login.getOid());
		summary.setUserOID(login.getUserOid());
		return summary;
	}

	public  XLNetsLoginWorkPlaceCodeSummary withLoginID(final LoginID loginId) {
		this.setLoginId(_loginId);
		return this;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  DEBUG
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CharSequence debugInfo() {
		StringBuffer sb = new StringBuffer("XLNetsLoginWorkPlaceCodeSummary  >> ");
	    sb.append(" workPlaceCode :" + _workPlaceCode);
	    sb.append(" loginID :" +       _loginId);
	    sb.append(" userOID :" +       _userOID);
	    sb.append(" loginOID :" +      _loginOID);
		return sb.toString();
	}


}
