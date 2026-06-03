package r01f.client.api.sub.security.authorization;

import java.util.Collection;

import jakarta.inject.Provider;

import r01f.api.interfaces.security.authorization.FindServicesForAuthorizationTargetResource;
import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.locale.Language;
import r01f.model.persistence.FindResult;
import r01f.model.persistence.FindSummariesResult;
import r01f.model.security.authorization.AuthorizationTargetResource;
import r01f.model.security.summaries.SummarizedAuthorizationResource;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.services.api.delegates.ClientAPIDelegateForModelObjectFindServices;
import r01f.services.api.delegates.ClientAPIModelObjectChangesTrack;

public class ClientAPIDelegateForAuthorizationTargetResourceFindServices<R extends AuthorizationTargetResource,
																		 S extends SummarizedAuthorizationResource<R>>
	 extends ClientAPIDelegateForModelObjectFindServices<AuthorizationTargetResourceOID,R> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForAuthorizationTargetResourceFindServices(final Provider<SecurityContext>  securityContextProvider,
													  				   final Marshaller marshaller,
													  				   final FindServicesForAuthorizationTargetResource<R> findServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  findServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns all secured resources
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Collection<R> findAllResources() {
		FindResult<R> find = this.getServiceProxyAs(FindServicesForAuthorizationTargetResource.class)
								 .findAllResources(this.getSecurityContext());
		Collection<R> outResources = find.getOrThrow();
		ClientAPIModelObjectChangesTrack.startTrackingChangesOnLoaded(outResources);
		return outResources;
	}
	/**
	 * Returns all app's secured resources
	 * @param appCode
	 * @param appComponent
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Collection<R> findBy(final AppCode appCode,final AppComponent appComponent) {
		FindResult<R> find = this.getServiceProxyAs(FindServicesForAuthorizationTargetResource.class)
								 .findBy(this.getSecurityContext(),
										 appCode,appComponent);
		Collection<R> outResources = find.getOrThrow();
		ClientAPIModelObjectChangesTrack.startTrackingChangesOnLoaded(outResources);
		return outResources;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SUMMARIZED
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns all secured resources summarized
	 * @param lang
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Collection<S> findAllResourcesSummarized(final Language lang) {
		FindSummariesResult<R> find = this.getServiceProxyAs(FindServicesForAuthorizationTargetResource.class)
								 		  .findAllResourcesSummarized(this.getSecurityContext(),
								 				  					  lang);
		Collection<S> outResources = find.getSummariesOrThrow();
		return outResources;
	}
	/**
	 * Returns secured resources summarized
	 * @param appCode
	 * @param appComponent
	 * @param lang
	 */
	@SuppressWarnings("unchecked")
	public Collection<S> findSummarizedBy(final AppCode appCode,final AppComponent appComponent,
										  final Language lang) {
		FindSummariesResult<R> find = this.getServiceProxyAs(FindServicesForAuthorizationTargetResource.class)
								 		  .findSummarizedBy(this.getSecurityContext(),
								 				  			appCode,appComponent,
								 				  			lang);
		Collection<S> outResources = find.getSummariesOrThrow();
		return outResources;
	}
}
