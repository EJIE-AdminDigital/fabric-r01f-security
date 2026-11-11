package r01f.services.delegates.security;

import java.util.Collection;

import r01f.api.interfaces.security.authorization.CountServicesForAuthorizationOnResource;
import r01f.eventbus.EventBus;
import r01f.exceptions.ExceptionSeverity;
import r01f.model.persistence.CountResult;
import r01f.model.persistence.CountResultBuilder;
import r01f.model.security.authorization.SecurityPrincipalAuthorizationOnResource;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.summaries.AuthorizationsWithRoleCount;
import r01f.model.services.COREServiceErrorOrigin;
import r01f.model.services.COREServiceErrorType;
import r01f.model.services.COREServiceMethod;
import r01f.model.services.COREServiceMethodExecError;
import r01f.model.services.COREServiceMethodExecResult;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.SecurityRole;
import r01f.oids.SecurityOIDs.AuthorizationOnResourceOID;
import r01f.oids.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.CountServicesForModelObjectDelegateBase;

public abstract class CountServicesDelegateForAuthorizationOnResourceBase
			  extends CountServicesForModelObjectDelegateBase<AuthorizationOnResourceOID,SecurityPrincipalAuthorizationOnResource>
		   implements CountServicesForAuthorizationOnResource {
	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public CountServicesDelegateForAuthorizationOnResourceBase(final ServicesCoreModuleConfig coreCfg,
												   			   final CountServicesForAuthorizationOnResource dbCount,
															   final Marshaller marshaller,
															   final EventBus eventBus) {
		super(coreCfg,
			  SecurityPrincipalAuthorizationOnResource.class,
			  dbCount,
			  eventBus);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override 
	public CountResult<SecurityPrincipalAuthorizationOnResource> countSecurityPrincipalsWithAuthsOnResource(final SecurityContext securityContext,
																	 										final AuthorizationTargetResourceOID resourceOid) {
		if (resourceOid == null) return CountResultBuilder.using(securityContext)
									 					  .on(_modelObjectType)
														  .badClientRequest("The resource oid MUST not be null")
														  .build();
		return this.getServiceImplAs(CountServicesForAuthorizationOnResource.class)
				   .countSecurityPrincipalsWithAuthsOnResource(securityContext,
							  				 resourceOid);
	}
	@Override 
	public CountResult<SecurityPrincipalAuthorizationOnResource> countSecurityPrincipalsWithAuthsOnResource(final SecurityContext securityContext,
																     									    final AuthorizationTargetResourceOID resourceOid,final SecurityRole role) {
		if (resourceOid == null
		 || role == null) return CountResultBuilder.using(securityContext)
								 				   .on(_modelObjectType)
								 				   .badClientRequest("The resource oid and role MUST not be null")
								 				   .build();
		return this.getServiceImplAs(CountServicesForAuthorizationOnResource.class)
				   .countSecurityPrincipalsWithAuthsOnResource(securityContext,
							  				 				   resourceOid,role);
	}
	@Override 
	public COREServiceMethodExecResult<Collection<AuthorizationsWithRoleCount>> countSecurityPrincipalsWithAuthsOnResourceByRole(final SecurityContext securityContext,
																										 	   					 final AuthorizationTargetResourceOID resourceOid) {
		if (resourceOid == null) return new COREServiceMethodExecError<Collection<AuthorizationsWithRoleCount>>(COREServiceMethod.current(),
																						   						 COREServiceErrorType.originatedAt(COREServiceErrorOrigin.CLIENT)
																						   						 					 .withName("badClientRequest")
																						   						 					 .noCodes()
																						   						 					 .severity(ExceptionSeverity.RECOVERABLE)
																						   						 					 .build(),
																							 					 "resource oid is mandatory!");
		return this.getServiceImplAs(CountServicesForAuthorizationOnResource.class)
				    .countSecurityPrincipalsWithAuthsOnResourceByRole(securityContext,
									  				resourceOid);
	}
}
