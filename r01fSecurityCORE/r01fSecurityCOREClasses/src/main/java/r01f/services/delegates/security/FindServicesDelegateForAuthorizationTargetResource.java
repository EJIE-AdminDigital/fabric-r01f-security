package r01f.services.delegates.security;



import r01f.api.interfaces.security.authorization.FindServicesForAuthorizationTargetResource;
import r01f.eventbus.EventBus;
import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.locale.Language;
import r01f.model.persistence.FindResult;
import r01f.model.persistence.FindSummariesResult;
import r01f.model.security.authorization.AuthorizationTargetResource;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.FindServicesForModelObjectDelegateBase;

public abstract class FindServicesDelegateForAuthorizationTargetResource<R extends AuthorizationTargetResource>
			  extends FindServicesForModelObjectDelegateBase<AuthorizationTargetResourceOID,R>
		   implements FindServicesForAuthorizationTargetResource<R> {

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public FindServicesDelegateForAuthorizationTargetResource(final ServicesCoreModuleConfig coreCfg,
												 	   		  final Class<R> modelObjectType,
												 	   		  final FindServicesForAuthorizationTargetResource<R> dbFind,
												 	   		  final Marshaller marshaller,
												 	   		  final EventBus eventBus) {
		super(coreCfg,
			  modelObjectType,
			  dbFind,
			  eventBus);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public FindResult<R> findAllResources(final SecurityContext securityContext) {
		// [1] - Load
		FindResult<R> outFindResult = this.getServiceImplAs(FindServicesForAuthorizationTargetResource.class)
												.findAllResources(securityContext);
		// [3] - Return
		return outFindResult;
	}
	@Override @SuppressWarnings("unchecked")
	public FindResult<R> findBy(final SecurityContext securityContext,
							    final AppCode appCode,final AppComponent appComp) {
		// [1] - Load
		FindResult<R> outFindResult = this.getServiceImplAs(FindServicesForAuthorizationTargetResource.class)
												.findBy(securityContext,
														appCode,appComp);
		// [3] - Return
		return outFindResult;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SUMMARIZED
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public FindSummariesResult<R> findAllResourcesSummarized(final SecurityContext securityContext,
															 final Language lang) {
		// [1] - Load
		FindSummariesResult<R> outFindResult = this.getServiceImplAs(FindServicesForAuthorizationTargetResource.class)
														.findAllResourcesSummarized(securityContext,
																					lang);
		// [3] - Return
		return outFindResult;
	}
	@Override @SuppressWarnings("unchecked")
	public FindSummariesResult<R> findSummarizedBy(final SecurityContext securityContext,
												   final AppCode appCode,final AppComponent appComponent,
												   final Language lang) {
		// [1] - Load
		FindSummariesResult<R> outFindResult = this.getServiceImplAs(FindServicesForAuthorizationTargetResource.class)
													.findSummarizedBy(securityContext,
																	  appCode,appComponent,
																	  lang);
		// [3] - Return
		return outFindResult;
	}
}
