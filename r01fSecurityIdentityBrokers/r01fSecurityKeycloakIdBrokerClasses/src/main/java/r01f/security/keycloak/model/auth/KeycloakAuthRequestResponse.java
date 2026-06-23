package r01f.security.keycloak.model.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.ModelObject;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.securitycontext.SecurityIDS.SecurityToken;


/**
 * Model representing an OAuth token response.
 * It contains information such as access token, refresh token, expiration times, and more.
 */
@Accessors(prefix="_")
@AllArgsConstructor
@NoArgsConstructor
public class KeycloakAuthRequestResponse
        implements ModelObject {
	private static final long serialVersionUID = 8705295055496765302L;
//////////////////////////////////////////////////////////////////////////////
//   FIELDS
//////////////////////////////////////////////////////////////////////////////
	/**
     * The access token.
     */
    @MarshallField(as="access_token")
    @Getter @Setter private SecurityToken _accessToken;
    
    /**
     * The expiration time of the access token in seconds.
     */
    @MarshallField(as="expires_in")
    @Getter @Setter private int _expiresIn;
    
    /**
     * The expiration time of the refresh token in seconds.
     */
    @MarshallField(as="refresh_expires_in")
    @Getter @Setter private int _refreshExpiresIn;
    
    /**
     * The refresh token used to obtain a new access token.
     */
    @MarshallField(as="refresh_token")
    @Getter @Setter private SecurityToken _refreshToken;
    
    /**
     * The type of the token (usually "bearer").
     */
    @MarshallField(as="token_type")
    @Getter @Setter private String _tokenType;
    
    /**
     * The ID token.
     */
    @MarshallField(as="id_token")
    @Getter @Setter private SecurityToken _idToken;
    
    /**
     * The not-before policy value indicating when the token is valid.
     */
    @MarshallField(as="not-before-policy")
    @Getter @Setter private int _notBeforePolicy;
    
    /**
     * The session state associated with the token.
     */
    @MarshallField(as="session_state")
    @Getter @Setter private String _sessionState;
    
    /**
     * The scope associated with the token.
     */
    @MarshallField(as="scope")
    @Getter @Setter private String _scope;
    
    /**
     * The issued token type.
     */
    @MarshallField(as="issued_token_type")
    @Getter @Setter private String _issuedTokenType;
}