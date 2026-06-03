package r01f.model.security.login.google;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.login.LoginBase;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.types.contact.EMail;

/**
 * Usage: just extend like:
 * <pre class='brush:java'>
 *		@MarshallType(as="googleLogin")
 *		@Accessors(prefix="_")
 *		public class MyGoogleLogin
 *			 extends GoogleLoginBase<MyGoogleLogin> 
 *		  implements MyIsPersistableSecurityObject {
 *		
 *			public MyGoogleLogin() {
 *				// default no-args constructor
 *			}
 *			public MyGoogleLogin(final LoginOID oid) {
 *				super(oid);
 *			}
 *			public static LoginBuilderStart<MyGoogleLogin,GoogleLoginBuilderEMailStep<MyGoogleLogin>> create() {
 *				MyGoogleLogin login = new MyGoogleLogin(LoginOID.supply());
 *				return new LoginBuilderStart<>(login,
 *											   GoogleLoginBuilderEMailStep::new);	// next step factory
 *			}
 *		}
 * </pre>
 * @param <SELF_TYPE>
 */
@Accessors(prefix="_")
public abstract class GoogleLoginBase<SELF_TYPE extends GoogleLoginBase<SELF_TYPE>>
			  extends LoginBase<SELF_TYPE>
		   implements GoogleLogin {

	private static final long serialVersionUID = 6847872821140655644L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	@MarshallField(as="googleMail", whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private EMail _googleMail;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public GoogleLoginBase() {
		// default no-args constructor
	}
	public GoogleLoginBase(final LoginOID oid) {
		super(oid);
	}
}
