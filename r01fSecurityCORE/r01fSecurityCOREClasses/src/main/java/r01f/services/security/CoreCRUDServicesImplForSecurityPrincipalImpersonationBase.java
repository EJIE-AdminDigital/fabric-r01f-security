package r01f.services.security;


import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.principal.impersonation.CRUDServicesForPrincipalImpersonation;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.impersonation.SecurityPrincipalImpersonation;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityOIDs.SecurityPrincipalImpersonationOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreCRUDServicesForModelObjectBase;

@Accessors(prefix="_")
public abstract class CoreCRUDServicesImplForSecurityPrincipalImpersonationBase
			  extends CoreCRUDServicesForModelObjectBase<SecurityPrincipalImpersonationOID,SecurityPrincipalImpersonation>
		   implements CRUDServicesForPrincipalImpersonation,
					  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreCRUDServicesImplForSecurityPrincipalImpersonationBase(final ServicesCoreModuleConfig coreCfg,
																	 final Marshaller modelObjectsMarshaller,
																	 final EventBus eventBus,
																	 final Provider<EntityManager> entityManagerProvider) {
		super(coreCfg,
			  modelObjectsMarshaller,
			  eventBus,
			  entityManagerProvider);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Transactional
	@Override
	public CRUDResult<SecurityPrincipalImpersonation> loadActiveForImpersonatedPrincipal(final SecurityContext securityContext,
												 	   						 			 final SecurityPrincipalOID impersonatedPrincipalOid) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(CRUDServicesForPrincipalImpersonation.class)
				   .loadActiveForImpersonatedPrincipal(securityContext,
						   							   impersonatedPrincipalOid);
	}
	@Transactional
	@Override
	public CRUDResult<SecurityPrincipalImpersonation> loadActiveForImpersonatingPrincipal(final SecurityContext securityContext,
												 	   						  			  final SecurityPrincipalOID impersonatingPrincipalOid) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(CRUDServicesForPrincipalImpersonation.class)
				   .loadActiveForImpersonatingPrincipal(securityContext,
						   								impersonatingPrincipalOid);
	}
}