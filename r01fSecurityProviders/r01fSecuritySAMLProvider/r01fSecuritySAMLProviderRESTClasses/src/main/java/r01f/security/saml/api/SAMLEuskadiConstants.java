package r01f.security.saml.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.security.saml.api.SAMLIDs.SAMLIdPID;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class SAMLEuskadiConstants {
/////////////////////////////////////////////////////////////////////////////////////////
//	SOME IdP ids
/////////////////////////////////////////////////////////////////////////////////////////
	public static SAMLIdPID ELKARLAN = SAMLIdPID.forId("elkarlan");
	public static SAMLIdPID ELKARLAN2 = SAMLIdPID.forId("elkarlan2");
	public static SAMLIdPID OKTA = SAMLIdPID.forId("okta");
	public static SAMLIdPID OSAAZUREAD = SAMLIdPID.forId("osaazuread");
	public static SAMLIdPID OSAAZUREAD_PD = SAMLIdPID.forId("osaazureadpd");
	
}
