package r01f.services.security;

import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.authorization.FindServicesForAuthorizationOnResource;
import r01f.eventbus.EventBus;
import r01f.model.persistence.FindResult;
import r01f.model.security.authorization.SecurityPrincipalAuthorizationOnResource;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.oids.CommonOIDs.AppCode;
import r01f.oids.CommonOIDs.AppComponent;
import r01f.oids.SecurityOIDs.AuthorizationOnResourceOID;
import r01f.oids.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreFindServicesForModelObjectBase;

@Accessors(prefix="_")
public abstract class CoreFindServicesImplForAuthorizationOnResourceBase
			  extends CoreFindServicesForModelObjectBase<AuthorizationOnResourceOID,SecurityPrincipalAuthorizationOnResource>
		   implements FindServicesForAuthorizationOnResource,
					  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreFindServicesImplForAuthorizationOnResourceBase(final ServicesCoreModuleConfig coreCfg,
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
	public FindResult<SecurityPrincipalAuthorizationOnResource> findBy(final SecurityContext securityContext,
							    									   final SecurityPrincipalOID securityPrincipalOid) {
		return this.forSecurityContext(securityContext)
						.createDelegateAs(FindServicesForAuthorizationOnResource.class)
							.findBy(securityContext,
									securityPrincipalOid);
	}
	@Override 
	public FindResult<SecurityPrincipalAuthorizationOnResource> findBy(final SecurityContext securityContext,
																	   final SecurityPrincipalOID securityPrincipalOid,
																	   final AppCode appCode,final AppComponent appComp) {
		return this.forSecurityContext(securityContext)
						.createDelegateAs(FindServicesForAuthorizationOnResource.class)
							.findBy(securityContext,
									securityPrincipalOid,
									appCode,appComp);
	}
	@Override 
	public FindResult<SecurityPrincipalAuthorizationOnResource> findBy(final SecurityContext securityContext,
							    									   final AuthorizationTargetResourceOID resOid) {
		return this.forSecurityContext(securityContext)
						.createDelegateAs(FindServicesForAuthorizationOnResource.class)
							.findBy(securityContext,
									resOid);
	}
	@Override 
	public FindResult<SecurityPrincipalAuthorizationOnResource> findBy(final SecurityContext securityContext,
																	   final SecurityPrincipalOID securityPrincipalOid,
																	   final AuthorizationTargetResourceOID resOid) {
		return this.forSecurityContext(securityContext)
						.createDelegateAs(FindServicesForAuthorizationOnResource.class)
							.findBy(securityContext,
									securityPrincipalOid,
									resOid);
	}
}
