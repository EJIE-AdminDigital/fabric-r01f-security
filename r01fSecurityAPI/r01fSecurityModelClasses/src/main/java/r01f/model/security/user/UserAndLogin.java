package r01f.model.security.user;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.aspects.interfaces.dirtytrack.ConvertToDirtyStateTrackable;
import r01f.model.ModelObject;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;

/**
 * Combines {@link User} info with login info
 * @param <U>
 */
@MarshallType(as="userAndLogin")
@ConvertToDirtyStateTrackable
@Accessors(prefix="_")
public class UserAndLogin<U extends User>
  implements ModelObject {

	private static final long serialVersionUID = -2597121838799468193L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="securityProviderId",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private SecurityProviderID _securityProviderId;

	@MarshallField(as="loginId",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private LoginID _loginId;
	
	@MarshallField(as="user")
	@Getter @Setter private U _user;
	
}
