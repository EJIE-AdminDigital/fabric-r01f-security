package r01f.client.api.sub.security.principal;

import jakarta.inject.Provider;
import r01f.api.interfaces.security.principal.CRUDServicesForPrincipal;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityIDs.SecurityPrincipalID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.api.delegates.ClientAPIDelegateForModelObjectCRUDServices;

public class ClientAPIDelegateForPrincipalCRUDServices<P extends IsSecurityPrincipal>
	 extends ClientAPIDelegateForModelObjectCRUDServices<SecurityPrincipalOID,P> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForPrincipalCRUDServices(final Provider<SecurityContext>  securityContextProvider,
													 final Marshaller marshaller,
													 final CRUDServicesForPrincipal<P> crudServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  crudServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Loads a principal using it's {@link LoginID}
	 * @param securityProviderId
	 * @param principalId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public P loadBy(final IdentityProvider securityProviderId,final SecurityPrincipalID principalId) {
		CRUDResult<P> opResult = this.getServiceProxyAs(CRUDServicesForPrincipal.class)
									 .loadBy(this.getSecurityContext(),
											 securityProviderId,principalId);
		return ClientAPIDelegateForModelObjectCRUDServices.objFormLoadResult(opResult);
	}
	/**
	 * Loads a principal using it's {@link LoginID}
	 * @param securityProviderId
	 * @param principalId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public P loadByOrNull(final IdentityProvider securityProviderId,final SecurityPrincipalID securityPrincipalId) {
		CRUDResult<P> opResult = this.getServiceProxyAs(CRUDServicesForPrincipal.class)
									 .loadBy(this.getSecurityContext(),
											 securityProviderId,securityPrincipalId);
		return ClientAPIDelegateForModelObjectCRUDServices.objOrNullFormLoadResult(opResult);
	}
}
