package r01f.security.keycloak.model.users;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.debug.Debuggable;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakUserID;
import r01f.security.keycloak.model.KeycloakOIDs.KeycloakUserOID;
import r01f.security.keycloak.model.KeycloakRequestResultBase;
import r01f.security.keycloak.model.KeycloakRequestedOperation;


/**
 * A result object from a request.
 */
@Accessors(prefix="_")
public abstract class KeycloakUserRequestResultBase<SELF_TYPE extends KeycloakUserRequestResultBase<SELF_TYPE>>
     extends KeycloakRequestResultBase<SELF_TYPE>
	implements Debuggable {
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter @Setter protected  KeycloakUserOID _userOid;
	@Getter @Setter protected  KeycloakUserID _userId;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
    public KeycloakUserRequestResultBase(final KeycloakRealm realm,
    									 final KeycloakUserOID userOid,final KeycloakUserID userId,
    						             final KeycloakRequestedOperation operation) {
    	super(realm,operation);    
    	_userOid  = userOid;
    	_userId  = userId;
    }
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
 	@Override
	public CharSequence debugInfo() {
		StringBuilder outDbgInfo = new StringBuilder();
	    outDbgInfo.append("KC [user] operation result for ")
		              .append(_operation)
		              .append(" at realm/oid/id =")
		              .append(_realm)
		              .append("/").append(_userOid)
	              .append("/").append(_userId);
	    return outDbgInfo.toString();
	}
}
