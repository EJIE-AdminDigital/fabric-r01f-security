package r01f.security.handler;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.crypto.Hash;
import r01f.security.provider.SecurityProviderSecretConfig;
import r01f.security.user.UserDataFromThirdPartySecurityProvider;
import r01f.securitycontext.SecurityIDS.SecurityToken;
import r01f.types.url.Url;

public interface IsOAuthBasedThirdPartyProviderHandler<U extends UserDataFromThirdPartySecurityProvider> {
	
////////////////////////////////////////////////////////////////////
/// METHODS TO IMPLEMENT
///////////////////////////////////////////////////////////////////	
	 public  Url getAuthorizationCodeUri(final  Url targetUrl , final Hash hashIfRequired);
	 
	 public  SecurityToken getAccessTokenUsing(final SecurityToken code,  final SecurityToken codeChallengeIfRequired,  final Url redirectUri);
	 
	 public  U fetchThirdPartyUserUsing(final SecurityToken accessToken );
	 
////////////////////////////////////////////////////////////////////
///  BASE
///////////////////////////////////////////////////////////////////	
	 @Accessors(prefix="_")
	 @RequiredArgsConstructor
	 public static abstract class OAuthBasedThirdProviderHandlerBase<U extends UserDataFromThirdPartySecurityProvider> 
	  	implements IsOAuthBasedThirdPartyProviderHandler<U> {
		 
		 final  SecurityProviderSecretConfig _secretConfig;
	 }
	 
	 public static interface  OAuthBasedProviderHandlerResult<U extends UserDataFromThirdPartySecurityProvider> {
		 public  U getUser();
		 
		 public  SecurityToken getAccessToken();
	 }

}
