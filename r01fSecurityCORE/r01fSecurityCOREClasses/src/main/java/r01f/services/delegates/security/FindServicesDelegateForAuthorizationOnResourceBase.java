package r01f.services.delegates.security;


import r01f.api.interfaces.security.authorization.FindServicesForAuthorizationOnResource;
import r01f.eventbus.EventBus;
import r01f.model.persistence.FindResult;
import r01f.model.security.authorization.SecurityPrincipalAuthorizationOnResource;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.oids.CommonOIDs.AppCode;
import r01f.oids.CommonOIDs.AppComponent;
import r01f.oids.SecurityOIDs.AuthorizationOnResourceOID;
import r01f.oids.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.FindServicesForModelObjectDelegateBase;

public abstract class FindServicesDelegateForAuthorizationOnResourceBase
			  extends FindServicesForModelObjectDelegateBase<AuthorizationOnResourceOID,SecurityPrincipalAuthorizationOnResource>
		   implements FindServicesForAuthorizationOnResource {

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public FindServicesDelegateForAuthorizationOnResourceBase(final ServicesCoreModuleConfig coreCfg,
												 	   		  final FindServicesForAuthorizationOnResource dbCrud,
												 	   		  final Marshaller marshaller,
												 	   		  final EventBus eventBus) {
		super(coreCfg,
			  SecurityPrincipalAuthorizationOnResource.class,
			  dbCrud,
			  eventBus);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override 
	public FindResult<SecurityPrincipalAuthorizationOnResource> findBy(final SecurityContext securityContext,
																	   final SecurityPrincipalOID principalOid) {
		// [0] - check the oid
		if (principalOid == null) throw new IllegalArgumentException("[principal] is mandatory to find [auths on resource]");
		// [1] - Find
		FindResult<SecurityPrincipalAuthorizationOnResource> outFindResult = this.getServiceImplAs(FindServicesForAuthorizationOnResource.class)
											.findBy(securityContext,
													principalOid);
		// [3] - Return
		return outFindResult;
	}
	@Override 
	public FindResult<SecurityPrincipalAuthorizationOnResource> findBy(final SecurityContext securityContext,
																	   final SecurityPrincipalOID principalOid,
																	   final AppCode appCode,final AppComponent appComp) {
		// [0] - check the oid, appCode & appComponent
		if (principalOid == null) throw new IllegalArgumentException("[principal] is mandatory to find [auths on resource]");
		if (appCode == null && appComp == null) return this.findBy(securityContext,
																   principalOid);
		// [1] - Find
		FindResult<SecurityPrincipalAuthorizationOnResource> outFindResult = this.getServiceImplAs(FindServicesForAuthorizationOnResource.class)
											.findBy(securityContext,
													principalOid,
													appCode,appComp);
		// [3] - Return
		return outFindResult;
	}
	@Override 
	public FindResult<SecurityPrincipalAuthorizationOnResource> findBy(final SecurityContext securityContext,
																	   final AuthorizationTargetResourceOID resOid) {
		// [0] - check the oid
		if (resOid == null) throw new IllegalArgumentException("[resource] is mandatory to find [auths on resource]");
		
		// [1] - Find
		FindResult<SecurityPrincipalAuthorizationOnResource> outFindResult = this.getServiceImplAs(FindServicesForAuthorizationOnResource.class)
																				 .findBy(securityContext,
																						 resOid);
		// [3] - Return
		return outFindResult;
	}
	@Override 
	public FindResult<SecurityPrincipalAuthorizationOnResource> findBy(final SecurityContext securityContext, 
																	   final SecurityPrincipalOID principalOid,
																	   final AuthorizationTargetResourceOID resOid) {
		// [0] - check the oid
		if (principalOid == null) throw new IllegalArgumentException("[principal] is mandatory to find [auths on resource]");
		if (resOid == null) throw new IllegalArgumentException("[resource] is mandatory to find [auths on resource]");
		
		// [1] - Load
		FindResult<SecurityPrincipalAuthorizationOnResource> outEntityLoadResult = this.getServiceImplAs(FindServicesForAuthorizationOnResource.class)
												.findBy(securityContext,
														principalOid,
														resOid);
		// [3] - Return
		return outEntityLoadResult;
	}
}
