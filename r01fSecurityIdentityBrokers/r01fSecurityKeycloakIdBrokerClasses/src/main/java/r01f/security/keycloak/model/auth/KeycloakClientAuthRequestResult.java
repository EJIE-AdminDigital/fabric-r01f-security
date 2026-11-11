package r01f.security.keycloak.model.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import r01f.debug.Debuggable;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;
import r01f.security.keycloak.model.KeycloakRequestBase;


/**
 * A response object from an auth request.
 */
@Accessors(prefix="_")
@NoArgsConstructor
public  class KeycloakClientAuthRequestResult
     extends KeycloakRequestBase<KeycloakClientAuthRequestResult>
	implements Debuggable {
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="response")
	@Getter KeycloakAuthRequestResponse  _response;
	@MarshallField(as="error")
	@Getter String _error; //  'invalid_grant', 'account_disabled', etc.
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
    public KeycloakClientAuthRequestResult(final KeycloakRealm realm, final KeycloakAuthRequestResponse  reponse) {
    	super(realm);    	
    	_response  = reponse;
    	_error = null;
    }
    
    public KeycloakClientAuthRequestResult(final KeycloakRealm realm, final String error) {
    	super(realm);    	
    	_response  = null;
    	_error = error;
    }
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
    @Override
	public CharSequence debugInfo() {
        StringBuilder outDbgInfo = new StringBuilder();
        outDbgInfo.append("KC Auth operation result")
                  .append(" at realm=")
                  .append(_realm)
                  .append(" - Tokens present: ")
                  .append(_response != null);
        return outDbgInfo.toString();
    }
}
