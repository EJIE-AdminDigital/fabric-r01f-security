package r01f.api.interfaces.security.authorization;

import r01f.api.interfaces.security.SecurityServiceInterface;
import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.locale.Language;
import r01f.model.persistence.FindResult;
import r01f.model.persistence.FindSummariesResult;
import r01f.model.security.authorization.AuthorizationTargetResource;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.services.interfaces.FindServicesForModelObject;

public interface FindServicesForAuthorizationTargetResource<R extends AuthorizationTargetResource>
		 extends FindServicesForModelObject<AuthorizationTargetResourceOID,R>,
				 SecurityServiceInterface {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns all secured resources
	 * @param securityContext
	 * @return
	 */
	public FindResult<R> findAllResources(final SecurityContext securityContext);
	
	/**
	 * Returns all app's secured resources
	 * @param securityContext
	 * @param appCode
	 * @param appComponent
	 * @return
	 */
	public FindResult<R> findBy(final SecurityContext securityContext,
								final AppCode appCode,final AppComponent appComponent);
/////////////////////////////////////////////////////////////////////////////////////////
//	SUMMARIZED	
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns all secured resources summarized
	 * @param securityContext
	 * @param lang
	 * @return
	 */
	public FindSummariesResult<R> findAllResourcesSummarized(final SecurityContext securityContext,
															 final Language lang);
	/**
	 * Returns secured resources summarized
	 * @param securityContext
	 * @param appCode
	 * @param appComponent
	 * @param lang
	 */
	public FindSummariesResult<R> findSummarizedBy(final SecurityContext securityContext,
												   final AppCode appCode,final AppComponent appComponent,
												   final Language lang);
}
