package r01f.model.security.login.data.userpassword;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.IsLoginData;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.SecurityIDs.Password;
import r01f.oids.SecurityIDs.PasswordHash;
import r01f.oids.SecurityIDs.SecurityToken;

@MarshallType(as="loginDataForUserPassword")
@Accessors(prefix="_")
public class LoginDataForUserPassword 
  implements IsLoginData {

	private static final long serialVersionUID = -5072810165746569102L;

/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="isConfirmed", 
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private boolean _confirmed = true;// Confirmed by default
	
////////// --- PASSWORD	
	@MarshallField(as="password")
	@Getter @Setter private Password _password;
	
	@MarshallField(as="passwordHash")
	@Getter @Setter private PasswordHash _passwordHash;

	@MarshallField(as="passwordExpiresAt",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601))
	@Getter @Setter private Instant _passwordExpiresAt;
	
////////// --- PASSWORD RESET TOKEN
	@MarshallField(as="passwordResetToken")
	@Getter @Setter private SecurityToken _passwordResetToken;
	
	@MarshallField(as="passwordResetTokenExpiresAt",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601))
	@Getter @Setter private Instant _passwordResetTokenExpiresAt;	
}
