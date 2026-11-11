package r01f.model.security.login.userpassword.messages.registration.acknowledge;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.SecurityPrincipalStatus;
import r01f.model.security.SecurityRequestBase;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.SecurityOIDs.LoginOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;

@MarshallType(as="userPasswordLoginConfirmationRequest")
@Accessors(prefix="_")
public class UserPasswordLoginRegistrationAcknowledgeRequest 
     extends SecurityRequestBase<UserPasswordLoginRegistrationAcknowledgeRequest> {

	private static final long serialVersionUID = 4167730519554321812L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="securityPrincipalOid",
				   whenXml=@MarshallFieldAsXml(attr=false))
	@Getter @Setter private SecurityPrincipalOID _securityPrincipalOid;
	
	@MarshallField(as="loginOid",
				   whenXml=@MarshallFieldAsXml(attr=false))
	@Getter @Setter private LoginOID _loginOid;
	
	@MarshallField(as="principalStatusAfterAcknowledgement",
				   whenXml=@MarshallFieldAsXml(attr=false))
	@Getter @Setter private SecurityPrincipalStatus _principalStatusAfterAcknowledgement;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public UserPasswordLoginRegistrationAcknowledgeRequest() {
		// default no-args constructor
	}
	public UserPasswordLoginRegistrationAcknowledgeRequest(final SecurityPrincipalOID principalOid,final LoginOID loginOid) {
		this(principalOid,loginOid,
			 SecurityPrincipalStatus.ACTIVE);
	}
	public UserPasswordLoginRegistrationAcknowledgeRequest(final SecurityPrincipalOID principalOid,final LoginOID loginOid,
														   final SecurityPrincipalStatus statusAfterConfirmation) {
		_loginOid = loginOid;
		_principalStatusAfterAcknowledgement = statusAfterConfirmation;
	}
}
