package r01f.client.api.sub.security.authorization;

import java.util.Collection;

import jakarta.inject.Provider;
import r01f.api.interfaces.security.authorization.FindServicesForAuthorizationOnResource;
import r01f.aspects.interfaces.dirtytrack.DirtyStateTrackable;
import r01f.model.persistence.FindResult;
import r01f.model.security.authorization.SecurityPrincipalAuthorizationOnResource;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.oids.CommonOIDs.AppCode;
import r01f.oids.CommonOIDs.AppComponent;
import r01f.oids.SecurityOIDs.AuthorizationOnResourceOID;
import r01f.oids.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.api.delegates.ClientAPIDelegateForModelObjectFindServices;
import r01f.services.api.delegates.ClientAPIModelObjectChangesTrack;

public class ClientAPIDelegateForAuthorizationOnResourceFindServices
	 extends ClientAPIDelegateForModelObjectFindServices<AuthorizationOnResourceOID,SecurityPrincipalAuthorizationOnResource> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////

	public ClientAPIDelegateForAuthorizationOnResourceFindServices(final Provider<SecurityContext>  securityContextProvider,
													  			   final Marshaller marshaller,
													  			   final FindServicesForAuthorizationOnResource crudServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  crudServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns authorizations for the given security principal
	 * @param securityPrincipalOid
	 * @return
	 */
	public Collection<SecurityPrincipalAuthorizationOnResource> findBy(final SecurityPrincipalOID securityPrincipalOid) {
		FindResult<SecurityPrincipalAuthorizationOnResource> findResult = this.getServiceProxyAs(FindServicesForAuthorizationOnResource.class)
																			   .findBy(this.getSecurityContext(),
																					   securityPrincipalOid);
		Collection<SecurityPrincipalAuthorizationOnResource> result = findResult.getOrThrow();
		if (result!=null) {
			for (SecurityPrincipalAuthorizationOnResource auth : result) {
				if (auth instanceof DirtyStateTrackable) {
					ClientAPIModelObjectChangesTrack.startTrackingChangesOnLoaded(auth);
				}
			}
		}
		return result;
	}
	/**
	 * Returns all authorizations for a given security principal
	 * @param securityPrincipalOid
	 * @param appCode
	 * @param appComp
	 * @return
	 */
	public Collection<SecurityPrincipalAuthorizationOnResource> findBy(final SecurityPrincipalOID securityPrincipalOid,
																	   final AppCode appCode,final AppComponent appComp) {
		FindResult<SecurityPrincipalAuthorizationOnResource> findResult = this.getServiceProxyAs(FindServicesForAuthorizationOnResource.class)
																			   .findBy(this.getSecurityContext(),
																					   securityPrincipalOid,
																					   appCode,appComp);
		Collection<SecurityPrincipalAuthorizationOnResource> result = findResult.getOrThrow();
		if (result!=null) {
			for (SecurityPrincipalAuthorizationOnResource auth : result) {
				if (auth instanceof DirtyStateTrackable) {
					ClientAPIModelObjectChangesTrack.startTrackingChangesOnLoaded(auth);
				}
			}
		}
		return result;
	}
	/**
	 * Returns authorizations for the given resource
	 * @param userOid
	 * @return
	 */
	public Collection<SecurityPrincipalAuthorizationOnResource> findBy(final AuthorizationTargetResourceOID resOid) {
		FindResult<SecurityPrincipalAuthorizationOnResource> findResult = this.getServiceProxyAs(FindServicesForAuthorizationOnResource.class)
																			   .findBy(this.getSecurityContext(),
																					   resOid);
		return findResult.getOrThrow();
	}
	/**
	 * Returns authorizations for the given user at the given resource
	 * @param securityPrincipalOid
	 * @param resOid
	 * @return
	 */
	public Collection<SecurityPrincipalAuthorizationOnResource> findBy(final SecurityPrincipalOID securityPrincipalOid,
																	   final AuthorizationTargetResourceOID resOid) {
		FindResult<SecurityPrincipalAuthorizationOnResource> findResult = this.getServiceProxyAs(FindServicesForAuthorizationOnResource.class)
																			   .findBy(this.getSecurityContext(),
																					   securityPrincipalOid,
																					   resOid);
		return findResult.getOrThrow();
	}
}
