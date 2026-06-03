package r01f.client.api.sub.security.login.entry;

import jakarta.inject.Provider;

import r01f.api.interfaces.security.login.entry.CRUDServicesForUserLoginEntry;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.login.entry.UserLoginEntry;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.SecurityToken;
import r01f.securitycontext.SecurityOIDs.UserLoginEntryOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.api.delegates.ClientAPIDelegateForModelObjectCRUDServices;

public class ClientAPIDelegateForUserLoginEntryCRUDServices<E extends UserLoginEntry>
	 extends ClientAPIDelegateForModelObjectCRUDServices<UserLoginEntryOID,E> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForUserLoginEntryCRUDServices(final Provider<SecurityContext>  securityContextProvider,
														  final Marshaller marshaller,
														  final CRUDServicesForUserLoginEntry<E> crudServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  crudServicesProxy);
	}
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
	 * @param userOid the user oid
	 * @param token the entity identifier
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public E loadForPasswordReset(final UserOID userOid,
								  final SecurityToken token) {
		CRUDResult<E> opResult = this.getServiceProxyAs(CRUDServicesForUserLoginEntry.class)
									 .loadForPasswordReset(this.getSecurityContext(),
											 			   userOid,token);
		E outEntity = opResult.getOrThrow();
		return outEntity;
	}
	/**
	 * Updates a login entry after a password reset
	 * @param userOid the user oid
	 * @param token the entity identifier
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public E updateAfterPasswordReset(final UserOID userOid,
									  final SecurityToken token) {
		CRUDResult<E> opResult = this.getServiceProxyAs(CRUDServicesForUserLoginEntry.class)
									 .updateAfterPasswordReset(this.getSecurityContext(),
											 			   	   userOid,token);
		E outEntity = opResult.getOrThrow();
		return outEntity;
	}
}
