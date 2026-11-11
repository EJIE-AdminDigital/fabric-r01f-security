package r01f.client.api.sub.security.authorization;

import java.util.Collection;

import jakarta.inject.Provider;
import r01f.api.interfaces.security.authorization.FindServicesForAuthorizationTargetResource;
import r01f.locale.Language;
import r01f.model.persistence.FindResult;
import r01f.model.persistence.FindSummariesResult;
import r01f.model.security.authorization.AuthorizationTargetResource;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.summaries.SummarizedAuthorizationTargetResource;
import r01f.objectstreamer.Marshaller;
import r01f.oids.CommonOIDs.AppCode;
import r01f.oids.CommonOIDs.AppComponent;
import r01f.oids.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.services.api.delegates.ClientAPIDelegateForModelObjectFindServices;
import r01f.services.api.delegates.ClientAPIModelObjectChangesTrack;

public class ClientAPIDelegateForAuthorizationTargetResourceFindServices
	 extends ClientAPIDelegateForModelObjectFindServices<AuthorizationTargetResourceOID,AuthorizationTargetResource> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForAuthorizationTargetResourceFindServices(final Provider<SecurityContext>  securityContextProvider,
													  				   final Marshaller marshaller,
													  				   final FindServicesForAuthorizationTargetResource findServicesProxy) {
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
	public Collection<AuthorizationTargetResource> findAllResources() {
		FindResult<AuthorizationTargetResource> find = this.getServiceProxyAs(FindServicesForAuthorizationTargetResource.class)
								 						   .findAllResources(this.getSecurityContext());
		Collection<AuthorizationTargetResource> outResources = find.getOrThrow();
		ClientAPIModelObjectChangesTrack.startTrackingChangesOnLoaded(outResources);
		return outResources;
	}
	/**
	 * Returns all app's secured resources
	 * @param appCode
	 * @param appComponent
	 * @return
	 */
	public Collection<AuthorizationTargetResource> findBy(final AppCode appCode,final AppComponent appComponent) {
		FindResult<AuthorizationTargetResource> find = this.getServiceProxyAs(FindServicesForAuthorizationTargetResource.class)
															 .findBy(this.getSecurityContext(),
																	 appCode,appComponent);
		Collection<AuthorizationTargetResource> outResources = find.getOrThrow();
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
	public Collection<SummarizedAuthorizationTargetResource> findAllResourcesSummarized(final Language lang) {
		FindSummariesResult<AuthorizationTargetResource> find = this.getServiceProxyAs(FindServicesForAuthorizationTargetResource.class)
															 		  .findAllResourcesSummarized(this.getSecurityContext(),
															 				  					  lang);
		Collection<SummarizedAuthorizationTargetResource> outResources = find.getSummariesOrThrow();
		return outResources;
	}
	/**
	 * Returns secured resources summarized
	 * @param appCode
	 * @param appComponent
	 * @param lang
	 */
	public Collection<SummarizedAuthorizationTargetResource> findSummarizedBy(final AppCode appCode,final AppComponent appComponent,
										  									  final Language lang) {
		FindSummariesResult<AuthorizationTargetResource> find = this.getServiceProxyAs(FindServicesForAuthorizationTargetResource.class)
															 	    .findSummarizedBy(this.getSecurityContext(),
															 				  		  appCode,appComponent,
															 				  		  lang);
		Collection<SummarizedAuthorizationTargetResource> outResources = find.getSummariesOrThrow();
		return outResources;
	}
}
