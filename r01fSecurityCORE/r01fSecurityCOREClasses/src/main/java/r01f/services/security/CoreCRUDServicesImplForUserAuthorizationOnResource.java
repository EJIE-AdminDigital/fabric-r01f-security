package r01f.services.security;



import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.authorization.CRUDServicesForUserAuthorizationOnResource;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.authorization.UserAuthorizationOnResource;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.UserRole;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.securitycontext.SecurityOIDs.UserAuthorizationOnResourceOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreCRUDServicesForModelObjectBase;

@Accessors(prefix="_")
public abstract class CoreCRUDServicesImplForUserAuthorizationOnResource<A extends UserAuthorizationOnResource>
			  extends CoreCRUDServicesForModelObjectBase<UserAuthorizationOnResourceOID,A>
		   implements CRUDServicesForUserAuthorizationOnResource<A>,
					  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreCRUDServicesImplForUserAuthorizationOnResource(final ServicesCoreModuleConfig coreCfg,
												   			  final Marshaller modelObjectsMarshaller,
												   			  final EventBus eventBus,
												   			  final Provider<EntityManager> entityManagerProvider) {
		super(coreCfg,
			  modelObjectsMarshaller,
			  eventBus,
			  entityManagerProvider);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EXTENSION  METHODS
/////////////////////////////////////////////////////////////////////////////////////////

	@Transactional
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<A> loadBy(final SecurityContext securityContext,
								final UserOID userOid,
								final AuthorizationTargetResourceOID resourceOid) {
		CRUDResult<A> outResult = this.forSecurityContext(securityContext)
									  .createDelegateAs(CRUDServicesForUserAuthorizationOnResource.class)
									  .loadBy(securityContext,
											  userOid,resourceOid);
		return outResult;
	}

	@Transactional
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<A> loadBy(final SecurityContext securityContext,
								final UserOID userOid,
								final AuthorizationTargetResourceOID resourceOid,
								final UserRole role) {
		CRUDResult<A> outResult = this.forSecurityContext(securityContext)
									  .createDelegateAs(CRUDServicesForUserAuthorizationOnResource.class)
									  .loadBy(securityContext,
											  userOid,resourceOid,role);
		return outResult;
	}
}
