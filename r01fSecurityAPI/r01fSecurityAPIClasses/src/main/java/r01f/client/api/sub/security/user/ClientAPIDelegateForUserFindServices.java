package r01f.client.api.sub.security.user;

import java.util.Collection;

import jakarta.inject.Provider;

import r01f.api.interfaces.security.user.FindServicesForUser;
import r01f.model.security.user.User;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.api.delegates.ClientAPIDelegateForModelObjectFindServices;
import r01f.services.api.delegates.ClientAPIModelObjectChangesTrack;


public class ClientAPIDelegateForUserFindServices<U extends User>
	 extends ClientAPIDelegateForModelObjectFindServices<UserOID,U> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////

	public ClientAPIDelegateForUserFindServices(final Provider<SecurityContext>  securityContextProvider,
												final Marshaller marshaller,
												final FindServicesForUser<U> findServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  findServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Finds all users
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Collection<U> findAllUsers() {
		Collection<U> outUsers = (Collection<U>)this.getServiceProxyAs(FindServicesForUser.class)
												    .findAllUsers(this.getSecurityContext())
												    .getOrThrow();
		ClientAPIModelObjectChangesTrack.startTrackingChangesOnLoaded(outUsers);
		return outUsers;
	}
	/**
	 * Finds all users that have a system-wide role
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Collection<U> findUsersWithSystemWideRole() {
		Collection<U> outUsers = (Collection<U>)this.getServiceProxyAs(FindServicesForUser.class)
												    .findUsersWithSystemWideRole(this.getSecurityContext())
												    .getOrThrow();
		ClientAPIModelObjectChangesTrack.startTrackingChangesOnLoaded(outUsers);
		return outUsers;
	}
	/**
	 * Finds users by text
	 * @param text
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Collection<U> findByText(final String text) {
		Collection<U> outUsers = (Collection<U>)this.getServiceProxyAs(FindServicesForUser.class)
												    .findByText(this.getSecurityContext(),
												    			text)
												    .getOrThrow();
		ClientAPIModelObjectChangesTrack.startTrackingChangesOnLoaded(outUsers);
		return outUsers;
	}
}
