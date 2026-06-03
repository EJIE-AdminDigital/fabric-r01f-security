package r01f.api.interfaces.security.login.apikey;

import r01f.api.interfaces.security.SecurityServiceInterface;
import r01f.model.security.login.apikey.ApiKeyVersion;
import r01f.securitycontext.SecurityOIDs.ApiKeyVersionOID;
import r01f.services.interfaces.ExposedServiceInterface;
import r01f.services.interfaces.FindServicesForModelObject;

@ExposedServiceInterface
public interface FindServicesForApiKeyVersion
         extends FindServicesForModelObject<ApiKeyVersionOID,ApiKeyVersion>,
         		 SecurityServiceInterface {
	// nothing
}