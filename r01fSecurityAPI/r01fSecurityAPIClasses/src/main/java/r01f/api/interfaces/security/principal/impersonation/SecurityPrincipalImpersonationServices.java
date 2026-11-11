package r01f.api.interfaces.security.principal.impersonation;

import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.impersonation.SecurityPrincipalImpersonation;
import r01f.model.services.COREServiceMethodExecResult;
import r01f.oids.SecurityOIDs.SecurityPrincipalImpersonationOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.interfaces.ServiceInterfaceForModelObject;


 /**
  *	IMPERSONATION
  *  The UI requesting the impersonation makes a request with:
  *  		- The impersonating [principal] > the one requesting the impersonation > usually an admin 
  *  		- The impersonated [principal] > the one being impersonated
  *  This method:
  *  	1.- Generates a RANDOM [security token]
  *  	2.- Creates a [impersonation] record that contains:
  *				- the impersonating [principal] (comes from the [security context])
  *				- the impersonated [principal] (comes from a parameter)
  *				- the previously-generated RANDOM [security token]
  *   	3.- Generates a [impersonation JWT] that contains:
  *   			- the impersonating [principal]
  *   			- the impersonated [principal]
  *   	4.-	The [impersonation JWT] is SIGNED with the previously-generated RANDOM [security token]
  *  	5.- Return
  *  			- The [impersonation JWT] 
  *  			- the [impersonation] oid (seed) 
  *  
  *  Later, when performing any operation on behalf another [principal] (impersonating another [principal])
  *  the UI MUST do ONE of the following:
  *  	- include a [request parameter] named [impersonateJwt] that contains the generated [impersonation JWT]
  *  	- include a [request parameter] named [impersonateToken] that contains the [impersonation] oid (seed)
  *		- include a COOKIE named [impersonateToken] that contains the [impersonation] oid (seed)
  *  
  *  At the [login filter] (see SecurityPrincipalLoginServletFilter)
  *  	1.- Check if the [impersonateJwt] is present; if so, it's an impersonation request
  *  	2.- Load the [impersonation] using the oid that comes at:
  *				- the [impersonateJWT]
  *				- a [request parameter] named [impersonateToken] 
  *				- a COOKIE named [impersonateToken]
  *  	3.- The [impersonation] contains the RANDOM [security token] that was used previously to SIGN the [impersonationJWT]
  *			and now can be used to VERIFY the received [impersonationJWT]
  *  	4.- Once the [impersonationJWT] is verified, extract from the [impersonationJWT] 
  *				- the impersonating [principal] oid
  *				- the impersonated [principal] oid
  *  	5.- Check the [principals]
  *  			- the impersonating [principal] must be the logged in user 
  *  			- the impersonated [principal] must exist
  *
  *  	6.- Replace the [login session] and [security context] by a NEW one where the [principal] is the one being impersonated
  */
public interface SecurityPrincipalImpersonationServices
		 extends ServiceInterfaceForModelObject<SecurityPrincipalImpersonationOID,SecurityPrincipalImpersonation> {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * CREATES a new [impersonation] that contains:
	 *	- the impersonating [principal] (comes from the [security context] = the logged in user)
	 *  - the impersonated [principal] (comes from a parameter)
	 * 	- a generated RANDOM [security token] that will be used to SIGN the [impersonation JWT]
	 * @param securityContext
	 * @param impersonatedPrincipalOid
	 * @param token
	 * @return
	 */
	public COREServiceMethodExecResult<SecurityPrincipalImpersonation> theSecurityContextPrincipalSTARTSImpersonating(final SecurityContext securityContext,
											   								  							   		      final SecurityPrincipalOID impersonatedPrincipalOid);
	/**
	 * ENDS the currently active [impersonation] for the [security context] impersonating [principal]
	 * @param securityContext
	 * @return
	 */
	public COREServiceMethodExecResult<SecurityPrincipalImpersonation> theSecurityContextPrincipalFINISHImpersonation(final SecurityContext securityContext);
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Loads a previously-created [impersonation] for a given impersonated [principal]
	 * that is still active 
	 * @param securityContext
	 * @param impersonatedPrincipalOid
	 * @return
	 */
	public COREServiceMethodExecResult<SecurityPrincipalImpersonation> loadActiveImpersonationForImpersonatedPrincipal(final SecurityContext securityContext,
																	     			  					   			   final SecurityPrincipalOID impersonatedPrincipalOid);
	/**
	 * Loads a previously-created [impersonation] for a given impersonated [principal]
	 * that is still active UPDATES it setting the [endedAt] date to NOW 
	 * loaded impersonating [login] 
	 * @param securityContext
	 * @param impersonatedPrincipalOid
	 * @return
	 */
	public COREServiceMethodExecResult<SecurityPrincipalImpersonation> endCurrentlyActiveImpersonationForImpersonatedPrincipal(final SecurityContext securityContext,
																							  					   			   final SecurityPrincipalOID impersonatedPrincipalOid);
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Loads a previously-created [impersonation] for a given impersonating [principal]
	 * that is still active 
	 * @param securityContext
	 * @param impersonatingPrincipalOid
	 * @return
	 */
	public COREServiceMethodExecResult<SecurityPrincipalImpersonation> loadActiveImpersonationForImpersonatingPrincipal(final SecurityContext securityContext,
																					  									final SecurityPrincipalOID impersonatingPrincipalOid);
	/**
	 * Loads a previously-created [impersonation] for a given impersonating [principal]
	 * that is still active UPDATES it setting the [endedAt] date to NOW 
	 * @param securityContext
	 * @param impersonatingLoginOid
	 * @return
	 */
	public COREServiceMethodExecResult<SecurityPrincipalImpersonation> endCurrentlyActiveImpersonationForImpersonatingPrincipal(final SecurityContext securityContext,
																							  									final SecurityPrincipalOID impersonatingPrincipalOid);
}

