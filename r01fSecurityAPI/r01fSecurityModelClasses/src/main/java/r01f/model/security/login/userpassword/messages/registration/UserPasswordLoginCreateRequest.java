package r01f.model.security.login.userpassword.messages.registration;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.LoginStatus;
import r01f.model.security.SecurityPrincipalStatus;
import r01f.model.security.SecurityRequestBase;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.SecurityIDs.LoginAndPassword;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityIDs.Password;
import r01f.oids.SecurityIDs.SecurityPrincipalID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;

@MarshallType(as="userPasswordChangeRequest")
@Accessors(prefix="_")
public class UserPasswordLoginCreateRequest<P extends IsSecurityPrincipal>
     extends SecurityRequestBase<UserPasswordLoginCreateRequest<P>> {

	private static final long serialVersionUID = -3008422140261489038L;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="user")
	@Getter @Setter private P _securityPrincipal;
	
	@MarshallField(as="loginId")
	@Getter @Setter private LoginAndPassword _loginAndPassword;
	
	@MarshallField(as="initialLoginStatusWhenPrincipalAlreadyExists", 
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private LoginStatus _initialLoginStatusWhenPrincipalAlreadyExists;
	
	@MarshallField(as="initialPrincipalStatusWhenNew", 
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private SecurityPrincipalStatus _initialPrincipalStatusWhenNew;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDER
/////////////////////////////////////////////////////////////////////////////////////////	
	public UserPasswordLoginCreateRequest() {
		// default constructor
	}
	public UserPasswordLoginCreateRequest(final P securityPrincipal,
										  final LoginAndPassword loginAndPassword) {
		_securityPrincipal = securityPrincipal;
		_loginAndPassword = loginAndPassword;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	////////// --- SECURITY PRINCIPAL
	public SecurityPrincipalOID getSecurityPrincipalOid() {
		return _securityPrincipal != null ? _securityPrincipal.getOid() : null;
	}
	public SecurityPrincipalID getSecurityPrincipalId() {
		return _securityPrincipal != null ? _securityPrincipal.getId() : null;
	}
	////////// --- LOGIN & PASSWORD
	public LoginID getLoginId() {
		return _loginAndPassword != null ? _loginAndPassword.getLoginId() : null;
	}
	public Password getPassword() {
		return _loginAndPassword != null ? _loginAndPassword.getPassword() : null;
	}
}
