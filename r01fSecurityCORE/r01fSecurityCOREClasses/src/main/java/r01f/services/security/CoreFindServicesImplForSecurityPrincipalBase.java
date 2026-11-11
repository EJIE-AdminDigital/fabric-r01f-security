package r01f.services.security;

import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.principal.FindServicesForPrincipal;
import r01f.eventbus.EventBus;
import r01f.model.persistence.FindResult;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreFindServicesForModelObjectBase;

@Accessors(prefix="_")
public abstract class CoreFindServicesImplForSecurityPrincipalBase<P extends IsSecurityPrincipal>
			  extends CoreFindServicesForModelObjectBase<SecurityPrincipalOID,P>
		   implements FindServicesForPrincipal<P>,
			 		  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreFindServicesImplForSecurityPrincipalBase(final ServicesCoreModuleConfig coreCfg,
													    final Marshaller modelObjectsMarshaller,
													    final EventBus eventBus,
													    final Provider<EntityManager> entityManagerProvider) {
		super(coreCfg,
			  modelObjectsMarshaller,
			  eventBus,
			  entityManagerProvider);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SERVICES EXTENSION
// 	IMPORTANT!!! Do NOT put any logic in these methods ONLY DELEGATE!!!
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public FindResult<P> findAllPrincipals(final SecurityContext securityContext) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(FindServicesForPrincipal.class)
				   .findAllPrincipals(securityContext);
	}
	@Override @SuppressWarnings("unchecked")
	public FindResult<P> findHavingAnySystemWideRole(final SecurityContext securityContext) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(FindServicesForPrincipal.class)
				   .findHavingAnySystemWideRole(securityContext);
	}
	@Override @SuppressWarnings("unchecked")
	public FindResult<P> findByText(final SecurityContext securityContext,
									final String text) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(FindServicesForPrincipal.class)
				   .findByText(securityContext,
						   	   text);
	}
}
