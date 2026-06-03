package r01f.services.delegates.security;


import r01f.api.interfaces.security.authorization.FindServicesForUserAuthorizationOnResource;
import r01f.eventbus.EventBus;
import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.model.persistence.FindResult;
import r01f.model.security.authorization.UserAuthorizationOnResource;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.securitycontext.SecurityOIDs.UserAuthorizationOnResourceOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.FindServicesForModelObjectDelegateBase;

public abstract class FindServicesDelegateForUserAuthorizationOnResource<A extends UserAuthorizationOnResource>
			  extends FindServicesForModelObjectDelegateBase<UserAuthorizationOnResourceOID,A>
		   implements FindServicesForUserAuthorizationOnResource<A> {

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public FindServicesDelegateForUserAuthorizationOnResource(final ServicesCoreModuleConfig coreCfg,
												 	   		  final Class<A> modelObjectType,
												 	   		  final FindServicesForUserAuthorizationOnResource<A> dbCrud,
												 	   		  final Marshaller marshaller,
												 	   		  final EventBus eventBus) {
		super(coreCfg,
			  modelObjectType,
			  dbCrud,
			  eventBus);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public FindResult<A> findBy(final SecurityContext securityContext,
								final UserOID userOid) {
		// [0] - check the oid
		if (userOid == null) throw new IllegalArgumentException("user oid is mandatory to find [auths on resource] by [user]");
		// [1] - Find
		FindResult<A> outFindResult = this.getServiceImplAs(FindServicesForUserAuthorizationOnResource.class)
												.findBy(securityContext,
														userOid);
		// [3] - Return
		return outFindResult;
	}
	@Override @SuppressWarnings("unchecked")
	public FindResult<A> findBy(final SecurityContext securityContext,
								final UserOID userOid,
								final AppCode appCode,final AppComponent appComp) {
		// [0] - check the oid, appCode & appComponent
		if (userOid == null) throw new IllegalArgumentException("user oid is mandatory to find [auths on resource] by [user]");
		if (appCode == null && appComp == null) return this.findBy(securityContext,
																   userOid);
		// [1] - Find
		FindResult<A> outFindResult = this.getServiceImplAs(FindServicesForUserAuthorizationOnResource.class)
												.findBy(securityContext,
														userOid,
														appCode,appComp);
		// [3] - Return
		return outFindResult;
	}
	@Override @SuppressWarnings("unchecked")
	public FindResult<A> findBy(final SecurityContext securityContext,
								final AuthorizationTargetResourceOID resOid) {
		// [0] - check the oid
		if (resOid == null) throw new IllegalArgumentException("resource oid is mandatory to find [auths on resource] by [resource]");
		
		// [1] - Find
		FindResult<A> outFindResult = this.getServiceImplAs(FindServicesForUserAuthorizationOnResource.class)
												.findBy(securityContext,
														resOid);
		// [3] - Return
		return outFindResult;
	}
	@Override @SuppressWarnings("unchecked")
	public FindResult<A> findBy(final SecurityContext securityContext, 
								final UserOID userOid,final AuthorizationTargetResourceOID resOid) {
		// [1] - Load
		FindResult<A> outEntityLoadResult = this.getServiceImplAs(FindServicesForUserAuthorizationOnResource.class)
												.findBy(securityContext,
														userOid,resOid);
		// [3] - Return
		return outEntityLoadResult;
	}
}
