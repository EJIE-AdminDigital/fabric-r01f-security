package r01f.model.security.login.saml;

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
 *		@MarshallType(as="samlLogin")
 *		@Accessors(prefix="_")
 *		public class MySAMLLogin
 *			 extends SAMLLoginBase<MySAMLLogin> 
 *		  implements MyIsPersistableSecurityObject {
 *		
 *			public MySAMLLogin() {
 *				// default no-args constructor
 *			}
 *			public MySAMLLogin(final LoginOID oid) {
 *				super(oid);
 *			}
 *			public static LoginBuilderStart<MySAMLLogin,SAMLLoginBuilderEMailStep<MySAMLLogin>> create() {
 *				MySAMLLogin login = new MySAMLLogin(LoginOID.supply());
 *				return new LoginBuilderStart<>(login,
 *											   SAMLLoginBuilderEMailStep::new);	// next step factory
 *			}
 *		}
 * </pre>
 * @param <SELF_TYPE>
 */
@Accessors(prefix="_")
public abstract class SAMLLoginBase<SELF_TYPE extends SAMLLoginBase<SELF_TYPE>>
			  extends LoginBase<SELF_TYPE>
		   implements SAMLLogin {

	private static final long serialVersionUID = 6847872821140655644L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	@MarshallField(as="mail", whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private EMail _mail;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public SAMLLoginBase() {
		// default no-args constructor
	}
	public SAMLLoginBase(final LoginOID oid) {
		super(oid);
	}
}
