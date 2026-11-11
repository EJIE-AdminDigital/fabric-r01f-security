package r01f.services.delegates.security;

import r01f.api.interfaces.security.principal.FindServicesForPrincipal;
import r01f.eventbus.EventBus;
import r01f.model.persistence.FindResult;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.FindServicesForModelObjectDelegateBase;

/**
 * Service layer delegated type for CRUD (Create/Read/Update/Delete) operations
 */
public abstract class FindServicesDelegateForSecurityPrincipalBase<P extends IsSecurityPrincipal>
			  extends FindServicesForModelObjectDelegateBase<SecurityPrincipalOID,P>
		   implements FindServicesForPrincipal<P> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR   & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public FindServicesDelegateForSecurityPrincipalBase(final ServicesCoreModuleConfig coreCfg,
												   		final Class<P> modelObjectType,
												   		final FindServicesForPrincipal<P> dbFind,
												   		final Marshaller marshaller,
												   		final EventBus eventBus) {
		super(coreCfg,
			  modelObjectType,
			  dbFind,
			  eventBus);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  EXTENSION METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public FindResult<P> findAllPrincipals(final SecurityContext securityContext) {
		return this.getServiceImplAs(FindServicesForPrincipal.class)
					.findAllPrincipals(securityContext);
	}
	@Override @SuppressWarnings("unchecked")
	public FindResult<P> findHavingAnySystemWideRole(final SecurityContext securityContext) {
		return this.getServiceImplAs(FindServicesForPrincipal.class)
					.findHavingAnySystemWideRole(securityContext);
	}
	@Override @SuppressWarnings("unchecked")
	public FindResult<P> findByText(final SecurityContext securityContext,
									final String text) {
		return this.getServiceImplAs(FindServicesForPrincipal.class)
				   .findByText(securityContext,
						   	   text);
	}
}
