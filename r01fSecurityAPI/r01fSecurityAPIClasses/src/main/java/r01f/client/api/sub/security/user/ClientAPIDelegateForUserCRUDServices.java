package r01f.client.api.sub.security.user;

import jakarta.inject.Provider;

import r01f.api.interfaces.security.user.CRUDServicesForUser;
import r01f.aspects.interfaces.dirtytrack.DirtyStateTrackable;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.PersistenceException;
import r01f.model.security.user.User;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.api.delegates.ClientAPIDelegateForModelObjectCRUDServices;
import r01f.services.api.delegates.ClientAPIModelObjectChangesTrack;
import r01f.types.contact.EMail;
import r01f.types.contact.Phone;

public class ClientAPIDelegateForUserCRUDServices<U extends User>
	 extends ClientAPIDelegateForModelObjectCRUDServices<UserOID,U> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForUserCRUDServices(final Provider<SecurityContext>  securityContextProvider,
												final Marshaller marshaller,
												final CRUDServicesForUser<U> crudServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  crudServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Loads a user using it's {@link LoginID}
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public U loadBy(final SecurityProviderID securityProviderId,final LoginID loginId) {
		CRUDResult<U> opResult = this.getServiceProxyAs(CRUDServicesForUser.class)
									 .loadBy(this.getSecurityContext(),
											 securityProviderId,loginId);
		U outEntity = opResult.getOrThrow();
		if (outEntity instanceof DirtyStateTrackable) {
			ClientAPIModelObjectChangesTrack.startTrackingChangesOnLoaded(outEntity);
		}
		return outEntity;
	}
	/**
	 * Loads a user using it's {@link LoginID}
	 * (returns null if the user does not exists)
	 * @param id
	 * @return
	 */
	public U loadByOrNull(final SecurityProviderID securityProviderId,final LoginID loginId) {
		U outUser = null;
		try {
			outUser = this.loadBy(securityProviderId,loginId);
			if (outUser instanceof DirtyStateTrackable) {
				ClientAPIModelObjectChangesTrack.startTrackingChangesOnLoaded(outUser);
			}
		} catch (PersistenceException pEx) {
			if (pEx.isEntityNotFound()) {
				outUser = null;
			} else {
				throw pEx;
			}
		}
		return outUser;
	}
	/**
	 * Loads the owner with the given email (should exist a single owner with the given mail)
	 * @param email
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public U loadBy(final EMail email) {
		CRUDResult<U> opResult = this.getServiceProxyAs(CRUDServicesForUser.class)
									 .loadBy(this.getSecurityContext(),
											 email);
		U outEntity = opResult.getOrThrow();
		if (outEntity instanceof DirtyStateTrackable) {
			ClientAPIModelObjectChangesTrack.startTrackingChangesOnLoaded(outEntity);
		}
		return outEntity;
	}
	/**
	 * Loads the owner with the given email (should exist a single owner with the given mail)
	 * @param email
	 * @return
	 */
	public U loadByOrNull(final EMail email) {
		U outUser = null;
		try {
			outUser = this.loadBy(email);
			if (outUser instanceof DirtyStateTrackable) {
				ClientAPIModelObjectChangesTrack.startTrackingChangesOnLoaded(outUser);
			}
		} catch (PersistenceException pEx) {
			if (pEx.isEntityNotFound()) {
				outUser = null;
			} else {
				throw pEx;
			}
		}
		return outUser;
	}
	/**
	 * Loads the owner with the given phone (should exist a single owner with the given phone)
	 * @param phone
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public U loadBy(final Phone phone) {
		CRUDResult<U> opResult = this.getServiceProxyAs(CRUDServicesForUser.class)
				 .loadBy(this.getSecurityContext(),
						 phone);
		U outEntity = opResult.getOrThrow();
		if (outEntity instanceof DirtyStateTrackable) {
			ClientAPIModelObjectChangesTrack.startTrackingChangesOnLoaded(outEntity);
		}
		return outEntity;
	}
	/**
	 * Loads the owner with the given email (should exist a single owner with the given mail)
	 * @param phone
	 * @return
	 */
	public U loadOrNull(final Phone phone) {
		U outUser = null;
		try {
			outUser = this.loadBy(phone);
			if (outUser instanceof DirtyStateTrackable) {
				ClientAPIModelObjectChangesTrack.startTrackingChangesOnLoaded(outUser);
			}
		} catch (PersistenceException pEx) {
			if (pEx.isEntityNotFound()) {
				outUser = null;
			} else {
				throw pEx;
			}
		}
		return outUser;
	}
}
