package r01f.services.security;

import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.authorization.FindServicesForAuthorizationTargetResource;
import r01f.eventbus.EventBus;
import r01f.locale.Language;
import r01f.model.persistence.FindResult;
import r01f.model.persistence.FindSummariesResult;
import r01f.model.security.authorization.AuthorizationTargetResource;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.oids.CommonOIDs.AppCode;
import r01f.oids.CommonOIDs.AppComponent;
import r01f.oids.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreFindServicesForModelObjectBase;

@Accessors(prefix="_")
public abstract class CoreFindServicesImplForAuthorizationTargetResourceBase
			  extends CoreFindServicesForModelObjectBase<AuthorizationTargetResourceOID,AuthorizationTargetResource>
		   implements FindServicesForAuthorizationTargetResource,
					  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreFindServicesImplForAuthorizationTargetResourceBase(final ServicesCoreModuleConfig coreCfg,
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
	public FindResult<AuthorizationTargetResource> findAllResources(final SecurityContext securityContext) {
		return this.forSecurityContext(securityContext)
						.createDelegateAs(FindServicesForAuthorizationTargetResource.class)
							.findAllResources(securityContext);
	}
	@Override 
	public FindResult<AuthorizationTargetResource> findBy(final SecurityContext securityContext,
														  final AppCode appCode,final AppComponent appComp) {
		return this.forSecurityContext(securityContext)
						.createDelegateAs(FindServicesForAuthorizationTargetResource.class)
							.findBy(securityContext,
									appCode,appComp);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SUMMARIZED
/////////////////////////////////////////////////////////////////////////////////////////
	@Override 
	public FindSummariesResult<AuthorizationTargetResource> findAllResourcesSummarized(final SecurityContext securityContext,
																					   final Language lang) {
		return this.forSecurityContext(securityContext)
						.createDelegateAs(FindServicesForAuthorizationTargetResource.class)
							.findAllResourcesSummarized(securityContext,
														lang);
	}
	@Override 
	public FindSummariesResult<AuthorizationTargetResource> findSummarizedBy(final SecurityContext securityContext,
																		     final AppCode appCode,final AppComponent appComponent,
																		     final Language lang) {
		return this.forSecurityContext(securityContext)
						.createDelegateAs(FindServicesForAuthorizationTargetResource.class)
							.findSummarizedBy(securityContext,
											  appCode,appComponent,
											  lang);
	}
}
