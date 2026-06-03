package r01f.services.delegates.security;


import r01f.api.interfaces.security.authorization.CRUDServicesForUserAuthorizationOnResource;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.CRUDResultBuilder;
import r01f.model.persistence.PersistenceRequestedOperation;
import r01f.model.security.authorization.UserAuthorizationOnResource;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.UserRole;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.securitycontext.SecurityOIDs.UserAuthorizationOnResourceOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.CRUDServicesForModelObjectDelegateBase;
import r01f.services.core.delegates.persistence.ValidatesModelObjectBeforeCreateOrUpdate;
import r01f.validation.ObjectValidationResult;
import r01f.validation.ObjectValidationResultBuilder;

public abstract class CRUDServicesDelegateForUserAuthorizationOnResource<A extends UserAuthorizationOnResource>
			  extends CRUDServicesForModelObjectDelegateBase<UserAuthorizationOnResourceOID,A>
		   implements CRUDServicesForUserAuthorizationOnResource<A>,
					  ValidatesModelObjectBeforeCreateOrUpdate<A> {
	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public CRUDServicesDelegateForUserAuthorizationOnResource(final ServicesCoreModuleConfig coreCfg,
												   			  final Class<A> modelObjectType,
												   			  final CRUDServicesForUserAuthorizationOnResource<A> dbCrud,
															  final Marshaller marshaller,
															  final EventBus eventBus) {
		super(coreCfg,
			  modelObjectType,
			  dbCrud,
			  eventBus);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  EXTENSION METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<A> loadBy(final SecurityContext securityContext,
								final UserOID userOid,
								final AuthorizationTargetResourceOID resourceOid) {
		if (userOid == null 
		 || resourceOid == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientBadRequest("The OIDs MUST not be null")
									.build();
		}
		CRUDResult<A> outResult = this.getServiceImplAs(CRUDServicesForUserAuthorizationOnResource.class)
										  .loadBy(securityContext,
												  userOid,resourceOid);
		return outResult;
	}
	
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<A> loadBy(final SecurityContext securityContext,
								final UserOID userOid,
								final AuthorizationTargetResourceOID resourceOid,
								final UserRole role) {
		if (userOid == null 
		 || resourceOid == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientBadRequest("The OIDs MUST not be null")
									.build();
		}
		CRUDResult<A> outResult = this.getServiceImplAs(CRUDServicesForUserAuthorizationOnResource.class)
										  .loadBy(securityContext,
												  userOid,resourceOid,role);
		return outResult;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	PARAMS VALIDATION ON CREATION / UPDATE
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public ObjectValidationResult<A> validateModelObjBeforeCreateOrUpdate(final SecurityContext securityContext,
																		  final PersistenceRequestedOperation requestedOp,
																		  final A userAuthOnResource) {
		CRUDResult<A> other;
		if (userAuthOnResource.getRole()!=null) {
			other = this.loadBy(securityContext,
								userAuthOnResource.getUserOid(),
								userAuthOnResource.getAuthorizationTargetResourceOid(),
								userAuthOnResource.getRole());
		} else {
			other = this.loadBy(securityContext,
								userAuthOnResource.getUserOid(),
								userAuthOnResource.getAuthorizationTargetResourceOid());
		}
		if (other.hasSucceeded()) {
			boolean existsOther = false;
			A otherUserAuth = other.getOrThrow();
			if (requestedOp == PersistenceRequestedOperation.CREATE) {
				existsOther = true;
			} 
			else if (requestedOp == PersistenceRequestedOperation.UPDATE) {
				existsOther = otherUserAuth.getOid().isNOT(userAuthOnResource.getOid());
			}
			
			if (existsOther) return ObjectValidationResultBuilder.on(userAuthOnResource)
												.notValidwithErrorCode(100)
												.isNotValidBecause("Exists another resource with the same userOid={} / reesourceOid={} / role={}",
																	userAuthOnResource.getUserOid(),userAuthOnResource.getAuthorizationTargetResourceOid(),userAuthOnResource.getRole());
		}
		
		// [3]: Return
		return ObjectValidationResultBuilder.on(userAuthOnResource).isValid();
	}
	
}
