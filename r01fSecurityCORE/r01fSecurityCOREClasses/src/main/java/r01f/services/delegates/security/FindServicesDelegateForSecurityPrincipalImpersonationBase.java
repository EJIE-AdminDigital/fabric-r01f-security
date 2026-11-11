package r01f.services.delegates.security;



import java.time.Instant;

import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.principal.impersonation.FindServicesForPrincipalImpersonation;
import r01f.eventbus.EventBus;
import r01f.model.persistence.FindResult;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.impersonation.SecurityPrincipalImpersonation;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityOIDs.SecurityPrincipalImpersonationOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.FindServicesForModelObjectDelegateBase;
import r01f.types.Range;

@Slf4j
public abstract class FindServicesDelegateForSecurityPrincipalImpersonationBase
			  extends FindServicesForModelObjectDelegateBase<SecurityPrincipalImpersonationOID,SecurityPrincipalImpersonation>
		   implements FindServicesForPrincipalImpersonation {

/////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTOR   & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public FindServicesDelegateForSecurityPrincipalImpersonationBase(final ServicesCoreModuleConfig coreCfg,
																	 final FindServicesForPrincipalImpersonation dbFind,
																	 final Marshaller marshaller,
																	 final EventBus eventBus) {
		super(coreCfg,
			  SecurityPrincipalImpersonation.class,
			  dbFind,
			  eventBus);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public FindResult<SecurityPrincipalImpersonation> findForImpersonatedPrincipal(final SecurityContext securityContext,
															 	   				   final SecurityPrincipalOID impersonatedPrincipalOid,
															 	   				   final Range<Instant> dateRange) {
		if (impersonatedPrincipalOid == null) throw new IllegalArgumentException("The impersonated principal OID is mandatory");
		if (dateRange == null) throw new IllegalArgumentException("The date range is mandatory");
		return this.getServiceImplAs(FindServicesForPrincipalImpersonation.class)
				   .findForImpersonatedPrincipal(securityContext,
				   								 impersonatedPrincipalOid,
				   								 dateRange);
	}
	@Override
	public FindResult<SecurityPrincipalImpersonation> findForImpersonatingPrincipal(final SecurityContext securityContext,
															 	   					final SecurityPrincipalOID impersonatingPrincipalOid,
															 	   					final Range<Instant> dateRange) {
		if (impersonatingPrincipalOid == null) throw new IllegalArgumentException("The impersonating principal OID is mandatory");
		if (dateRange == null) throw new IllegalArgumentException("The date range is mandatory");
		return this.getServiceImplAs(FindServicesForPrincipalImpersonation.class)
				   .findForImpersonatingPrincipal(securityContext,
				   								  impersonatingPrincipalOid,
				   								  dateRange);
	}
}
