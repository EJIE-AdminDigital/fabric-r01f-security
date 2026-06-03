package r01f.security.keycloak.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.debug.Debuggable;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;


/**
 * A result object from a request.
 */
@Accessors(prefix="_")
public abstract class KeycloakRequestResultBase<SELF_TYPE extends KeycloakRequestResultBase<SELF_TYPE>>
     extends KeycloakRequestBase<SELF_TYPE>
	implements Debuggable {
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter @Setter protected  KeycloakRequestedOperation _operation;	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
    public KeycloakRequestResultBase(final KeycloakRealm realm,
    						         final KeycloakRequestedOperation operation) {
    	super(realm);
    	_operation = operation;    	
    }
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
 	@Override
	public CharSequence debugInfo() {
		StringBuilder outDbgInfo = new StringBuilder();
	    outDbgInfo.append("KC operation result for ")
	              .append(_operation)
	              .append(" at realm/oid=")
	              .append(_realm);
	    return outDbgInfo.toString();
	}
}
