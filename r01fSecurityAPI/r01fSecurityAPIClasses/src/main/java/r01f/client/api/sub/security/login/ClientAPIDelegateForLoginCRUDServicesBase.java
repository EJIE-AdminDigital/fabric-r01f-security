package r01f.client.api.sub.security.login;

import jakarta.inject.Provider;

import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.login.Login;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.api.delegates.ClientAPIDelegateForModelObjectCRUDServices;
import r01f.services.api.delegates.ClientAPIModelObjectChangesTrack;

public abstract class ClientAPIDelegateForLoginCRUDServicesBase<L extends Login>
	 		  extends ClientAPIDelegateForModelObjectCRUDServices<LoginOID,L> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForLoginCRUDServicesBase(final Provider<SecurityContext>  securityContextProvider,
													 final Marshaller marshaller,
													 final CRUDServicesForLogin<L> crudServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  crudServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns a login from the given user
	 * @param oid the login
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public L loadBy(final UserOID oid) {
		CRUDResult<L> opResult = this.getServiceProxyAs(CRUDServicesForLogin.class)
									 .loadBy(this.getSecurityContext(),
											 oid);
		L outLogin = opResult.getOrThrow();
		ClientAPIModelObjectChangesTrack.startTrackingChangesOnLoaded(outLogin);
		return outLogin;
	}
	/**
	 * Returns a login from the given user
	 * @param oid the login
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public L loadByOrNull(final UserOID oid) {
		L outLogin = null;
		CRUDResult<L> opResult = this.getServiceProxyAs(CRUDServicesForLogin.class)
									 .loadBy(this.getSecurityContext(),
											 oid);
		if (opResult.hasSucceeded()) {
			outLogin = opResult.getOrThrow();
			ClientAPIModelObjectChangesTrack.startTrackingChangesOnLoaded(outLogin);
		} else if (opResult.hasFailed()
				&& opResult.asCRUDError()
						   .wasBecauseClientRequestedEntityWasNOTFound()) {
			// do nothing
		} else {
			opResult.asCRUDError().throwAsPersistenceException();
		}
		return outLogin;
	}
	/**
	 * Returns a login from the given id
	 * @param id the login
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public L loadBy(final LoginID id) {
		CRUDResult<L> opResult = this.getServiceProxyAs(CRUDServicesForLogin.class)
									 .loadBy(this.getSecurityContext(),
											 id);
		L outLogin = opResult.getOrThrow();
		ClientAPIModelObjectChangesTrack.startTrackingChangesOnLoaded(outLogin);
		return outLogin;
	}
	/**
	 * Returns a login from the given id
	 * @param id the login
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public L loadByOrNull(final LoginID id) {
		L outLogin = null;
		CRUDResult<L> opResult = this.getServiceProxyAs(CRUDServicesForLogin.class)
									 .loadBy(this.getSecurityContext(),
											 id);
		if (opResult.hasSucceeded()) {
			outLogin = opResult.getOrThrow();
			ClientAPIModelObjectChangesTrack.startTrackingChangesOnLoaded(outLogin);
		} else if (opResult.hasFailed()
				&& opResult.asCRUDError()
						   .wasBecauseClientRequestedEntityWasNOTFound()) {
			// do nothing
		} else {
			opResult.asCRUDError().throwAsPersistenceException();
		}
		return outLogin;
	}
}
