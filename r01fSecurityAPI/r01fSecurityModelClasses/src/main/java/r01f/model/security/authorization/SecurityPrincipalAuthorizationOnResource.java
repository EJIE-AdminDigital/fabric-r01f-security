package r01f.model.security.authorization;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.aspects.interfaces.dirtytrack.ConvertToDirtyStateTrackable;
import r01f.debug.Debuggable;
import r01f.model.security.IsSecurityPersistableObject;
import r01f.model.security.SecurityPersistableObjectBase;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.oids.SecurityIDs.SecurityRole;
import r01f.oids.SecurityOIDs.AuthorizationOnResourceOID;
import r01f.oids.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.types.CanBeRepresentedAsString;
import r01f.util.types.Strings;

/**
 * Models an [security principal]'s [authorization] on a target [resource]
 */
@ConvertToDirtyStateTrackable
@Accessors(prefix="_")
@NoArgsConstructor
public class SecurityPrincipalAuthorizationOnResource
	 extends SecurityPersistableObjectBase<AuthorizationOnResourceOID,
		  								   SecurityPrincipalAuthorizationOnResource>
  implements IsSecurityPersistableObject<AuthorizationOnResourceOID>,
 		 	 HasSecurityRole,
   			 CanBeRepresentedAsString,
   			 Debuggable {

	private static final long serialVersionUID = 6754519051550137327L;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="securityPrincipalOid",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private SecurityPrincipalOID _securityPrincipalOid;
	
	@MarshallField(as="authorizationTargetResourceOid",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private AuthorizationTargetResourceOID _authorizationTargetResourceOid;
	
	@MarshallField(as="role",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private SecurityRole _role;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public String asString() {
		return Strings.customized("[security principal]={} authorized at [resource]={} with [role]={}", 
								  _securityPrincipalOid,_authorizationTargetResourceOid,_role);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	DEBUG
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CharSequence debugInfo() {
		return this.asString();
	}

}
