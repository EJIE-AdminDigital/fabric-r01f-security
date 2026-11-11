package r01f.model.security.login.userpassword.messages.registration;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.SecurityResponseBase;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityOIDs.LoginOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;

@MarshallType(as="userPasswordLoginCreateResponse")
@Accessors(prefix="_")
public class UserPasswordLoginCreateResponse
     extends SecurityResponseBase<UserPasswordLoginCreateResult,
  							      UserPasswordLoginCreateResponse> {

	private static final long serialVersionUID = 96395944606115391L;

/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="securityPrincipalOid",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter private SecurityPrincipalOID _securityPrincipalOid;
	
	@MarshallField(as="loginOid",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter private LoginOID _loginOid;
	
	@MarshallField(as="loginId",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter private LoginID _loginId;
}
