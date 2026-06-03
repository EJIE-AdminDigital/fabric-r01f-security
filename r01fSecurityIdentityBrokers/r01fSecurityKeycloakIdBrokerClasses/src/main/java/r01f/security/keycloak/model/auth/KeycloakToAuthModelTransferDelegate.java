package r01f.security.keycloak.model.auth;

import org.keycloak.representations.AccessTokenResponse;

import r01f.securitycontext.SecurityIDS.SecurityToken;

public class KeycloakToAuthModelTransferDelegate {
	
	public static KeycloakAuthRequestResponse toKeycloalAuthRequestResponse(final AccessTokenResponse source) {
        if (source == null) {
            return null;
        }
        
        KeycloakAuthRequestResponse target = new KeycloakAuthRequestResponse();

        // Mapping tokens to [String -> SecurityToken]
        target.setAccessToken(SecurityToken.from(source.getToken()));
        target.setRefreshToken(SecurityToken.from(source.getRefreshToken()));
        target.setIdToken(SecurityToken.from(source.getIdToken()));

        // Mapping expiration times and policies.
        target.setExpiresIn((int) source.getExpiresIn());
        target.setRefreshExpiresIn((int) source.getRefreshExpiresIn());
        target.setNotBeforePolicy(source.getNotBeforePolicy());

        // Mapeo de Strings y Metadatos
        target.setTokenType(source.getTokenType());
        target.setSessionState(source.getSessionState());
        target.setScope(source.getScope());
        
        // [Warning! El campo issued_token_type suele venir en el mapa de "otherClaims" 
        // en el objeto original de Keycloak si es un Token Exchange
        Object issuedTokenType = source.getOtherClaims().get("issued_token_type");
        if (issuedTokenType != null) {
            target.setIssuedTokenType(issuedTokenType.toString());
        }

        return target;

      
    }


}
