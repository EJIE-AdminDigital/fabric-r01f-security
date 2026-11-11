package r01f.client.api.sub.security.principal;

import java.util.Collection;

import jakarta.inject.Provider;
import r01f.api.interfaces.security.principal.FindServicesForPrincipal;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.api.delegates.ClientAPIDelegateForModelObjectFindServices;
import r01f.services.api.delegates.ClientAPIModelObjectChangesTrack;


public class ClientAPIDelegateForPrincipalFindServices<P extends IsSecurityPrincipal>
	 extends ClientAPIDelegateForModelObjectFindServices<SecurityPrincipalOID,P> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////

	public ClientAPIDelegateForPrincipalFindServices(final Provider<SecurityContext>  securityContextProvider,
													 final Marshaller marshaller,
													 final FindServicesForPrincipal<P> findServicesProxy) {
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
	public Collection<P> findAllPrincipals() {
		Collection<P> outUsers = (Collection<P>)this.getServiceProxyAs(FindServicesForPrincipal.class)
												    .findAllPrincipals(this.getSecurityContext())
												    .getOrThrow();
		ClientAPIModelObjectChangesTrack.startTrackingChangesOnLoaded(outUsers);
		return outUsers;
	}
	/**
	 * Finds all users that have a system-wide role
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Collection<P> findWithSystemWideRole() {
		Collection<P> outUsers = (Collection<P>)this.getServiceProxyAs(FindServicesForPrincipal.class)
												    .findHavingAnySystemWideRole(this.getSecurityContext())
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
	public Collection<P> findByText(final String text) {
		Collection<P> outUsers = (Collection<P>)this.getServiceProxyAs(FindServicesForPrincipal.class)
												    .findByText(this.getSecurityContext(),
												    			text)
												    .getOrThrow();
		ClientAPIModelObjectChangesTrack.startTrackingChangesOnLoaded(outUsers);
		return outUsers;
	}
}
