package r01f.model.security.login.userpassword.messages.registration.acknowledge;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.SecurityPrincipalStatus;
import r01f.model.security.SecurityResponseBase;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityIDs.SecurityPrincipalID;
import r01f.oids.SecurityOIDs.LoginOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;

@MarshallType(as="userPasswordLoginRegistrationAcknowledgeResponse")
@Accessors(prefix="_")
public class UserPasswordLoginRegistrationAcknowledgeResponse
     extends SecurityResponseBase<UserPasswordLoginRegistrationAcknowledgeResult,
     							  UserPasswordLoginRegistrationAcknowledgeResponse> {

	private static final long serialVersionUID = -6118544836852486080L;

/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
////////// --- PRINCIPAL
	@MarshallField(as="principalOid",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter private SecurityPrincipalOID _principalOid;
	
	@MarshallField(as="principalId",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter private SecurityPrincipalID _principalId;
	
	@MarshallField(as="principalStatusAfterLoginConfirmation",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter private SecurityPrincipalStatus _principalStatusAfterConfirmation;
////////// --- LOGIN
	@MarshallField(as="loginOid",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter private LoginOID _loginOid;
	
	@MarshallField(as="loginId",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter private LoginID _loginId;
}
