package r01f.services.security;

import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import lombok.experimental.Accessors;
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
import r01f.services.core.persistence.CoreFindServicesForModelObjectBase;

@Accessors(prefix="_")
public abstract class CoreFindServicesImplForUserAuthorizationOnResource<A extends UserAuthorizationOnResource>
			  extends CoreFindServicesForModelObjectBase<UserAuthorizationOnResourceOID,A>
		   implements FindServicesForUserAuthorizationOnResource<A>,
					  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreFindServicesImplForUserAuthorizationOnResource(final ServicesCoreModuleConfig coreCfg,
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
	@Override @SuppressWarnings("unchecked")
	public FindResult<A> findBy(final SecurityContext securityContext,
							    final UserOID userOid) {
		return this.forSecurityContext(securityContext)
						.createDelegateAs(FindServicesForUserAuthorizationOnResource.class)
							.findBy(securityContext,
									userOid);
	}
	@Override @SuppressWarnings("unchecked")
	public FindResult<A> findBy(final SecurityContext securityContext,
								final UserOID userOid,
								final AppCode appCode,final AppComponent appComp) {
		return this.forSecurityContext(securityContext)
						.createDelegateAs(FindServicesForUserAuthorizationOnResource.class)
							.findBy(securityContext,
									userOid,
									appCode,appComp);
	}
	@Override @SuppressWarnings("unchecked")
	public FindResult<A> findBy(final SecurityContext securityContext,
							    final AuthorizationTargetResourceOID resOid) {
		return this.forSecurityContext(securityContext)
						.createDelegateAs(FindServicesForUserAuthorizationOnResource.class)
							.findBy(securityContext,
									resOid);
	}
	@Override @SuppressWarnings("unchecked")
	public FindResult<A> findBy(final SecurityContext securityContext,
								final UserOID userOid,final AuthorizationTargetResourceOID resOid) {
		return this.forSecurityContext(securityContext)
						.createDelegateAs(FindServicesForUserAuthorizationOnResource.class)
							.findBy(securityContext,
									userOid,resOid);
	}
}
