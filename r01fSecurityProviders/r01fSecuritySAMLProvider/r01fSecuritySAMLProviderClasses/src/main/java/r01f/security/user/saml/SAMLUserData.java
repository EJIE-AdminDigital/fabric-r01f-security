package r01f.security.user.saml;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.guids.CommonOIDs.WorkPlaceCode;
import r01f.locale.Language;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.security.user.UserDataFromThirdPartySecurityProvider;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.types.contact.EMail;
import r01f.types.contact.Phone;

/** 
 * Models the data that XLNets returns
 */
@NoArgsConstructor
@MarshallType(as="samlUserData")
@Accessors(prefix="_")
public class SAMLUserData
  implements UserDataFromThirdPartySecurityProvider {

	private static final long serialVersionUID = 3189595900075153543L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	@MarshallField(as="loginId",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private LoginID _loginId;

	@MarshallField(as="dni",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private String _dni;

	@MarshallField(as="name",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private String _name;

	@MarshallField(as="surname",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private String _surname;

	@MarshallField(as="displayName",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private String _displayName;

	@MarshallField(as="mail",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private EMail _mail;

	@MarshallField(as="phone",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Phone _phone;

	@MarshallField(as="workPlaceCode",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private WorkPlaceCode _workPlaceCode;
	
	@MarshallField(as="prefLang",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Language _prefLang;
}
