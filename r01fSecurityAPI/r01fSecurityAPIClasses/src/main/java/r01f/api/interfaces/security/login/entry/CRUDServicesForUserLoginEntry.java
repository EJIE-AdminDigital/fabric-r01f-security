package r01f.api.interfaces.security.login.entry;

import r01f.api.interfaces.security.SecurityServiceInterface;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.login.entry.UserLoginEntry;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.securitycontext.SecurityIDS.SecurityToken;
import r01f.securitycontext.SecurityIDS.UserLoginEntryID;
import r01f.securitycontext.SecurityOIDs.UserLoginEntryOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.interfaces.CRUDServicesForModelObject;

public interface CRUDServicesForUserLoginEntry<E extends UserLoginEntry>
		 extends CRUDServicesForModelObject<UserLoginEntryOID,E>,
		 		 SecurityServiceInterface {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * load las login entry (the most recent one)
	 * @param securityContext
	 * @param providerId
	 * @param loginId
	 * @param entryId
	 * @return
	 */
	public CRUDResult<E> loadLastLoginEntryFor(final SecurityContext securityContext,
											   final SecurityProviderID providerId,final LoginID loginId,
											   final UserLoginEntryID entryId);
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Loads a login entry for a password reset entry
	 * It's called from the [security rest services] when a [password reset request] is received
	 * Remember the password recovery flow:
	 * 		1.- Create a [login entry] with id=PASSWORD_RECOVERY_REQUEST and a [token]
	 * 		2.- Send an [email] to the user with a url like http://site/securityRestEndPoint/?userOid={}&token={}
	 * 		3.- When the user clicks the link the request gets to the [security REST end point] and
	 * 				a) a [login entry] with id=PASSWORD_RECOVERY_REQUEST and loginId={} is loaded
	 * 				b) the [token] at the [login entry] is compared with the received one
	 * 				c) it the [token] matches, the user can be redirected to the [password change ui]
	 * @param securityContext the user auth data & context info
	 * @param userOid the user oid
	 * @param token the entity identifier
	 * @return a {@link CRUDResult} that encapsulates the entity if it was loaded successfully
	 */
	public CRUDResult<E> loadForPasswordReset(final SecurityContext securityContext,
											  final UserOID userOid,
											  final SecurityToken token);

	/**
	 * Updates a login entry after a password reset
	 * @param securityContext the user auth data & context info
	 * @param userOid the user oid
	 * @param token the entity identifier
	 * @return a {@link CRUDResult} that encapsulates the entity if it was loaded successfully
	 */
	public CRUDResult<E> updateAfterPasswordReset(final SecurityContext securityContext,
												  final UserOID userOid,
												  final SecurityToken token);
/////////////////////////////////////////////////////////////////////////////////////////
//	IMPERSONATION
//  The UI requesting the impersonation makes a REST request with:
//  		- The impersonating user oid > the user requesting the impersonation > usually an admin user
//  		- The impersonated user oid > the user being impersonated
//  This method:
//  		1.- Generates a RANDOM [security token]
//  		2.- Creates in [impersonation login entry] that contains the previously-generated RANDOM [security token]
//   	3.- Generates a [impersonation JWT] that contains:
//   			a) the impersonating user oid
//   			b) the impersonated user oid
//   		The [impersonation JWT] is SIGNED with the previously-generated RANDOM [security token]
//  		4.- Return
//  				a) The [impersonation JWT] 
//  				b) the [impersonation login entry oid] (seed) 
//  
//  Later, when requesting anything on behalf another user (impersonating another user) the UI MUST:
//  		- include a [request parameter] named [impersonateJwt] that contains the generated [impersonation JWT]
//  		- include a [request parameter] or a COOKIE named [impersonateToken] that contains the [impersonation login entry oid]
//  
//  At the [login filter] (see MT01UsersLoginServletFilter)
//  		1.- Check if the [impersonateJwt] is present; if so, it's an impersonation request
//  		2.- Load the [impersonation login entry] using the oid that comes at the [JWT], a [request parameter] or a COOKIE named [impersonateToken]
//  		3.- Get the RANDOM [security token] at the [impersonation login entry] and use it to VERIFY the received [impersonation JWT]
//  			(remember that the [impersonation JWT] was signed using the RANDOM [security token])
//  		4.- Once the [impersonation JWT] is verified, extract the [impersonating user oid] and the [impersonated user oid]
//  		5.- Check the users:
//  				a) the [impersonating user] must be the logged user
//  				b) the [impersonated user] must exist
//  		6.- Replace the [login session] and [security context] by a NEW one where the user is the one being impersonated
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * PUSH (creates) a new [impersonation user login entry]
	 * @param securityContext
	 * @param impersonatingUserOid
	 * @param impersonatedUserOid
	 * @param token
	 * @return
	 */
	public CRUDResult<E> pushImpersonationLogin(final SecurityContext securityContext,
												final UserOID impersonatingUserOid,final UserOID impersonatedUserOid,
												final SecurityToken token);
	/**
	 * POPs (loads) a previously-created [impersonation user login entry]
	 * @param securityContext
	 * @param loginEntryOid
	 * @return
	 */
	public CRUDResult<E> popImpersonationLogin(final SecurityContext securityContext,
											   final UserLoginEntryOID loginEntryOid);
}

