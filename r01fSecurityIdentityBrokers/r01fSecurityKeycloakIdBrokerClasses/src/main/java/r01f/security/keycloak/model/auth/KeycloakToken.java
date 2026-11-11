package r01f.security.keycloak.model.auth;

import r01f.oids.SecurityIDs.SecurityToken;

public interface KeycloakToken {
   
	SecurityToken asSecurityToken();
    
	KeycloakTokenType getType();
   
    boolean isExpired(); 
    
    enum KeycloakTokenType { ACCESS, REFRESH, EXCHANGE }
}