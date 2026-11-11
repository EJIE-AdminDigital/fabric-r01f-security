/**
 *
 */
package r01f.model.security.principal;

import java.util.Collection;

import lombok.Getter;
import r01f.model.security.HasSystemWideRoles;
import r01f.model.security.IsSecurityObject;
import r01f.model.security.SecurityRoleInModule;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="securityPrincipalSystemWideRoles")
public record SecurityPrincipalSystemWideRoles(/**
												* [security principal] system-wide role
												* DO NO confuse with the [security principal]'s role on a [resource]!!!
												* This a general role like admin, master, or whatever
												*/
											   @MarshallField(as="roles",
															  whenXml=@MarshallFieldAsXml(collectionElementName="role"))
											   @Getter Collection<SecurityRoleInModule> systemWideRoles) 
  implements IsSecurityObject, 
  			 HasSystemWideRoles {
	// nothing
}
