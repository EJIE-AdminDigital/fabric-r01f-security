package r01f.services.security;


import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.principal.CRUDServicesForPrincipal;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.SecurityPrincipalID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreCRUDServicesForModelObjectBase;
import r01f.types.contact.EMail;

@Accessors(prefix="_")
public abstract class CoreCRUDServicesImplForSecurityPrincipalBase<P extends IsSecurityPrincipal>
			  extends CoreCRUDServicesForModelObjectBase<SecurityPrincipalOID,P>
		   implements CRUDServicesForPrincipal<P>,
					  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreCRUDServicesImplForSecurityPrincipalBase(final ServicesCoreModuleConfig coreCfg,
												   		final Marshaller modelObjectsMarshaller,
												   		final EventBus eventBus,
												   		final Provider<EntityManager> entityManagerProvider) {
		super(coreCfg,
			  modelObjectsMarshaller,
			  eventBus,
			  entityManagerProvider);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EXTENSION  METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Transactional
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<P> loadBy(final SecurityContext securityContext,
								final IdentityProvider securityProvider,final SecurityPrincipalID principalId) {
		CRUDResult<P> outResult = this.forSecurityContext(securityContext)
									  .createDelegateAs(CRUDServicesForPrincipal.class)
									  .loadBy(securityContext,
											  securityProvider,principalId);
		return outResult;
	}
	@Transactional
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<P> loadBy(final SecurityContext securityContext,
								final EMail email) {
		CRUDResult<P> outResult = this.forSecurityContext(securityContext)
									  .createDelegateAs(CRUDServicesForPrincipal.class)
									  .loadBy(securityContext,
											  email);
		return outResult;
	}
}
