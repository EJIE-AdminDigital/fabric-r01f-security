package r01f.services.security;

import java.util.Collection;

import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.authorization.CountServicesForAuthorizationOnResource;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CountResult;
import r01f.model.security.authorization.SecurityPrincipalAuthorizationOnResource;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.summaries.AuthorizationsWithRoleCount;
import r01f.model.services.COREServiceMethodExecResult;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.SecurityRole;
import r01f.oids.SecurityOIDs.AuthorizationOnResourceOID;
import r01f.oids.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreCountServicesForModelObjectBase;

@Accessors(prefix="_")
public abstract class CoreCountServicesImplForAuthorizationOnResourceBase
			  extends CoreCountServicesForModelObjectBase<AuthorizationOnResourceOID,SecurityPrincipalAuthorizationOnResource>
		   implements CountServicesForAuthorizationOnResource,
					  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreCountServicesImplForAuthorizationOnResourceBase(final ServicesCoreModuleConfig coreCfg,
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
	@Override 
	public CountResult<SecurityPrincipalAuthorizationOnResource> countSecurityPrincipalsWithAuthsOnResource(final SecurityContext securityContext,
												   				     					   					final AuthorizationTargetResourceOID resourceOid) {
		CountResult<SecurityPrincipalAuthorizationOnResource> outResult = this.forSecurityContext(securityContext)
																			  .createDelegateAs(CountServicesForAuthorizationOnResource.class)
																			  .countSecurityPrincipalsWithAuthsOnResource(securityContext,
																					  									  resourceOid);
		return outResult;
	}
	@Override 
	public CountResult<SecurityPrincipalAuthorizationOnResource> countSecurityPrincipalsWithAuthsOnResource(final SecurityContext securityContext,
																     										final AuthorizationTargetResourceOID resourceOid,final SecurityRole role) {
		CountResult<SecurityPrincipalAuthorizationOnResource> outResult = this.forSecurityContext(securityContext)
																			  .createDelegateAs(CountServicesForAuthorizationOnResource.class)
																			  .countSecurityPrincipalsWithAuthsOnResource(securityContext,
																					  									  resourceOid,role);
		return outResult;
	}
	@Override 
	public COREServiceMethodExecResult<Collection<AuthorizationsWithRoleCount>> countSecurityPrincipalsWithAuthsOnResourceByRole(final SecurityContext securityContext,
																										 						 final AuthorizationTargetResourceOID resourceOid) {
		COREServiceMethodExecResult<Collection<AuthorizationsWithRoleCount>> outResult = this.forSecurityContext(securityContext)
																						  .createDelegateAs(CountServicesForAuthorizationOnResource.class)
																						  .countSecurityPrincipalsWithAuthsOnResourceByRole(securityContext,
																								  						  					resourceOid);
		return outResult;
	}
}
