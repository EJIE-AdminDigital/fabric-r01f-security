package r01f.security.rest.resource.webauthn;

import org.glassfish.jersey.media.multipart.FormDataParam;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.jwt.JWT;
import r01f.rest.RESTResourceBase;
import r01f.rest.resources.delegates.RESTDelegate;
import r01f.security.rest.delegates.resource.webauthn.RESTDelegateForWebAuthnBase;
import r01f.securitycontext.SecurityIDS.LoginID;


@Slf4j
@Accessors(prefix="_")
public abstract class RESTResourceForWebAuthnServicesBase
	          extends RESTResourceBase  {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public RESTResourceForWebAuthnServicesBase(final RESTDelegate delegate) {
		super(delegate);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	Just says hello!!
/////////////////////////////////////////////////////////////////////////////////////////
	@GET
	@Path("/")	@SuppressWarnings("static-method")
	public Response hello() {
		return Response.ok().entity("// webauthn- services //").build();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	ATTESTATION ( REGISTRATION )
/////////////////////////////////////////////////////////////////////////////////////////
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("/registration/start")
	public Response startWebAuthnRegistration(  @FormDataParam("loginId")  final LoginID loginId) {
		return this.getDelegateAs(RESTDelegateForWebAuthnBase.class)
						.startWebAuthnRegistration(loginId);
	}


    @POST
	@Path("/registration/finish")
	public Response finishWebAuthnRegistration( @FormDataParam("loginId")   final LoginID loginId,
                                                @FormDataParam("credName")  final String credNameAsString,
			                                    @FormDataParam("publicKeyCredentialRegistrationResponse") final String publicKeyCredentialRegistrationResponse,
			                                    @FormDataParam("publicKeyCredentialCreateOption")         final String publicKeyCredentialCreateOption
                                               ) {
		return this.getDelegateAs(RESTDelegateForWebAuthnBase.class)
				   .finishWebAuthnRegistration(loginId,credNameAsString,
						                       publicKeyCredentialRegistrationResponse,
						                       publicKeyCredentialCreateOption);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	ASSERTION ( AUTH BASED ON CREDENTIALS )
/////////////////////////////////////////////////////////////////////////////////////////
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("/authentication/start")
	public Response startWebAuthnAuthentication( @FormDataParam("loginId") final LoginID loginId) {
		log.warn("/authentication/start {}", loginId.asString());
		return this.getDelegateAs(RESTDelegateForWebAuthnBase.class)
				   .startWebAuthAuthentication(loginId);
	}


	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("/authentication/finish")
	public Response finishWebAuthnAuthentication( @FormDataParam("publicKeyCredentialAssertResponse") final String publicKeyCredentialAssertResponse ,
			                                      @FormDataParam("publicKeyCredentialAssertRequest")  final String publicKeyCredentialAssertRequest,
			                                      @FormDataParam("to")                                final String afterRedirUri) {
		log.warn(" to : {} ", afterRedirUri);
		return this.getDelegateAs(RESTDelegateForWebAuthnBase.class)
					   .finishWebAuthAuthentication(publicKeyCredentialAssertResponse,
							                        publicKeyCredentialAssertRequest,afterRedirUri);
	}

/////////////////////////////////////////////////////////////////////////////////////////
// LOGIN / FETCH USER ( FROM FILTER )
/////////////////////////////////////////////////////////////////////////////////////////
	@GET	// ... called from the security filter
	@Path("/login/{jwt}")
	public Response fetchUser(@PathParam("jwt") final JWT jwt) {
		return this.getDelegateAs(RESTDelegateForWebAuthnBase.class)
				   .fetchUser(jwt);
	}
}
