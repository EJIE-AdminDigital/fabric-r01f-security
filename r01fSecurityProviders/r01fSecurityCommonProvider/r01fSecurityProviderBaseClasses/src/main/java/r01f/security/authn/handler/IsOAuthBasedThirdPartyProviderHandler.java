package r01f.security.authn.handler;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.crypto.Hash;
import r01f.oids.SecurityIDs.SecurityToken;
import r01f.security.authn.provider.config.AuthenticationProviderSecretConfig;
import r01f.types.url.Url;

public interface IsOAuthBasedThirdPartyProviderHandler<P extends IsIdentityProviderReturnedData> {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	 public Url getAuthorizationCodeUri(final Url targetUrl,final Hash hashIfRequired);
	 
	 public SecurityToken getAccessTokenUsing(final SecurityToken code,final SecurityToken codeChallengeIfRequired,
			 								  final Url redirectUri);
	 
	 public P fetchThirdPartySecurityPrincipalUsing(final SecurityToken accessToken);
	 
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	 
	 @Accessors(prefix="_")
	 @RequiredArgsConstructor
	 public static abstract class OAuthBasedThirdProviderHandlerBase<P extends IsIdentityProviderReturnedData> 
	  				   implements IsOAuthBasedThirdPartyProviderHandler<P> {
		 
		 protected final AuthenticationProviderSecretConfig _secretConfig;
	 }
	 
	 public static interface OAuthBasedProviderHandlerResult<P extends IsIdentityProviderReturnedData> {
		 public P getSecurityPrincipal();
		 
		 public  SecurityToken getAccessToken();
	 }

}
