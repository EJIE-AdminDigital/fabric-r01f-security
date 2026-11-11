package r01f.services.security;


import java.time.Instant;

import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.principal.impersonation.FindServicesForPrincipalImpersonation;
import r01f.eventbus.EventBus;
import r01f.model.persistence.FindResult;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.impersonation.SecurityPrincipalImpersonation;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityOIDs.SecurityPrincipalImpersonationOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreFindServicesForModelObjectBase;
import r01f.types.Range;

@Accessors(prefix="_")
public abstract class CoreFindServicesImplForSecurityPrincipalImpersonationBase
			  extends CoreFindServicesForModelObjectBase<SecurityPrincipalImpersonationOID,SecurityPrincipalImpersonation>
		   implements FindServicesForPrincipalImpersonation,
					  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreFindServicesImplForSecurityPrincipalImpersonationBase(final ServicesCoreModuleConfig coreCfg,
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
	public FindResult<SecurityPrincipalImpersonation> findForImpersonatedPrincipal(final SecurityContext securityContext,
															 	   				   final SecurityPrincipalOID impersonatedPrincipalOid,
															 	   				   final Range<Instant> dateRange) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(FindServicesForPrincipalImpersonation.class)
				   .findForImpersonatedPrincipal(securityContext,
						   						 impersonatedPrincipalOid,
						   						 dateRange);
	}
	@Transactional
	@Override
	public FindResult<SecurityPrincipalImpersonation> findForImpersonatingPrincipal(final SecurityContext securityContext,
															 	   					final SecurityPrincipalOID impersonatingPrincipalOid,
															 	   					final Range<Instant> dateRange) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(FindServicesForPrincipalImpersonation.class)
				   .findForImpersonatingPrincipal(securityContext,
						   						  impersonatingPrincipalOid,
						   						  dateRange);
	}
}