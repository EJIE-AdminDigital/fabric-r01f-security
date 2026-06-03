package r01f.model.security.authorization;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.aspects.interfaces.dirtytrack.ConvertToDirtyStateTrackable;
import r01f.debug.Debuggable;
import r01f.model.security.PersistableSecurityObjectBase;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.securitycontext.SecurityIDS.UserRole;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.securitycontext.SecurityOIDs.UserAuthorizationOnResourceOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.types.CanBeRepresentedAsString;
import r01f.util.types.Strings;

/**
 * Models an user's authorization on a target resource
 */
@ConvertToDirtyStateTrackable
@Accessors(prefix="_")
@NoArgsConstructor
public abstract class UserAuthorizationOnResourceBase<SELF_TYPE extends UserAuthorizationOnResourceBase<SELF_TYPE>>
			  extends PersistableSecurityObjectBase<UserAuthorizationOnResourceOID,
			  										SELF_TYPE>
		   implements UserAuthorizationOnResource,
		   			  CanBeRepresentedAsString,
		   			  Debuggable {

	private static final long serialVersionUID = 6754519051550137327L;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="userOid",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private UserOID _userOid;
	
	@MarshallField(as="authorizationTargetResourceOid",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private AuthorizationTargetResourceOID _authorizationTargetResourceOid;
	
	@MarshallField(as="role",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private UserRole _role;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public String asString() {
		return Strings.customized("[user]={} authorized at [resource]={} with role={}", 
								  _userOid,_authorizationTargetResourceOid,_role);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	DEBUG
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CharSequence debugInfo() {
		return this.asString();
	}

}
