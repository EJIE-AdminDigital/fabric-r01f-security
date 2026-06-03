package r01f.model.security.login.userpassword;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.login.LoginBase;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.Password;
import r01f.securitycontext.SecurityIDS.PasswordHash;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.securitycontext.SecurityOIDs.UserOID;

/**
 * Usage: just extend like
 * <pre class='brush:java'>
 *		@MarshallType(as = "userPasswordLogin")
 *		@Accessors(prefix = "_")
 *		public class MyUserPasswordLogin
 *			 extends UserPasswordLoginBase<MyUserPasswordLogin> 
 *		  implements MyIsPersistableSecurityObject {
 *		
 *			public MyUserPasswordLogin() {
 *				// default no-args constructor
 *			}
 *			public MyUserPasswordLogin(final LoginOID oid) {
 *				super(oid);
 *			}
 *			public MyUserPasswordLogin(final UserOID userOid,
 *										 final LoginID loginId) {
 *				super(userOid,
 *					  loginId);
 *			}
 *			public MyUserPasswordLogin(final UserOID userOid,
 *										 final LoginID loginId,final Password password) {
 *				super(userOid,
 *					  loginId,password);
 *			}
 *			public static LoginBuilderStart<MyUserPasswordLogin,UserPasswordLoginBuilderPasswordStep<MyUserPasswordLogin>> create() {
 *				MyUserPasswordLogin login = new MyUserPasswordLogin(LoginOID.supply());
 *				return new LoginBuilderStart<>(login,
 *											   UserPasswordLoginBuilderPasswordStep::new);	// next step factory
 *			}
 *		}
 * </pre>
 * @param <SELF_TYPE>
 */
@Accessors(prefix="_")
public abstract class UserPasswordLoginBase<SELF_TYPE extends UserPasswordLoginBase<SELF_TYPE>>
			  extends LoginBase<SELF_TYPE>
		   implements UserPasswordLogin {

	private static final long serialVersionUID = 6847872821140655644L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="password",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Password _password;
	
	@MarshallField(as="passwordHash",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private PasswordHash _passwordHash;

	@MarshallField(as="passwordExpirationDate",dateFormat=@MarshallDateFormat(use=DateFormat.CUSTOM,format="yyyy-MM-dd HH:mm:ss"),
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Instant _passwordExpirationDate;

	@MarshallField(as="isConfirmed", whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private boolean _confirmed = true;//Confirmed by default
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public UserPasswordLoginBase() {
		// default no-args constructor
	}
	public UserPasswordLoginBase(final LoginOID oid) {
		super(oid);
	}
	public UserPasswordLoginBase(final LoginOID oid,
								 final UserOID userOid,final LoginID loginId) {
		super(oid,
			  userOid,loginId);
	}
	public UserPasswordLoginBase(final UserOID userOid,final LoginID loginId) {
		super(userOid,loginId);
	}
	public UserPasswordLoginBase(final UserOID userOid,final LoginID loginId,final Password password) {
		super(userOid,loginId);
		_password = password;
	}
	public UserPasswordLoginBase(final LoginOID oid,
								 final UserOID userOid,final LoginID loginId,final Password password) {
		super(oid,
			  userOid,loginId);
		_password = password;
	}
}
