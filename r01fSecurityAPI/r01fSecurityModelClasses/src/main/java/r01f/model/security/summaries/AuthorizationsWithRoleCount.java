package r01f.model.security.summaries;

import java.util.Objects;

import lombok.Getter;
import r01f.model.security.IsSecurityObject;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.SecurityIDs.SecurityRole;

/**
 * The number of [security principal] that have a certain [role]
 */
@MarshallType(as="authorizationsWithRoleCount")
public record AuthorizationsWithRoleCount(@MarshallField(as="role",
														 whenXml=@MarshallFieldAsXml(attr=true))
										  @Getter SecurityRole role,
											
										  @MarshallField(as="count",
														 whenXml=@MarshallFieldAsXml(attr=true))
										  @Getter long count)
   implements IsSecurityObject {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public AuthorizationsWithRoleCount {
		Objects.requireNonNull(role, "The role is mandatory");
		Objects.requireNonNull(count, "The count is mandatory");
	}
}
