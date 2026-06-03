package r01f.model.security.login.giltza;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.login.LoginBase;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.types.contact.EMail;

/**
 * Usage: just extend
 * <pre class='brush:java'>
 *		@MarshallType(as="giltzaLogin")
 *		@Accessors(prefix="_")
 *		public class MyGiltzaLogin 
 *			 extends GiltzaLoginBase<MyGiltzaLogin> 
 *		  implements MyIsPersistableSecurityObject {
 *		
 *			public MyGiltzaLogin() {
 *				// default no-args constructor
 *			}
 *			public MyGiltzaLogin(final LoginOID oid) {
 *				super(oid);
 *			}
 *			public static LoginBuilderStart<MyGiltzaLogin,GiltzaLoginBuilderEMailStep<MyGiltzaLogin>> create() {
 *				MyGiltzaLogin login = new MyGiltzaLogin(LoginOID.supply());
 *				return new LoginBuilderStart<>(login,
 *											   GiltzaLoginBuilderEMailStep<MyGiltzaLogin>::new);	// next step factory
 *			}
 *		}
 * </pre>
 * @param <SELF_TYPE>
 */
@Accessors(prefix="_")
public abstract class GiltzaLoginBase<SELF_TYPE extends GiltzaLoginBase<SELF_TYPE>>
			  extends LoginBase<SELF_TYPE>
		   implements GiltzaLogin {

	private static final long serialVersionUID = 6847872821140655644L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	@MarshallField(as="email", whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private EMail _email;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public GiltzaLoginBase() {
		// default no-args constructor
	}
	public GiltzaLoginBase(final LoginOID oid) {
		super(oid);
	}
}
