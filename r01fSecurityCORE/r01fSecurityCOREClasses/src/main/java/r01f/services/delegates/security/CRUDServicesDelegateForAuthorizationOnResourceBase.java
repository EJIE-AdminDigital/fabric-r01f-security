package r01f.services.delegates.security;


import r01f.api.interfaces.security.authorization.CRUDServicesForAuthorizationOnResource;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.CRUDResultBuilder;
import r01f.model.persistence.PersistenceRequestedOperation;
import r01f.model.security.authorization.SecurityPrincipalAuthorizationOnResource;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.SecurityRole;
import r01f.oids.SecurityOIDs.AuthorizationOnResourceOID;
import r01f.oids.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.CRUDServicesForModelObjectDelegateBase;
import r01f.services.core.delegates.persistence.ValidatesModelObjectBeforeCreateOrUpdate;
import r01f.validation.ObjectValidationResult;
import r01f.validation.ObjectValidationResultBuilder;

public abstract class CRUDServicesDelegateForAuthorizationOnResourceBase
			  extends CRUDServicesForModelObjectDelegateBase<AuthorizationOnResourceOID,SecurityPrincipalAuthorizationOnResource>
		   implements CRUDServicesForAuthorizationOnResource,
					  ValidatesModelObjectBeforeCreateOrUpdate<SecurityPrincipalAuthorizationOnResource> {
	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public CRUDServicesDelegateForAuthorizationOnResourceBase(final ServicesCoreModuleConfig coreCfg,
												   			  final CRUDServicesForAuthorizationOnResource dbCrud,
															  final Marshaller marshaller,
															  final EventBus eventBus) {
		super(coreCfg,
			  SecurityPrincipalAuthorizationOnResource.class,
			  dbCrud,
			  eventBus);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  EXTENSION METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override 
	public CRUDResult<SecurityPrincipalAuthorizationOnResource> loadBy(final SecurityContext securityContext,
																	   final SecurityPrincipalOID principalOid,
																	   final AuthorizationTargetResourceOID resourceOid) {
		if (principalOid == null 
		 || resourceOid == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientBadRequest("The OIDs MUST not be null")
									.build();
		}
		CRUDResult<SecurityPrincipalAuthorizationOnResource> outResult = this.getServiceImplAs(CRUDServicesForAuthorizationOnResource.class)
																			 .loadBy(securityContext,
																					 principalOid,
																					 resourceOid);
		return outResult;
	}
	
	@Override 
	public CRUDResult<SecurityPrincipalAuthorizationOnResource> loadBy(final SecurityContext securityContext,
																	   final SecurityPrincipalOID principalOid,
																	   final AuthorizationTargetResourceOID resourceOid,final SecurityRole role) {
		if (principalOid == null 
		 || resourceOid == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientBadRequest("The OIDs MUST not be null")
									.build();
		}
		CRUDResult<SecurityPrincipalAuthorizationOnResource> outResult = this.getServiceImplAs(CRUDServicesForAuthorizationOnResource.class)
										  .loadBy(securityContext,
												  principalOid,
												  resourceOid,role);
		return outResult;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	PARAMS VALIDATION ON CREATION / UPDATE
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public ObjectValidationResult<SecurityPrincipalAuthorizationOnResource> validateModelObjBeforeCreateOrUpdate(final SecurityContext securityContext,
																												 final PersistenceRequestedOperation requestedOp,
																												 final SecurityPrincipalAuthorizationOnResource authOnResource) {
		CRUDResult<SecurityPrincipalAuthorizationOnResource> other;
		if (authOnResource.getRole()!=null) {
			other = this.loadBy(securityContext,
								authOnResource.getSecurityPrincipalOid(),
								authOnResource.getAuthorizationTargetResourceOid(),
								authOnResource.getRole());
		} else {
			other = this.loadBy(securityContext,
								authOnResource.getSecurityPrincipalOid(),
								authOnResource.getAuthorizationTargetResourceOid());
		}
		if (other.hasSucceeded()) {
			boolean existsOther = false;
			SecurityPrincipalAuthorizationOnResource otherAuth = other.getOrThrow();
			if (requestedOp == PersistenceRequestedOperation.CREATE) {
				existsOther = true;
			} 
			else if (requestedOp == PersistenceRequestedOperation.UPDATE) {
				existsOther = otherAuth.getOid().isNOT(authOnResource.getOid());
			}
			
			if (existsOther) return ObjectValidationResultBuilder.on(authOnResource)
												.notValidwithErrorCode(100)
												.isNotValidBecause("Exists another resource with the same principalOid={} / resourceOid={} / role={}",
																	authOnResource.getSecurityPrincipalOid(),authOnResource.getAuthorizationTargetResourceOid(),authOnResource.getRole());
		}
		
		// [3]: Return
		return ObjectValidationResultBuilder.on(authOnResource).isValid();
	}
	
}
