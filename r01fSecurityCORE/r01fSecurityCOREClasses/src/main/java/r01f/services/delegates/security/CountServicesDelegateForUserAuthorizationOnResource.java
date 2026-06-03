package r01f.services.delegates.security;

import java.util.Collection;

import r01f.api.interfaces.security.authorization.CountServicesForUserAuthorizationOnResource;
import r01f.eventbus.EventBus;
import r01f.exceptions.ExceptionSeverity;
import r01f.model.persistence.CountResult;
import r01f.model.persistence.CountResultBuilder;
import r01f.model.security.authorization.UserAuthorizationOnResource;
import r01f.model.security.summaries.UserAuthWithRoleCount;
import r01f.model.services.COREServiceErrorOrigin;
import r01f.model.services.COREServiceErrorType;
import r01f.model.services.COREServiceMethod;
import r01f.model.services.COREServiceMethodExecError;
import r01f.model.services.COREServiceMethodExecResult;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.UserRole;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.securitycontext.SecurityOIDs.UserAuthorizationOnResourceOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.CountServicesForModelObjectDelegateBase;

public abstract class CountServicesDelegateForUserAuthorizationOnResource<A extends UserAuthorizationOnResource>
			  extends CountServicesForModelObjectDelegateBase<UserAuthorizationOnResourceOID,A>
		   implements CountServicesForUserAuthorizationOnResource<A> {
	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public CountServicesDelegateForUserAuthorizationOnResource(final ServicesCoreModuleConfig coreCfg,
												   			   final Class<A> modelObjectType,
												   			   final CountServicesForUserAuthorizationOnResource<A> dbCount,
															   final Marshaller marshaller,
															   final EventBus eventBus) {
		super(coreCfg,
			  modelObjectType,
			  dbCount,
			  eventBus);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public CountResult<A> countUserAuthsOnResource(final SecurityContext securityContext,
												   final AuthorizationTargetResourceOID resourceOid) {
		if (resourceOid == null) {
			return CountResultBuilder.using(securityContext)
									 .on(_modelObjectType)
									 .badClientRequest("The resource oid MUST not be null")
									 .build();
		}
		CountResult<A> outResult = this.getServiceImplAs(CountServicesForUserAuthorizationOnResource.class)
									   .countUserAuthsOnResource(securityContext,
												  				 resourceOid);
		return outResult;
	}
	@Override @SuppressWarnings("unchecked")
	public CountResult<A> countUserAuthsOnResource(final SecurityContext securityContext,
												   final AuthorizationTargetResourceOID resourceOid,
												   final UserRole userRole) {
		if (resourceOid == null
		 || userRole == null) {
			return CountResultBuilder.using(securityContext)
									 .on(_modelObjectType)
									 .badClientRequest("The resource oid and user role MUST not be null")
									 .build();
		}
		CountResult<A> outResult = this.getServiceImplAs(CountServicesForUserAuthorizationOnResource.class)
									   .countUserAuthsOnResource(securityContext,
												  				 resourceOid,userRole);
		return outResult;
	}
	@Override @SuppressWarnings("unchecked")
	public COREServiceMethodExecResult<Collection<UserAuthWithRoleCount>> countUserAuthsOnResourceByRole(final SecurityContext securityContext,
																										 final AuthorizationTargetResourceOID resourceOid) {
		if (resourceOid == null) {
			return new COREServiceMethodExecError<Collection<UserAuthWithRoleCount>>(COREServiceMethod.named("countUserAuthsOnResourceByRole"),
																					 COREServiceErrorType.originatedAt(COREServiceErrorOrigin.CLIENT)
																					 					 .withName("badClientRequest")
																					 					 .noCodes()
																					 					 .severity(ExceptionSeverity.RECOVERABLE)
																					 					 .build(),
																					 "resource oid is mandatory!");
		}
		COREServiceMethodExecResult<Collection<UserAuthWithRoleCount>> outResult = this.getServiceImplAs(CountServicesForUserAuthorizationOnResource.class)
																					   .countUserAuthsOnResourceByRole(securityContext,
																								  				 	   resourceOid);
		return outResult;
	}
}
