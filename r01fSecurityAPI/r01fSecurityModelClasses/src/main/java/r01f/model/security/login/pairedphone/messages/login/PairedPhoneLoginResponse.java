package r01f.model.security.login.pairedphone.messages.login;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.eventbus.Event;
import r01f.model.security.SecurityResponseBase;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.oids.SecurityIDs.SecurityPrincipalID;
import r01f.oids.SecurityOIDs.PairedPhoneLoginConsentOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;

@Accessors(prefix="_")
public class PairedPhoneLoginResponse
     extends SecurityResponseBase<PairedPhoneLoginResult,
     							  PairedPhoneLoginResponse> 
  implements Event {

	private static final long serialVersionUID = -8942725466362564199L;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="securityPrincipalOid",
			       whenXml=@MarshallFieldAsXml(attr=true))
	@Getter	@Setter private SecurityPrincipalOID _securityPrincipalOid;
	
	@MarshallField(as="securityPrincipalId",
			       whenXml=@MarshallFieldAsXml(attr=true))
	@Getter	@Setter private SecurityPrincipalID _securityPrincipalId;
	
	@MarshallField(as="loginConsentOid",
			       whenXml=@MarshallFieldAsXml(attr=true))
	@Getter	@Setter private PairedPhoneLoginConsentOID _loginConsentOid;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////
	public PairedPhoneLoginResponse() {
		super();
	}
	public PairedPhoneLoginResponse(final PairedPhoneLoginResult result) {
		super(result);
	}
}