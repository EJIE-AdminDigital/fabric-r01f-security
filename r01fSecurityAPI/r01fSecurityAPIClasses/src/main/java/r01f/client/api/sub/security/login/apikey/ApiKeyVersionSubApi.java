package r01f.client.api.sub.security.login.apikey;

import jakarta.inject.Provider;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.login.apikey.CRUDServicesForApiKeyVersion;
import r01f.api.interfaces.security.login.apikey.FindServicesForApiKeyVersion;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.PersistenceException;
import r01f.model.security.login.apikey.ApiKeyVersion;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.ApiKeyVersionOID;
import r01f.services.api.ClientSubAPIBase;
import r01f.services.api.delegates.ClientAPIDelegateForModelObjectCRUDServices;
import r01f.services.api.delegates.ClientAPIDelegateForModelObjectFindServices;
import r01f.types.AppVersion;

@Accessors(prefix="_")
public class ApiKeyVersionSubApi
	 extends ClientSubAPIBase {		// a sub API uses the proxy to access the services layer
/////////////////////////////////////////////////////////////////////////////////////////
//	DELEGATE APIS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final ClientAPIDelegateForApiKeyVersionCRUDServices _forCrud;
	@Getter private final ClientAPIDelegateForApiKeyVersionFindServices _forFind;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ApiKeyVersionSubApi(final Provider<SecurityContext>  securityContextProvider,
							   final Marshaller modelObjectsMarshaller,
							   final CRUDServicesForApiKeyVersion crudServices,
							   final FindServicesForApiKeyVersion findServices)  {

		super(securityContextProvider,
		      modelObjectsMarshaller,
		      null);	// no reference to other client apis


		_forCrud = new ClientAPIDelegateForApiKeyVersionCRUDServices(securityContextProvider,
																	 modelObjectsMarshaller,
																	 crudServices);
		_forFind = new ClientAPIDelegateForApiKeyVersionFindServices(securityContextProvider,
																	 modelObjectsMarshaller,
																	 findServices);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  CRUD
/////////////////////////////////////////////////////////////////////////////////////////
	public static class ClientAPIDelegateForApiKeyVersionCRUDServices
		 		extends ClientAPIDelegateForModelObjectCRUDServices<ApiKeyVersionOID,ApiKeyVersion> {

		public ClientAPIDelegateForApiKeyVersionCRUDServices(final Provider<SecurityContext> securityContextProvider,
															 final Marshaller modelObjectsMarshaller,
															 final CRUDServicesForApiKeyVersion crudServicesProxy) {
			super(securityContextProvider,
				  modelObjectsMarshaller,
				  crudServicesProxy);
		}
		/**
		 * Loads the current version
		 * @return
		 */
		public ApiKeyVersion loadCurrent() {
			CRUDResult<ApiKeyVersion> versionLoadResult = this.getServiceProxyAs(CRUDServicesForApiKeyVersion.class)
																  .loadCurrent(this.getSecurityContext());
			return versionLoadResult.getOrThrow();
		}
		/**
		 * Loads the given version
		 * @param id
		 * @return
		 */
		public ApiKeyVersion load(final AppVersion id) {
			CRUDResult<ApiKeyVersion> versionLoadResult = this.getServiceProxyAs(CRUDServicesForApiKeyVersion.class)
																  .loadById(this.getSecurityContext(),
																		    id);
			return versionLoadResult.getOrThrow();
		}
		/**
		 * Loads the given version
		 * @param id
		 * @return
		 */
		public ApiKeyVersion loadOrNull(final AppVersion id) {
			ApiKeyVersion outVersion = null;
			try {
				outVersion = this.load(id);
			} catch (PersistenceException pEx) {
				if (pEx.isEntityNotFound()) {
					outVersion = null;
				} else {
					throw pEx;
				}
			}
			return outVersion;
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  FIND
/////////////////////////////////////////////////////////////////////////////////////////
	public static class ClientAPIDelegateForApiKeyVersionFindServices
		 		extends ClientAPIDelegateForModelObjectFindServices<ApiKeyVersionOID,ApiKeyVersion> {

		public ClientAPIDelegateForApiKeyVersionFindServices(final Provider<SecurityContext> securityContextProvider,
													    	 final Marshaller modelObjectsMarshaller,
													    	 final FindServicesForApiKeyVersion findServicesProxy) {
			super(securityContextProvider,
				  modelObjectsMarshaller,
				  findServicesProxy);
		}
	}
}
