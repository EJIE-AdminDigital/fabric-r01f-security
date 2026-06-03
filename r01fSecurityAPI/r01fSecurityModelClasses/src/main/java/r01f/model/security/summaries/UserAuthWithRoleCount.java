package r01f.model.security.summaries;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.model.ModelObject;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallFrom;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.securitycontext.SecurityIDS.UserRole;

/**
 * The number of users that have a certain role
 */
@MarshallType(as="userAuthWithRoleCount")
@Accessors(prefix="_")
public class UserAuthWithRoleCount 
  implements ModelObject {

	private static final long serialVersionUID = 3233403889151847835L;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="userRole",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter private final UserRole _userRole;
	
	@MarshallField(as="count",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter private final long _count;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////ç
	public UserAuthWithRoleCount(@MarshallFrom("userRole") final UserRole userRole,@MarshallFrom("count") final long count) {
		_userRole = userRole;
		_count = count;
	}
}
