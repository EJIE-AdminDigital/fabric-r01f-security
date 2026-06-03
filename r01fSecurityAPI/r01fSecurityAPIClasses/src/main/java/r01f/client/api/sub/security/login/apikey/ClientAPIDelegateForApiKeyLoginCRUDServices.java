package r01f.client.api.sub.security.login.apikey;

import jakarta.inject.Provider;

import r01f.api.interfaces.security.login.apikey.CRUDServicesForApiKeyLogin;
import r01f.client.api.sub.security.login.ClientAPIDelegateForLoginCRUDServicesBase;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.login.apikey.ApiKeyLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.SecurityToken;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.api.delegates.ClientAPIModelObjectChangesTrack;
import r01f.types.AppVersion;

public class ClientAPIDelegateForApiKeyLoginCRUDServices<L extends ApiKeyLogin>
	 extends ClientAPIDelegateForLoginCRUDServicesBase<L> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForApiKeyLoginCRUDServices(final Provider<SecurityContext>  securityContextProvider,
													   final Marshaller marshaller,
													   final CRUDServicesForApiKeyLogin<L> crudServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  crudServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Loads the [api key] by it's [public key]
	 * (BEWARE that the [private key] is NEVER stored)
	 * @param publicKey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public L loadByPublicKey(final SecurityToken publicKey) {
		CRUDResult<L> load = this.getServiceProxyAs(CRUDServicesForApiKeyLogin.class)
								 .loadByPublicKey(this.getSecurityContext(),
									     		  publicKey);
		L login = load.getOrThrow();
		ClientAPIModelObjectChangesTrack.startTrackingChangesOnLoaded(login);
		return login;
	}
	/**
	 * Loads the [api key] by it's [public key]; returns null if the [public key] does NOT exist
	 * (BEWARE that the [private key] is NEVER stored)
	 * @param publicKey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public L loadByPublicKeyOrNull(final SecurityToken publicKey) {
		CRUDResult<L> load = this.getServiceProxyAs(CRUDServicesForApiKeyLogin.class)
								 .loadByPublicKey(this.getSecurityContext(),
									     		  publicKey);
		L login = null;
		if (load.hasSucceeded()) {
			login = load.getOrThrow();
			ClientAPIModelObjectChangesTrack.startTrackingChangesOnLoaded(login);
		} else if (load.hasFailed()
				&& load.asCRUDError()
					   .wasBecauseClientRequestedEntityWasNOTFound()) {
			// not found
		} else {
			load.asCRUDError().throwAsPersistenceException();
		}
		return login;
	}
	/**
	 * Loads the usable api key for the given owner at the given version
	 * Note that ONLY a single api key is usable by an owner at a version
	 * @param ownerOid
	 * @param version
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public L load(final UserOID ownerOid,
				  final AppVersion version) {
		CRUDResult<L> load = this.getServiceProxyAs(CRUDServicesForApiKeyLogin.class)
								 .loadBy(this.getSecurityContext(),
									   ownerOid,version);
		L login = load.getOrThrow();
		ClientAPIModelObjectChangesTrack.startTrackingChangesOnLoaded(login);
		return login;
	}
	/**
	 * Loads the usable api key for the given owner at the given version; returns null if the [login] does NOT exists
	 * Note that ONLY a single api key is usable by an owner at a version
	 * @param ownerOid
	 * @param version
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public L loadOrNull(final UserOID ownerOid,
				  		final AppVersion version) {
		CRUDResult<L> load = this.getServiceProxyAs(CRUDServicesForApiKeyLogin.class)
								 .loadBy(this.getSecurityContext(),
									   ownerOid,version);
		L login = null;
		if (load.hasSucceeded()) {
			login = load.getOrThrow();
			ClientAPIModelObjectChangesTrack.startTrackingChangesOnLoaded(login);
		} else if (load.hasFailed()
				&& load.asCRUDError()
					   .wasBecauseClientRequestedEntityWasNOTFound()) {
			// not found
		} else {
			load.asCRUDError().throwAsPersistenceException();
		}
		return login;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Issues a new api key for the given owner
	 * ... if there exists another usable api key, it's invalidated
	 * @param ownerOid
	 * @param version
	 * @param readOnly
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public L issue(final UserOID ownerOid,
				   final AppVersion version,final boolean readOnly) {
		CRUDResult<L> create = this.getServiceProxyAs(CRUDServicesForApiKeyLogin.class)
								 .issue(this.getSecurityContext(),
									   ownerOid,version,readOnly);
		L login = create.getOrThrow();
		ClientAPIModelObjectChangesTrack.startTrackingChangesOnSaved(create);
		return login;
	}
	/**
	 * Issues a new api key for the given owner
	 * ... if there exists another usable api key, it's invalidated
	 * @param ownerOid
	 * @param readOnly
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public L issueForLastVersion(final UserOID ownerOid,
								 final boolean readOnly) {
		CRUDResult<L> load = this.getServiceProxyAs(CRUDServicesForApiKeyLogin.class)
								 .issueForLastVersion(this.getSecurityContext(),
										 			  ownerOid,readOnly);
		L login = load.getOrThrow();
		ClientAPIModelObjectChangesTrack.startTrackingChangesOnSaved(login);
		return login;
	}
	/**
	 * Issues a new api key for the given owner
	 * ... if there exists another usable api key, it's invalidated
	 * @param version
	 * @param apiKeyOwnerOid
	 * @return
	 */
	public L issueReadOnly(final AppVersion version,
						   final UserOID ownerOid) {
		return this.issue(ownerOid,
						  version,true);
	}
	/**
	 * Issues a new api key for the given owner
	 * ... if there exists another usable api key, it's invalidated
	 * @param version
	 * @param apiKeyOwnerOid
	 * @return
	 */
	public L issueForReadAndWrite(final AppVersion version,
								  final UserOID ownerOid) {
		return this.issue(ownerOid,
						  version,false);
	}
	/**
	 * Invalidates an api key: sets the usage range upper bound to the current date
	 * @param apiKeyOid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public L invalidate(final LoginOID apiKeyOid) {
		CRUDResult<L> load = this.getServiceProxyAs(CRUDServicesForApiKeyLogin.class)
								 .invalidate(this.getSecurityContext(),
										 	 apiKeyOid);
		L login = load.getOrThrow();
		ClientAPIModelObjectChangesTrack.startTrackingChangesOnSaved(login);
		return login;
	}
}
