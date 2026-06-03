package r01f.api.interfaces.security.login.apikey;

import r01f.api.interfaces.security.SecurityServiceInterface;
import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.login.apikey.ApiKeyLogin;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityToken;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.interfaces.ExposedServiceInterface;
import r01f.types.AppVersion;

@ExposedServiceInterface
public interface CRUDServicesForApiKeyLogin<L extends ApiKeyLogin>
         extends CRUDServicesForLogin<L>,
         		 SecurityServiceInterface {
/////////////////////////////////////////////////////////////////////////////////////////
//	LOAD
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Loads the [api key] by it's [public key]
	 * (BEWARE that the [private key] is NEVER stored)
	 * @param securityContext
	 * @param publicKey
	 * @return
	 */
	public CRUDResult<L> loadByPublicKey(final SecurityContext securityContext,
										 final SecurityToken publicKey);
	/**
	 * Loads the usable api key for the given loginId at the given version
	 * Note that ONLY a single api key is usable by a loginId at a version
	 * @param securityContext
	 * @param ownerOid
	 * @param version
	 * @return
	 */
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
							  	final LoginID loginId,final AppVersion version);
	/**
	 * Loads the usable api key for the given owner at the given version
	 * Note that ONLY a single api key is usable by an owner at a version
	 * @param securityContext
	 * @param ownerOid
	 * @param version
	 * @return
	 */
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
							  	final UserOID ownerOid,
							  	final AppVersion version);
/////////////////////////////////////////////////////////////////////////////////////////
//	CREATE / UPDATE / DELETE
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Issues a new api key for the given owner
	 * ... if there exists another usable api key, it's invalidated
	 * @param securityContext
	 * @param ownerOid
	 * @param version
	 * @param readOnly
	 * @return
	 */
	public CRUDResult<L> issue(final SecurityContext securityContext,
							   final UserOID ownerOid,
							   final AppVersion version,
							   final boolean readOnly);
	/**
	 * Issues a new api key for the given owner
	 * ... if there exists another usable api key, it's invalidated
	 * @param securityContext
	 * @param ownerOid
	 * @param readOnly
	 * @return
	 */
	public CRUDResult<L> issueForLastVersion(final SecurityContext securityContext,
											 final UserOID ownerOid,
											 final boolean readOnly);
	/**
	 * Invalidates an api key: sets the usage range upper bound to the current date
	 * @param securityContext
	 * @param apiKeyOid
	 * @return
	 */
	public CRUDResult<L> invalidate(final SecurityContext securityContext,
									final LoginOID apiKeyOid);
	
}