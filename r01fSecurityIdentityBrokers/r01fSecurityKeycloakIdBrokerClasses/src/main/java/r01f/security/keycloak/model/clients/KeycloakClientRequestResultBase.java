package r01f.security.keycloak.model.clients;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.debug.Debuggable;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;
import r01f.security.keycloak.model.KeycloakOIDs.KeycloakClientOID;
import r01f.security.keycloak.model.KeycloakRequestResultBase;
import r01f.security.keycloak.model.KeycloakRequestedOperation;


/**
 * A result object from a request.
 */
@Accessors(prefix="_")
public abstract class KeycloakClientRequestResultBase<SELF_TYPE extends KeycloakClientRequestResultBase<SELF_TYPE>>
     extends KeycloakRequestResultBase<SELF_TYPE>
	implements Debuggable {
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter @Setter protected  KeycloakClientOID _clientOid;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
    public KeycloakClientRequestResultBase(final KeycloakRealm realm,final KeycloakClientOID clientOid,
    						          final KeycloakRequestedOperation operation) {
    	super(realm,operation);    
    	_clientOid  = clientOid;
    }
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
 	@Override
	public CharSequence debugInfo() {
		StringBuilder outDbgInfo = new StringBuilder();
	    outDbgInfo.append("KC [client] operation result for ")
	              .append(_operation)
	              .append(" at realm/oid=")
	              .append(_realm)
	              .append("/").append(_clientOid);
	    return outDbgInfo.toString();
	}
}
