package r01f.client.api.sub.security.authorization;

import java.util.Collection;

import jakarta.inject.Provider;

import r01f.api.interfaces.security.authorization.FindServicesForUserAuthorizationOnResource;
import r01f.aspects.interfaces.dirtytrack.DirtyStateTrackable;
import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.model.persistence.FindResult;
import r01f.model.security.authorization.UserAuthorizationOnResource;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.securitycontext.SecurityOIDs.UserAuthorizationOnResourceOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.api.delegates.ClientAPIDelegateForModelObjectFindServices;
import r01f.services.api.delegates.ClientAPIModelObjectChangesTrack;

public class ClientAPIDelegateForUserAuthorizationOnResourceFindServices<A extends UserAuthorizationOnResource>
	 extends ClientAPIDelegateForModelObjectFindServices<UserAuthorizationOnResourceOID,A> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////

	public ClientAPIDelegateForUserAuthorizationOnResourceFindServices(final Provider<SecurityContext>  securityContextProvider,
													  				   final Marshaller marshaller,
													  				   final FindServicesForUserAuthorizationOnResource<A> crudServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  crudServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns authorizations for the given user
	 * @param userOid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Collection<A> findBy(final UserOID userOid) {
		FindResult<A> findResult = this.getServiceProxyAs(FindServicesForUserAuthorizationOnResource.class)
									   .findBy(this.getSecurityContext(),
											   userOid);
		Collection<A> result = findResult.getOrThrow();
		if (result!=null) {
			for (A auth : result) {
				if (auth instanceof DirtyStateTrackable) {
					ClientAPIModelObjectChangesTrack.startTrackingChangesOnLoaded(auth);
				}
			}
		}
		return result;
	}
	/**
	 * Returns all authorizations for a given user
	 * @param userOid
	 * @param appCode
	 * @param appComp
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Collection<A> findBy(final UserOID userOid,
								final AppCode appCode,final AppComponent appComp) {
		FindResult<A> findResult = this.getServiceProxyAs(FindServicesForUserAuthorizationOnResource.class)
									   .findBy(this.getSecurityContext(),
											   userOid,
											   appCode,appComp);
		Collection<A> result = findResult.getOrThrow();
		if (result!=null) {
			for (A auth : result) {
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
	@SuppressWarnings("unchecked")
	public Collection<A> findBy(final AuthorizationTargetResourceOID resOid) {
		FindResult<A> findResult = this.getServiceProxyAs(FindServicesForUserAuthorizationOnResource.class)
									   .findBy(this.getSecurityContext(),
											   resOid);
		return findResult.getOrThrow();
	}
	/**
	 * Returns authorizations for the given user at the given resource
	 * @param userOid
	 * @param resOid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Collection<A> findBy(final UserOID userOid,final AuthorizationTargetResourceOID resOid) {
		FindResult<A> findResult = this.getServiceProxyAs(FindServicesForUserAuthorizationOnResource.class)
									   .findBy(this.getSecurityContext(),
											   userOid,resOid);
		return findResult.getOrThrow();
	}
}
