package r01f.model.security.login.userpassword;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.SecurityObject;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.url.Url;

/**
 * When using [web flow] for [user/password login] a POST with the user/pwd must be issued 
 * to the [security REST service] that in turn will return an URL to redir to
 */
@MarshallType(as="userPasswordLoginResponseForWebFlow")
@Accessors(prefix="_")
public class UserPasswordLoginResponseForWebFlow 
  implements SecurityObject {

	private static final long serialVersionUID = -3134448127913644074L;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * The url to redir to
	 */
	@MarshallField(as="url",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Url _redirUrl;
	/**
	 * If the login was unsuccessful, this contains the error
	 * (not present if login successful)
	 */
	@MarshallField(as="error",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter UserPasswordLoginErrorType _error;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDER
/////////////////////////////////////////////////////////////////////////////////////////	
	public UserPasswordLoginResponseForWebFlow() {
		// default no-args constructor
	}
	public UserPasswordLoginResponseForWebFlow(final Url redirUrl,final UserPasswordLoginErrorType error) {
		_redirUrl = redirUrl;
		_error = error;
	}
	public static UserPasswordLoginResponseForWebFlow success(final Url redirUrl) {
		return new UserPasswordLoginResponseForWebFlow(redirUrl,null);	// no error
	}
	public static UserPasswordLoginResponseForWebFlow failed(final Url redirUrl,final UserPasswordLoginErrorType error) {
		return new UserPasswordLoginResponseForWebFlow(redirUrl,error);
	}
	public static UserPasswordLoginResponseForWebFlow pendingOnAuthFactor(final Url redirUrl) {
		return new UserPasswordLoginResponseForWebFlow(redirUrl,null);	// pending on auth factor
	}	
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public boolean wasSuccessful() {
		return _error == null;
	}
	public boolean hasFailel() {
		return !this.wasSuccessful();
	}

}
