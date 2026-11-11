package r01f.model.security.login.pairedphone.messages.login;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.SecurityRequestBase;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.SecurityIDs.SecurityToken;
import r01f.oids.SecurityOIDs.PairedPhoneLoginConsentOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;

@MarshallType(as = "loginConsentResponseFromPairedPhone")
@Accessors(prefix = "_")
public class PairedPhoneLoginConsentResponseFromPairedPhone 
  	 extends SecurityRequestBase<PairedPhoneLoginConsentResponseFromPairedPhone> {
	
	private static final long serialVersionUID = -8135980254651198012L;
/////////////////////////////////////////////////////////////////////////////////////////
// FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="securityPrincipalOid",
			       whenXml=@MarshallFieldAsXml(attr=true))
	@Getter	@Setter private SecurityPrincipalOID _securityPrincipalOid;
	
	@MarshallField(as="loginConsentOid",
			       whenXml=@MarshallFieldAsXml(attr=true))
	@Getter	@Setter private PairedPhoneLoginConsentOID _loginConsentOid;
	
	@MarshallField(as="actiono",
			       whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private PairedPhoneConsentAction _consentAction;
	
	@MarshallField(as="securityToken")
	@Getter	@Setter private SecurityToken _securityToken;
}
