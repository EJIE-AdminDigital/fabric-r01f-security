package r01f.services.security;



import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.authorization.CRUDServicesForAuthorizationOnResource;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.authorization.SecurityPrincipalAuthorizationOnResource;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.SecurityRole;
import r01f.oids.SecurityOIDs.AuthorizationOnResourceOID;
import r01f.oids.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreCRUDServicesForModelObjectBase;

@Accessors(prefix="_")
public abstract class CoreCRUDServicesImplForAuthorizationOnResourceBase
			  extends CoreCRUDServicesForModelObjectBase<AuthorizationOnResourceOID,SecurityPrincipalAuthorizationOnResource>
		   implements CRUDServicesForAuthorizationOnResource,
					  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreCRUDServicesImplForAuthorizationOnResourceBase(final ServicesCoreModuleConfig coreCfg,
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
	@Override 
	public CRUDResult<SecurityPrincipalAuthorizationOnResource> loadBy(final SecurityContext securityContext,
																	   final SecurityPrincipalOID securityPrincipalOid,
																	   final AuthorizationTargetResourceOID resourceOid) {
		CRUDResult<SecurityPrincipalAuthorizationOnResource> outResult = this.forSecurityContext(securityContext)
																			 .createDelegateAs(CRUDServicesForAuthorizationOnResource.class)
																			 .loadBy(securityContext,
																					 securityPrincipalOid,
																					 resourceOid);
		return outResult;
	}
	@Transactional
	@Override 
	public CRUDResult<SecurityPrincipalAuthorizationOnResource> loadBy(final SecurityContext securityContext,
																	   final SecurityPrincipalOID securityPrincipalOid,
																	   final AuthorizationTargetResourceOID resourceOid,
																	   final SecurityRole role) {
		CRUDResult<SecurityPrincipalAuthorizationOnResource> outResult = this.forSecurityContext(securityContext)
																			 .createDelegateAs(CRUDServicesForAuthorizationOnResource.class)
																			 .loadBy(securityContext,
																					 securityPrincipalOid,
																					 resourceOid,role);
		return outResult;
	}
}
