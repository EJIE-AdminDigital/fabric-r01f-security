package r01f.security.handler;

import java.net.URI;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import r01f.security.login.AfterLoginRedirUrlExtraInfoStore;
import r01f.security.provider.SecurityProviderSecretConfig;
import r01f.types.StringBase64;
import r01f.types.url.Url;


public abstract class LoginEntryPointHandlerBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	protected final SecurityProviderSecretConfig _securityProviderSecretConfig;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public LoginEntryPointHandlerBase(final SecurityProviderSecretConfig securityProviderSecretConfig) {
		_securityProviderSecretConfig = securityProviderSecretConfig;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public Response redirToSecurityProviderLoginEntryPointWith(final Url afterLoginRedirToUrl) {
		return this.redirToSecurityProviderLoginEntryPointWith(afterLoginRedirToUrl,
															   null);	// no impl
	}
	public Response redirToSecurityProviderLoginEntryPointWith(final Url afterLoginRedirToUrl,
															   final String impl) {
		return this.redirToSecurityProviderLoginEntryPointWith(afterLoginRedirToUrl,null,	// no extra info base 64
															   impl);
	}
	public Response redirToSecurityProviderLoginEntryPointWith(final Url afterLoginRedirToUrl,final StringBase64 afterLoginToUrlExtaInfoBase64,
															   final String impl) {
		Url loginEntryPointUrl = this.getLoginEntryPointUrl(afterLoginRedirToUrl,afterLoginToUrlExtaInfoBase64,
															impl);
		URI loginEntryPointUri = UriBuilder.fromUri(loginEntryPointUrl.asString())
										   .build();
		return Response.seeOther(loginEntryPointUri)
					   .build();
	}
	/**
	 * Gets the login entrypoint uri, in case of google this is the  "authorization code uri",
	 * first step oth google oauth flow, where thew user will obtain an authorization code,.
	 * @param afterLoginRedirToUrl
	 * @param impl
	 * @return
	 */
	public abstract Url getLoginEntryPointUrl(final Url afterLoginRedirToUrl,
									 		  final String impl);
	/**
	 * Gets the login entrypoint uri, in case of google this is the  "authorization code uri",
	 * first step oth google oauth flow, where thew user will obtain an authorization code,.
	 * @param afterLoginRedirToUrl
	 * @param afterLoginToUrlExtaInfoBase64
	 * @param impl
	 * @return
	 */
	public Url getLoginEntryPointUrl(final Url afterLoginRedirToUrl,final StringBase64 afterLoginToUrlExtaInfoBase64,
									 final String impl) {
		// the [after login redir url] can store "extra" info in Base64 format that is appended as a [query string] param
		// ... so the final [after login redir url] is like:
		//			afterLoginRedirUrl?eb64=<any extra info in base 64>
		Url finalAfterLoginRedirUrl = afterLoginToUrlExtaInfoBase64 != null ? AfterLoginRedirUrlExtraInfoStore.addAsUrlQueryStringParam(afterLoginToUrlExtaInfoBase64)
																											  .to(afterLoginRedirToUrl)
																			: afterLoginRedirToUrl;
		// compose the real [after login redir url]
		Url theAfterLoginEntryPointUrl = this.getLoginEntryPointUrl(finalAfterLoginRedirUrl,
																	impl);
		return theAfterLoginEntryPointUrl;
	}
}
