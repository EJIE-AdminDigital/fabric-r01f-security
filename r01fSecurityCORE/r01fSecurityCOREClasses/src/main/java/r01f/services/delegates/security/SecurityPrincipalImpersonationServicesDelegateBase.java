package r01f.services.delegates.security;



import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.principal.impersonation.CRUDServicesForPrincipalImpersonation;
import r01f.api.interfaces.security.principal.impersonation.SecurityPrincipalImpersonationServices;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.impersonation.SecurityPrincipalImpersonation;
import r01f.model.services.COREServiceMethod;
import r01f.model.services.COREServiceMethodExecError;
import r01f.model.services.COREServiceMethodExecOK;
import r01f.model.services.COREServiceMethodExecResult;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityOIDs.SecurityPrincipalImpersonationOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.PersistenceServicesDelegateForModelObjectBase;

@Slf4j
public abstract class SecurityPrincipalImpersonationServicesDelegateBase
			  extends PersistenceServicesDelegateForModelObjectBase<SecurityPrincipalImpersonationOID,SecurityPrincipalImpersonation>
		   implements SecurityPrincipalImpersonationServices {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	private final CRUDServicesForPrincipalImpersonation _impersonationCrud;
/////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTOR   & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public SecurityPrincipalImpersonationServicesDelegateBase(final ServicesCoreModuleConfig coreCfg,
															  final Marshaller marshaller,
															  final EventBus eventBus,
															  // other services
															  final CRUDServicesForPrincipalImpersonation impersonationCrud) {
		super(coreCfg,
			  SecurityPrincipalImpersonation.class,
			  null,	// no service impl (all logic is contained in this delegate)
			  eventBus);
		_impersonationCrud = impersonationCrud;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public COREServiceMethodExecResult<SecurityPrincipalImpersonation> theSecurityContextPrincipalSTARTSImpersonating(final SecurityContext securityContext,
											   								  										  final SecurityPrincipalOID impersonatedPrincipalOid) {
		if (impersonatedPrincipalOid == null) throw new IllegalArgumentException("The impersonated principal OID is mandatory");
		if (securityContext.isBeingImpersonated()) throw new IllegalStateException("The current [security context] is being impersonated by another [principal]: an impersonated [principal] cannot impersonate another [principal]" +
																				   "(security context principal OID=" + securityContext.getSecurityPrincipalOid() + " / impersonating principal OID=" + securityContext.getImpersonatingSecurityPrincipalOid() + ")");
		
		// Create the [impersonation]
		SecurityPrincipalOID theImpersonatingPrincipalOid = securityContext.getSecurityPrincipalOid();
		SecurityPrincipalOID theImpersonatedPrincipalOid = impersonatedPrincipalOid;
		
		SecurityPrincipalImpersonation impersonation = new SecurityPrincipalImpersonation();
		
		impersonation.setImpersonatingPrincipalOid(theImpersonatingPrincipalOid);
		impersonation.setImpersonatedPrincipalOid(theImpersonatedPrincipalOid);
		
		impersonation.startImpersonation();
		
		CRUDResult<SecurityPrincipalImpersonation> impersonationCreate = _impersonationCrud.create(securityContext,
																								   impersonation);
		return impersonationCreate.hasSucceeded() ? new COREServiceMethodExecOK<>(COREServiceMethod.current(),
											 									  impersonationCreate.getOrThrow())
												  : new COREServiceMethodExecError<>(COREServiceMethod.current(),
														 						     impersonationCreate.asCRUDError());
	}
	@Override
	public COREServiceMethodExecResult<SecurityPrincipalImpersonation> theSecurityContextPrincipalFINISHImpersonation(final SecurityContext securityContext) {
		if (!securityContext.isBeingImpersonated()) throw new IllegalStateException("The current [security context] is NOT being impersonated");
		
		// Load the active impersonation for the current security context
		SecurityPrincipalOID theImpersonatingPrincipalOid = securityContext.getSecurityPrincipalOid();
		CRUDResult<SecurityPrincipalImpersonation> impersonationLoad = _impersonationCrud.loadActiveForImpersonatingPrincipal(securityContext,
																															  theImpersonatingPrincipalOid);
		return _endImpersonation(securityContext,
								 impersonationLoad);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public COREServiceMethodExecResult<SecurityPrincipalImpersonation> loadActiveImpersonationForImpersonatedPrincipal(final SecurityContext securityContext,
																	     			  								   final SecurityPrincipalOID impersonatedPrincipalOid) {
		if (impersonatedPrincipalOid == null) throw new IllegalArgumentException("The impersonated principal OID is mandatory");
		CRUDResult<SecurityPrincipalImpersonation> impersonationLoad = _impersonationCrud.loadActiveForImpersonatedPrincipal(securityContext,
																															 impersonatedPrincipalOid);
		return impersonationLoad.hasSucceeded() ? new COREServiceMethodExecOK<>(COREServiceMethod.current(),
												 									 impersonationLoad.getOrThrow())
												: new COREServiceMethodExecError<>(COREServiceMethod.current(),
														 						   impersonationLoad.asCRUDError());
	}
	@Override
	public COREServiceMethodExecResult<SecurityPrincipalImpersonation> endCurrentlyActiveImpersonationForImpersonatedPrincipal(final SecurityContext securityContext,
																							  					   			   final SecurityPrincipalOID impersonatedPrincipalOid) {
		if (impersonatedPrincipalOid == null) throw new IllegalArgumentException("The impersonated principal OID is mandatory");
		CRUDResult<SecurityPrincipalImpersonation> impersonationLoad = _impersonationCrud.loadActiveForImpersonatedPrincipal(securityContext,
																															 impersonatedPrincipalOid);
		return _endImpersonation(securityContext,
								 impersonationLoad);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public COREServiceMethodExecResult<SecurityPrincipalImpersonation> loadActiveImpersonationForImpersonatingPrincipal(final SecurityContext securityContext,
																					  									final SecurityPrincipalOID impersonatingPrincipalOid) {
		if (impersonatingPrincipalOid == null) throw new IllegalArgumentException("The impersonating principal OID is mandatory");
		CRUDResult<SecurityPrincipalImpersonation> impesonationLoad = _impersonationCrud.loadActiveForImpersonatingPrincipal(securityContext,
																																   impersonatingPrincipalOid);
		return impesonationLoad.hasSucceeded() ? new COREServiceMethodExecOK<>(COREServiceMethod.current(),
												 									 impesonationLoad.getOrThrow())
											   : new COREServiceMethodExecError<>(COREServiceMethod.current(),
														 						  impesonationLoad.asCRUDError());
	}
	@Override
	public COREServiceMethodExecResult<SecurityPrincipalImpersonation> endCurrentlyActiveImpersonationForImpersonatingPrincipal(final SecurityContext securityContext,
																							  								    final SecurityPrincipalOID impersonatingPrincipalOid) {
		if (impersonatingPrincipalOid == null) throw new IllegalArgumentException("The impersonating principal OID is mandatory");
		CRUDResult<SecurityPrincipalImpersonation> outActiveImpersonation = _impersonationCrud.loadActiveForImpersonatedPrincipal(securityContext,
																																  impersonatingPrincipalOid);
		return _endImpersonation(securityContext,
								 outActiveImpersonation);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	private final COREServiceMethodExecResult<SecurityPrincipalImpersonation> _endImpersonation(final SecurityContext securityContext,
																								final CRUDResult<SecurityPrincipalImpersonation> impersonationLoad) {
		COREServiceMethodExecResult<SecurityPrincipalImpersonation> outResult = null;
		
		if (impersonationLoad.hasSucceeded()) {
			// The ACTIVE impersonation EXISTS
			// update the impersonation to mark it as ended
			SecurityPrincipalImpersonation activeImpersonation = impersonationLoad.getOrThrow();
			activeImpersonation.endImpersonation();
			
			CRUDResult<SecurityPrincipalImpersonation> update = _impersonationCrud.update(securityContext,
																						  activeImpersonation);
			if (update.hasSucceeded()) {
				outResult = new COREServiceMethodExecOK<>(COREServiceMethod.current(),
													 	  update.getOrThrow());
			} else {
				outResult = new COREServiceMethodExecError<>(COREServiceMethod.current(),
															 update.asCRUDError());
			}
		} else {
			// The ACTIVE impersonation EXISTS
			outResult = new COREServiceMethodExecError<>(COREServiceMethod.current(),
														 impersonationLoad.asCRUDError());
		}
		return outResult;
	}
}
