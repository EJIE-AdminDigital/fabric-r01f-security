package r01f.services.security;

import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import r01f.api.interfaces.security.principal.impersonation.SecurityPrincipalImpersonationServices;
import r01f.eventbus.EventBus;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.impersonation.SecurityPrincipalImpersonation;
import r01f.model.services.COREServiceMethodExecResult;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CorePersistenceServicesBase;

public abstract class CoreSecurityPrincipalImpersonationServicesImplBase 
			  extends CorePersistenceServicesBase
		   implements SecurityPrincipalImpersonationServices,
		   			  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreSecurityPrincipalImpersonationServicesImplBase(final ServicesCoreModuleConfig coreCfg,
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
	public COREServiceMethodExecResult<SecurityPrincipalImpersonation> theSecurityContextPrincipalSTARTSImpersonating(final SecurityContext securityContext,
											   								  										  final SecurityPrincipalOID impersonatedPrincipalOid) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(SecurityPrincipalImpersonationServices.class)
				   .theSecurityContextPrincipalSTARTSImpersonating(securityContext,
						   			   							 impersonatedPrincipalOid);
	}
	@Override
	public COREServiceMethodExecResult<SecurityPrincipalImpersonation> theSecurityContextPrincipalFINISHImpersonation(final SecurityContext securityContext) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(SecurityPrincipalImpersonationServices.class)
				   .theSecurityContextPrincipalFINISHImpersonation(securityContext);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Transactional
	@Override
	public COREServiceMethodExecResult<SecurityPrincipalImpersonation> loadActiveImpersonationForImpersonatedPrincipal(final SecurityContext securityContext,
																	     			  								   final SecurityPrincipalOID impersonatedPrincipalOid) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(SecurityPrincipalImpersonationServices.class)
				   .loadActiveImpersonationForImpersonatedPrincipal(securityContext,
						   					   impersonatedPrincipalOid);
	}
	@Transactional
	@Override
	public COREServiceMethodExecResult<SecurityPrincipalImpersonation> endCurrentlyActiveImpersonationForImpersonatedPrincipal(final SecurityContext securityContext,
																							  					   			   final SecurityPrincipalOID impersonatedPrincipalOid) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(SecurityPrincipalImpersonationServices.class)
				   .endCurrentlyActiveImpersonationForImpersonatedPrincipal(securityContext,
						   													impersonatedPrincipalOid);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Transactional
	@Override
	public COREServiceMethodExecResult<SecurityPrincipalImpersonation> loadActiveImpersonationForImpersonatingPrincipal(final SecurityContext securityContext,
																					  									final SecurityPrincipalOID impersonatingPrincipalOid) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(SecurityPrincipalImpersonationServices.class)
				   .loadActiveImpersonationForImpersonatingPrincipal(securityContext,
						   					   impersonatingPrincipalOid);
	}
	@Transactional
	@Override
	public COREServiceMethodExecResult<SecurityPrincipalImpersonation> endCurrentlyActiveImpersonationForImpersonatingPrincipal(final SecurityContext securityContext,
																							  									final SecurityPrincipalOID impersonatingPrincipalOid) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(SecurityPrincipalImpersonationServices.class)
				   .endCurrentlyActiveImpersonationForImpersonatingPrincipal(securityContext,
						   					   							 	 impersonatingPrincipalOid);
	}
}
