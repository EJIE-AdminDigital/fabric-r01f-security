package r01f.security.rest.resource;

import java.net.URI;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.jwt.JWT;
import r01f.rest.RESTResourceBase;
import r01f.rest.resources.delegates.RESTDelegate;
import r01f.servlet.HttpServletRequestUtils;
import r01f.types.StringBase64;
import r01f.types.url.Url;

@Slf4j
@Accessors(prefix="_")
public abstract class RESTResourceForSecurityServicesBase
	          extends RESTResourceBase  {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public RESTResourceForSecurityServicesBase(final RESTDelegate delegate) {
		super(delegate);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	Just says hello!!
/////////////////////////////////////////////////////////////////////////////////////////
	@GET
	@Path("/")
	public Response hello() {
		return Response.ok().entity("hi").build();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	Redirs to the [security provider] login entry point (with the given url as param)
/////////////////////////////////////////////////////////////////////////////////////////
	@GET	// usually called from the security filter
	@Path("/login")
	public Response redirToSecurityProviderLoginEntryPointWith(     @QueryParam("to") final Url afterLoginRedirToUrl,
															   @QueryParam("toeib64") final StringBase64 afterLoginToUrlExtaInfoBase64,	// used to store "extra" info at the [after login redir url]
															      @QueryParam("impl") final String impl) {			// impl is used at SAML security provider (see SAMLLoginEntryPointHandler)
		return this.getDelegateAs(RESTDelegateForLoginBase.class)
				   .redirToSecurityProviderLoginEntryPointWith(afterLoginRedirToUrl,afterLoginToUrlExtaInfoBase64,
						   									   impl);

	}
/////////////////////////////////////////////////////////////////////////////////////////
//	Returns [user data] assuming a valid JWT is received (valid login)
/////////////////////////////////////////////////////////////////////////////////////////
	@GET	// usually called from the security filter
	@Path("/login/{jwt}")
	public Response fetchUser(@PathParam("jwt") final JWT jwt) {
		return this.getDelegateAs(RESTDelegateForLoginBase.class)
				   .fetchUser(jwt,
						   	  null);	// no [security provider]'s session data
	}
	@POST	// usually called from the security filter (see UserDataFromJWT)
	@Path("/login/{jwt}")
	public Response fetchUserUsingSecurityProviderData(@PathParam("jwt") final JWT jwt,
													   					 final String securityProviderSessionBase64) {
		log.info("...call fetchUserUsingSecurityProviderData!!");
		return this.getDelegateAs(RESTDelegateForLoginBase.class)
				   .fetchUser(jwt,
						      StringBase64.fromBase64EncodedString(securityProviderSessionBase64));	// this DOES NOT encode or decode the received param to BASE64
																									// ... it JUST ASSUMES the received param is BASE64 encoded
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LOGOUT
/////////////////////////////////////////////////////////////////////////////////////////
	@GET
	@Path("/logout")
	@SneakyThrows
	public Response logout(@QueryParam("uiLogoutUrl") final Url uiLogoutUrl,	// this is the {ui webApp} /logout url
						   @Context final UriInfo uriInfo) {
		// 		 the logout flow is like:
		//
		//               [UI]         [SecurityRestServices]
		//                |                       |
		//                +----redir----> /api/{providerId}/users/logout
		//                |                       |
		//                |                      [ ] ----> register logout
		//                |                       |
		//            /logout <------redir--------+
		//                |
		//             [filter] (remove everything)


		// this is the place to register the [log out]
		// ...

		// redir to the login page BUT including a parameter that "gives a clue" that the [user]
		// has clicked the [log-out] button
		URI theLoginUIUri = HttpServletRequestUtils.clientRequestedHost(_req)
												   .asUrl().joinWith(uiLogoutUrl.getUrlPath())
												   .asUri();
//		URI theLoginUIUri = uriInfo.getAbsolutePathBuilder()
//							       .replacePath(uiLogoutUrl.asString())
//							       .build();
		return Response.seeOther(theLoginUIUri)
					   .build();
	}
}


