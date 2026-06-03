package r01f.model.security.login.userpassword.create;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.SecurityObject;
import r01f.model.security.user.User;
import r01f.model.security.user.UserStatus;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.securitycontext.SecurityIDS.LoginAndPassword;
import r01f.types.contact.HasMailAddress;
import r01f.types.contact.HasPhoneNumber;
import r01f.types.contact.NotificationMedium;

@MarshallType(as="userPasswordLoginRequest")
@Accessors(prefix="_")
public class UserPasswordLoginCreateRequest<U extends User & HasMailAddress & HasPhoneNumber> 
  implements SecurityObject {

	private static final long serialVersionUID = -8624275004943297076L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="requestAt",dateFormat=@MarshallDateFormat(use=DateFormat.CUSTOM,format="yyyy-MM-dd HH:mm:ss"),
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Instant _requestAt;
	
	@MarshallField(as="user")
	@Getter @Setter private U _user;
	
	@MarshallField(as="loginId")
	@Getter @Setter private LoginAndPassword _login;
	
	@MarshallField(as="initialUserStatusWhenUserAlreadyExists", 
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private UserStatus _initialUserStatusWhenUserAlreadyExists;
	
	@MarshallField(as="initialUserStatusWhenUserIsNew", 
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private UserStatus _initialUserStatusWhenUserIsNew;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public UserPasswordLoginCreateRequest() {
		// default no-args constructor
	}
	public UserPasswordLoginCreateRequest(final U user,final LoginAndPassword loginData) {
		this(user,loginData,
			 UserStatus.ACTIVE,UserStatus.ACTIVE);
	}
	public UserPasswordLoginCreateRequest(final U user,final LoginAndPassword loginData,
								   		  final UserStatus initialUserStatusWhenUserAlreadyExists,final UserStatus initialUserStatusWhenUserIsNew) {
		this(user,loginData,
			 initialUserStatusWhenUserAlreadyExists,initialUserStatusWhenUserIsNew,
			 Instant.now());
	}
	public UserPasswordLoginCreateRequest(final U user,final LoginAndPassword loginData,
								   		  final UserStatus initialUserStatusWhenUserAlreadyExists,final UserStatus initialUserStatusWhenUserIsNew,
								   		  final Instant requestAt) {
		_user = user;
		_login = loginData;
		_initialUserStatusWhenUserAlreadyExists = initialUserStatusWhenUserAlreadyExists;
		_initialUserStatusWhenUserIsNew = initialUserStatusWhenUserIsNew;
		_requestAt = requestAt;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public NotificationMedium getUserNotificationMedium() {
		NotificationMedium outMedium = null;
		if (_user != null) {
			if (_user.getMailAddress() != null) {
				outMedium = NotificationMedium.EMAIL;
			} else if (_user.getPhoneNumber() != null) {
				outMedium = NotificationMedium.SMS;
			} 
		} 
		if (outMedium == null) throw new IllegalStateException("NO user notification medium available!");
		return outMedium;
	}
}
