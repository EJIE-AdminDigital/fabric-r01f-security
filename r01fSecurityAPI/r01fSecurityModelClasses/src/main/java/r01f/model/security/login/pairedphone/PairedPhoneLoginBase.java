package r01f.model.security.login.pairedphone;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.login.LoginBase;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.types.contact.Phone;

/**
 * Usage: just extend like:
 * <pre class='brush:java'>
 *		@MarshallType(as="pairedPhoneLogin")
 *		@Accessors(prefix="_")
 *		public class MyPairedPhoneLogin 
 *			 extends PairedPhoneLoginBase<MyPairedPhoneLogin> 
 *		  implements MyIsPersistableSecurityObject {
 *		
 *			public MyPairedPhoneLogin() {
 *				// default no-args constructor
 *			}
 *			public MyPairedPhoneLogin(final LoginOID oid) {
 *				super(oid);
 *			}
 *			public static LoginBuilderStart<MyPairedPhoneLogin,PairedPhoneLoginBuilderPhoneStep<MyPairedPhoneLogin>> create() {
 *				MyPairedPhoneLogin login = new MyPairedPhoneLogin(LoginOID.supply());
 *				return new LoginBuilderStart<>(login,
 *											   PairedPhoneLoginBuilderPhoneStep::new);	// next step factory
 *			}
 *		}
 * </pre>
 * @param <SELF_TYPE>
 */
@Accessors(prefix="_")
public abstract class PairedPhoneLoginBase<SELF_TYPE extends PairedPhoneLoginBase<SELF_TYPE>>
			  extends LoginBase<SELF_TYPE>
		   implements PairedPhoneLogin {

	private static final long serialVersionUID = 8350894051330115259L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	@MarshallField(as="pairedPhone", whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Phone  _pairedPhone;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public PairedPhoneLoginBase() {
		// default no-args constructor
	}
	public PairedPhoneLoginBase(final LoginOID oid) {
		super(oid);
	}
}


