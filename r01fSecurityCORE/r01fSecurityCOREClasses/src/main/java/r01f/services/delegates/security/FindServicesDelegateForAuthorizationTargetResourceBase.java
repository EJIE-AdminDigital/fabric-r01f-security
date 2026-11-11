package r01f.services.delegates.security;



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
import r01f.services.core.delegates.persistence.FindServicesForModelObjectDelegateBase;

public abstract class FindServicesDelegateForAuthorizationTargetResourceBase
			  extends FindServicesForModelObjectDelegateBase<AuthorizationTargetResourceOID,AuthorizationTargetResource>
		   implements FindServicesForAuthorizationTargetResource {

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public FindServicesDelegateForAuthorizationTargetResourceBase(final ServicesCoreModuleConfig coreCfg,
													 	   		  final FindServicesForAuthorizationTargetResource dbFind,
													 	   		  final Marshaller marshaller,
													 	   		  final EventBus eventBus) {
		super(coreCfg,
			  AuthorizationTargetResource.class,
			  dbFind,
			  eventBus);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override 
	public FindResult<AuthorizationTargetResource> findAllResources(final SecurityContext securityContext) {
		// [1] - Load
		FindResult<AuthorizationTargetResource> outFindResult = this.getServiceImplAs(FindServicesForAuthorizationTargetResource.class)
																	.findAllResources(securityContext);
		// [3] - Return
		return outFindResult;
	}
	@Override 
	public FindResult<AuthorizationTargetResource> findBy(final SecurityContext securityContext,
							    						  final AppCode appCode,final AppComponent appComp) {
		// [1] - Load
		FindResult<AuthorizationTargetResource> outFindResult = this.getServiceImplAs(FindServicesForAuthorizationTargetResource.class)
																	.findBy(securityContext,
																			appCode,appComp);
		// [3] - Return
		return outFindResult;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SUMMARIZED
/////////////////////////////////////////////////////////////////////////////////////////
	@Override 
	public FindSummariesResult<AuthorizationTargetResource> findAllResourcesSummarized(final SecurityContext securityContext,
															 						   final Language lang) {
		Language theLang = lang != null ? lang : Language.DEFAULT;
		
		// [1] - Load
		FindSummariesResult<AuthorizationTargetResource> outFindResult = this.getServiceImplAs(FindServicesForAuthorizationTargetResource.class)
																			 .findAllResourcesSummarized(securityContext,
																										 theLang);
		// [3] - Return
		return outFindResult;
	}
	@Override 
	public FindSummariesResult<AuthorizationTargetResource> findSummarizedBy(final SecurityContext securityContext,
																		     final AppCode appCode,final AppComponent appComponent,
																		     final Language lang) {
		Language theLang = lang != null ? lang : Language.DEFAULT;
		
		// [1] - Load
		FindSummariesResult<AuthorizationTargetResource> outFindResult = this.getServiceImplAs(FindServicesForAuthorizationTargetResource.class)
																			 .findSummarizedBy(securityContext,
																							   appCode,appComponent,
																							   theLang);
		// [3] - Return
		return outFindResult;
	}
}
