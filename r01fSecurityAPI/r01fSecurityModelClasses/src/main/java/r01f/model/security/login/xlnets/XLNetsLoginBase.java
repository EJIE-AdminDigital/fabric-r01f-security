package r01f.model.security.login.xlnets;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.guids.CommonOIDs.WorkPlaceCode;
import r01f.model.security.login.LoginBase;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.securitycontext.SecurityOIDs.LoginOID;

/**
 * Usage: just extend like
 * <pre class='brush:java'>
 *		@MarshallType(as="xlnetsLogin")
 *		@Accessors(prefix="_")
 *		public class MyXLNetsLogin
 *			 extends XLNetsLoginBase<MyXLNetsLogin>
 *		  implements MyIsPersistableSecurityObject {
 *
 *			public MyXLNetsLogin() {
 *				// default no-args constructor
 *			}
 *			public MyXLNetsLogin(final LoginOID oid) {
 *				super(oid);
 *			}
 *			public static LoginBuilderStart<MyXLNetsLogin,XLNetsLoginBuilderWorkPlaceStep<MyXLNetsLogin>> create() {
 *				MyXLNetsLogin login = new MyXLNetsLogin(LoginOID.supply());
 *				return new LoginBuilderStart<>(login,
 *											   XLNetsLoginBuilderWorkPlaceStep<MyXLNetsLogin>::new);	// next step factory
 *			}
 *		}
 * </pre>
 * @param <SELF_TYPE>
 */
@Accessors(prefix="_")
@MarshallType(as="xlnetsLoginBase")
public abstract class XLNetsLoginBase<SELF_TYPE extends XLNetsLoginBase<SELF_TYPE>>
			  extends LoginBase<SELF_TYPE>
		   implements XLNetsLogin {

	private static final long serialVersionUID = 6847872821140655644L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * WorkPlace
	 */
	@MarshallField(as="workPlaceCode",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private WorkPlaceCode _workPlaceCode;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public XLNetsLoginBase() {
		// default no-args constructor
	}
	public XLNetsLoginBase(final LoginOID oid) {
		super(oid);
	}
}

