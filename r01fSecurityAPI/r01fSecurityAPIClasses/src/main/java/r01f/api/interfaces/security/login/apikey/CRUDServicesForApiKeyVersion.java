package r01f.api.interfaces.security.login.apikey;

import r01f.api.interfaces.security.SecurityServiceInterface;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.login.apikey.ApiKeyVersion;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.ApiKeyVersionOID;
import r01f.services.interfaces.CRUDServicesForModelObject;
import r01f.services.interfaces.ExposedServiceInterface;
import r01f.types.AppVersion;

@ExposedServiceInterface
public interface CRUDServicesForApiKeyVersion
         extends CRUDServicesForModelObject<ApiKeyVersionOID,ApiKeyVersion>,
         		 SecurityServiceInterface {
	/**
	 * Loads the current version
	 * @param securityContext
	 * @return
	 */
	public CRUDResult<ApiKeyVersion> loadCurrent(final SecurityContext securityContext);
	/**
	 * Loads the given version
	 * @param securityContext
	 * @param versionId
	 * @return
	 */
	public CRUDResult<ApiKeyVersion> loadById(final SecurityContext securityContext,
										  	  final AppVersion versionId);
}