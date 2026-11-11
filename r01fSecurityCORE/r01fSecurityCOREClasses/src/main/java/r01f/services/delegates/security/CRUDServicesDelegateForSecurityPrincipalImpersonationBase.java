package r01f.services.delegates.security;



import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.principal.impersonation.CRUDServicesForPrincipalImpersonation;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.impersonation.SecurityPrincipalImpersonation;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityOIDs.SecurityPrincipalImpersonationOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.CRUDServicesForModelObjectDelegateBase;

@Slf4j
public abstract class CRUDServicesDelegateForSecurityPrincipalImpersonationBase
			  extends CRUDServicesForModelObjectDelegateBase<SecurityPrincipalImpersonationOID,SecurityPrincipalImpersonation>
		   implements CRUDServicesForPrincipalImpersonation {

/////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTOR   & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public CRUDServicesDelegateForSecurityPrincipalImpersonationBase(final ServicesCoreModuleConfig coreCfg,
																	 final CRUDServicesForPrincipalImpersonation dbCrud,
																	 final Marshaller marshaller,
																	 final EventBus eventBus) {
		super(coreCfg,
			  SecurityPrincipalImpersonation.class,
			  dbCrud,
			  eventBus);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CRUDResult<SecurityPrincipalImpersonation> loadActiveForImpersonatedPrincipal(final SecurityContext securityContext,
												 	   						 			 final SecurityPrincipalOID impersonatedPrincipalOid) {
		if (impersonatedPrincipalOid == null) throw new IllegalArgumentException("The impersonated principal OID is mandatory");
		return this.getServiceImplAs(CRUDServicesForPrincipalImpersonation.class)
				   .loadActiveForImpersonatedPrincipal(securityContext,
				   									   impersonatedPrincipalOid);
	}
	@Override
	public CRUDResult<SecurityPrincipalImpersonation> loadActiveForImpersonatingPrincipal(final SecurityContext securityContext,
												 	   						  			  final SecurityPrincipalOID impersonatingPrincipalOid) {
		if (impersonatingPrincipalOid == null) throw new IllegalArgumentException("The impersonating principal OID is mandatory");
		return this.getServiceImplAs(CRUDServicesForPrincipalImpersonation.class)
				   .loadActiveForImpersonatingPrincipal(securityContext,
				   									    impersonatingPrincipalOid);
	}
}
