package r01f.client.api.sub.security.principal.impersonation;

import jakarta.inject.Provider;
import r01f.api.interfaces.security.principal.impersonation.SecurityPrincipalImpersonationServices;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.impersonation.SecurityPrincipalImpersonation;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.api.delegates.ClientAPIServiceDelegateBase;

public class ClientAPIDelegateForPrincipalImpersonationServices
     extends ClientAPIServiceDelegateBase<SecurityPrincipalImpersonationServices> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForPrincipalImpersonationServices(final Provider<SecurityContext>  securityContextProvider,
															  final Marshaller marshaller,
															  final SecurityPrincipalImpersonationServices impersonationServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  impersonationServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	UTIL METHODS TO BUILD A NEW [SECURITY CONTEXT] FOR AN IMPERSONATED [PRINCIPAL]
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Creates a [security context]
	 * 		- for the impersonated [principal] when starting to impersonate another [principal]
	 * 		- for the impersonating [principal] when ending the impersonation of another [principal]
	 * <pre>
	 * 		LoadsSecurityPrincipalFromPrincipalOID<MyPrincipal> principalLoader = ...;
	 * 		CreatesSecurityContextFromPrincipal<MyPrincipal> securityContextFactory = ...;
	 * 
	 * 		// use the NEW [security context] belonging to the IMPERSONATED [principal]
	 * 		// instead of the current one (that belongs to the IMPERSONATING [principal]
	 * 		SecurityContext impersonatedSecurityContext = securityApi.getPrincipalImpersonationApi()
	 * 				   												 .getForImpersonation()
	 * 				   												 .createSecurityContextBuilderUsing(principalLoader,
	 * 																									securityContextFactory)
	 * 																 .forStartImpersonating(impersonatedPrincipalOid);
	 * 		// ... any operation performed from now on will be done on behalf of the IMPERSONATED [principal]
	 * 
	 * 		// END the impersonation and restore the [security context] of the IMPERSONATING [principal]
	 * 		SecurityContext impersonatingSecurityContext = securityApi.getPrincipalImpersonationApi()
	 * 				   												  .getForImpersonation()
	 * 				   												  .createSecurityContextBuilderUsing(principalLoader,
	 * 																									 securityContextFactory)
	 * 																  .forFinishImpersonating();
	 * </pre>
	 * @param <P>
	 * @param principalLoader
	 * @param securityContextCreator
	 * @return
	 */
	public <P extends IsSecurityPrincipal> PrincipalImpersonationSecurityContextBuilder<P> createSecurityContextBuilderUsing(final LoadsSecurityPrincipalFromPrincipalOID<P> principalLoader,
											 				  							  									 final CreatesSecurityContextFromPrincipal<P> securityContextCreator) {
		return new PrincipalImpersonationSecurityContextBuilder<>(principalLoader,
															      securityContextCreator);
	}
	public class PrincipalImpersonationSecurityContextBuilder<P extends IsSecurityPrincipal> {
		private final LoadsSecurityPrincipalFromPrincipalOID<P> _principalLoader;
		private final CreatesSecurityContextFromPrincipal<P> _securityContextCreator;
		
		public PrincipalImpersonationSecurityContextBuilder(final LoadsSecurityPrincipalFromPrincipalOID<P> principalLoader,
															final CreatesSecurityContextFromPrincipal<P> securityContextCreator) {
			_principalLoader = principalLoader;
			_securityContextCreator = securityContextCreator;
		}
		/**
		 * CREATES a new [security context] for the impersonated [principal]
		 * @param impersonatedPrincipalOid the OID of the IMPERSONATED [principal] (the one that is being impersonated)
		 * @return
		 */
		public SecurityContext forStartImpersonating(final SecurityPrincipalOID impersonatedPrincipalOid) {
			SecurityContext securityContextOfImpersonatingPrincipal = ClientAPIDelegateForPrincipalImpersonationServices.this.getSecurityContext();
			SecurityPrincipalOID impersonatingPrincipalOid = securityContextOfImpersonatingPrincipal.getSecurityPrincipalOid();
			
			// [0] - Load the impersonated [principal]
			P impersonatedPrincipal = _loadPrincipalOrThrow(impersonatedPrincipalOid);
			
			// [1] - Create the impersonation
			SecurityPrincipalImpersonationServices impersonationServices = ClientAPIDelegateForPrincipalImpersonationServices.this.getServiceProxyAs(SecurityPrincipalImpersonationServices.class);

			var impersonationStart = impersonationServices.theSecurityContextPrincipalSTARTSImpersonating(securityContextOfImpersonatingPrincipal,
																										  impersonatedPrincipalOid);			
			if (impersonationStart.hasFailed()) throw new IllegalStateException("Error creating the [impersonation] for the impersonated [principal]=" + impersonatedPrincipalOid + " by impersonating [principal]=" + impersonatingPrincipalOid + ": " + impersonationStart.asCOREServiceMethodExecError().getDetailedMessage());
			
			// [2] - Create the NEW [security context] for the impersonated [principal]
			//			- The [principal] is the one being impersonated
			//			- The impersonating [principal] is the one that is impersonating the other [principal]
			SecurityContext impersonatedSecurityContext = _securityContextCreator.createSecurityContext(impersonatedPrincipal);
			return impersonatedSecurityContext;
		}
		/**
		 * CREATES a new [security context] for the impersonating [principal] that is currently impersonating another [principal]
		 * @return
		 */
		public SecurityContext forFinishImpersonating() {
			SecurityContext securityContextOfImpersonatedPrincipal = ClientAPIDelegateForPrincipalImpersonationServices.this.getSecurityContext();
			SecurityPrincipalOID impersonatedPrincipalOid = securityContextOfImpersonatedPrincipal.getSecurityPrincipalOid();
			SecurityPrincipalOID impersonatingPrincipalOid = securityContextOfImpersonatedPrincipal.getImpersonatingSecurityPrincipalOid();
			
			// [0] - Load the impersonated [principal]
			P impersonatingPrincipal = _loadPrincipalOrThrow(impersonatingPrincipalOid);
			
			// [0] - End the impersonation
			SecurityPrincipalImpersonationServices impersonationServices = ClientAPIDelegateForPrincipalImpersonationServices.this.getServiceProxyAs(SecurityPrincipalImpersonationServices.class);
			
			var impersonationEnd = impersonationServices.theSecurityContextPrincipalFINISHImpersonation(securityContextOfImpersonatedPrincipal);			
			if (impersonationEnd.hasFailed()) throw new IllegalStateException("Error ending the [impersonation] for the impersonated [principal]=" + impersonatedPrincipalOid + " by impersonating [principal]=" + impersonatingPrincipalOid + ": " + impersonationEnd.asCOREServiceMethodExecError().getDetailedMessage());
			
			// [1] - Create the NEW [security context] for the impersonating [principal]
			//		 (restore the [security context] of the impersonating [principal])
			//			- The [principal] is the one being impersonated
			//			- The impersonating [principal] is the one that is impersonating the other [principal]
			SecurityContext impersonatedSecurityContext = _securityContextCreator.createSecurityContext(impersonatingPrincipal);
			return impersonatedSecurityContext;
		}
		private P _loadPrincipalOrThrow(final SecurityPrincipalOID principalOid) {
			var principalLoad = _principalLoader.loadPrincipal(principalOid);
			if (principalLoad.hasFailed()
			 && principalLoad.asCRUDError()
							 .wasBecauseClientRequestedEntityWasNOTFound()) { 
				throw new IllegalArgumentException("The [principal]=" + principalOid + " does NOT exist");
			} else if (principalLoad.hasFailed()) {
				throw new IllegalStateException("Error loading the [principal]=" + principalOid + ": " + principalLoad.asCRUDError().getDetailedMessage());
			}
			return principalLoad.getOrThrow();
		}
	}
	////////// --- INTERFACES
	public interface LoadsSecurityPrincipalFromPrincipalOID<P extends IsSecurityPrincipal> {
		public CRUDResult<P> loadPrincipal(final SecurityPrincipalOID securityPrincipal);
	} 
	public interface CreatesSecurityContextFromPrincipal<P extends IsSecurityPrincipal> {
		public SecurityContext createSecurityContext(final P securityPrincipal);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * CREATES a new [impersonation] that contains:
	 *	- the impersonating [principal] (comes from the [security context] = the logged in user)
	 *  - the impersonated [principal] (comes from a parameter)
	 * 	- a generated RANDOM [security token] that will be used to SIGN the [impersonation JWT]
	 * @param impersonatedPrincipalOid
	 * @param token
	 * @return
	 */
	public SecurityPrincipalImpersonation theSecurityContextPrincipalSTARTSImpersonating(final SecurityPrincipalOID impersonatedPrincipalOid) {
		return this.getServiceProxyAs(SecurityPrincipalImpersonationServices.class)
				   .theSecurityContextPrincipalSTARTSImpersonating(this.getSecurityContext(),
				   					   							   impersonatedPrincipalOid)
				   .getOrThrow();
	}
	/**
	 * ENDS the currently active [impersonation] for the [security context] impersonating [principal]
	 * @param securityContext
	 * @return
	 */
	public SecurityPrincipalImpersonation theSecurityContextPrincipalFINISHImpersonation() {
		return this.getServiceProxyAs(SecurityPrincipalImpersonationServices.class)
				   .theSecurityContextPrincipalFINISHImpersonation(this.getSecurityContext())
				   .getOrThrow();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Loads a previously-created [impersonation] for a given impersonated [principal]
	 * that is still active 
	 * @param impersonatedPrincipalOid
	 * @return
	 */
	public SecurityPrincipalImpersonation loadActiveImpersonationForImpersonatedPrincipal(final SecurityPrincipalOID impersonatedPrincipalOid) {
		return this.getServiceProxyAs(SecurityPrincipalImpersonationServices.class)
				   .loadActiveImpersonationForImpersonatedPrincipal(this.getSecurityContext(),
				   							  impersonatedPrincipalOid)
				   .getOrThrow();
	}
	/**
	 * Loads a previously-created [impersonation] for a given impersonated [principal]
	 * that is still active UPDATES it setting the [endedAt] date to NOW 
	 * loaded impersonating [login] 
	 * @param impersonatedPrincipalOid
	 * @return
	 */
	public SecurityPrincipalImpersonation endCurrentlyActiveImpersonationForImpersonatedPrincipal(final SecurityPrincipalOID impersonatedPrincipalOid) {
		return this.getServiceProxyAs(SecurityPrincipalImpersonationServices.class)
				   .endCurrentlyActiveImpersonationForImpersonatedPrincipal(this.getSecurityContext(),
				   														    impersonatedPrincipalOid)
				   .getOrThrow();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Loads a previously-created [impersonation] for a given impersonating [principal]
	 * that is still active 
	 * @param impersonatingPrincipalOid
	 * @return
	 */
	public SecurityPrincipalImpersonation loadActiveImpersonationForImpersonatingPrincipal(final SecurityPrincipalOID impersonatingPrincipalOid) {
		return this.getServiceProxyAs(SecurityPrincipalImpersonationServices.class)
				   .loadActiveImpersonationForImpersonatingPrincipal(this.getSecurityContext(),
				   							   						 impersonatingPrincipalOid)
				   .getOrThrow();
	}
	/**
	 * Loads a previously-created [impersonation] for a given impersonationg [principal]
	 * that is still active UPDATES it setting the [endedAt] date to NOW 
	 * @param impersonatingLoginOid
	 * @return
	 */
	public SecurityPrincipalImpersonation endCurrentlyActiveImpersonationForImpersonatingPrincipal(final SecurityPrincipalOID impersonatingPrincipalOid) {
		return this.getServiceProxyAs(SecurityPrincipalImpersonationServices.class)
				   .endCurrentlyActiveImpersonationForImpersonatingPrincipal(this.getSecurityContext(),
				   														     impersonatingPrincipalOid)
				   .getOrThrow();
	}
}
