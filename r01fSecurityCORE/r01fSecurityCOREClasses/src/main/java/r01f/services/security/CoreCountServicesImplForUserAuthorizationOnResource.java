package r01f.services.security;

import java.util.Collection;

import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.authorization.CountServicesForUserAuthorizationOnResource;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CountResult;
import r01f.model.security.authorization.UserAuthorizationOnResource;
import r01f.model.security.summaries.UserAuthWithRoleCount;
import r01f.model.services.COREServiceMethodExecResult;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.UserRole;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.securitycontext.SecurityOIDs.UserAuthorizationOnResourceOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreCountServicesForModelObjectBase;

@Accessors(prefix="_")
public abstract class CoreCountServicesImplForUserAuthorizationOnResource<A extends UserAuthorizationOnResource>
			  extends CoreCountServicesForModelObjectBase<UserAuthorizationOnResourceOID,A>
		   implements CountServicesForUserAuthorizationOnResource<A>,
					  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreCountServicesImplForUserAuthorizationOnResource(final ServicesCoreModuleConfig coreCfg,
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
	public CountResult<A> countUserAuthsOnResource(final SecurityContext securityContext,
												   final AuthorizationTargetResourceOID resourceOid) {
		CountResult<A> outResult = this.forSecurityContext(securityContext)
										  .createDelegateAs(CountServicesForUserAuthorizationOnResource.class)
										  .countUserAuthsOnResource(securityContext,
												  					resourceOid);
		return outResult;
	}
	@Override @SuppressWarnings("unchecked")
	public CountResult<A> countUserAuthsOnResource(final SecurityContext securityContext,
												   final AuthorizationTargetResourceOID resourceOid,
												   final UserRole userRole) {
		CountResult<A> outResult = this.forSecurityContext(securityContext)
										  .createDelegateAs(CountServicesForUserAuthorizationOnResource.class)
										  .countUserAuthsOnResource(securityContext,
												  					resourceOid,userRole);
		return outResult;
	}
	@Override @SuppressWarnings("unchecked")
	public COREServiceMethodExecResult<Collection<UserAuthWithRoleCount>> countUserAuthsOnResourceByRole(final SecurityContext securityContext,
																										 final AuthorizationTargetResourceOID resourceOid) {
		COREServiceMethodExecResult<Collection<UserAuthWithRoleCount>> outResult = this.forSecurityContext(securityContext)
																						  .createDelegateAs(CountServicesForUserAuthorizationOnResource.class)
																						  .countUserAuthsOnResourceByRole(securityContext,
																								  						  resourceOid);
		return outResult;
	}
}
