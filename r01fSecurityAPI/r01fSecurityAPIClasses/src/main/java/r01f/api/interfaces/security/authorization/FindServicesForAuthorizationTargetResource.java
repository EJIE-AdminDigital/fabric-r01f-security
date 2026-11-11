package r01f.api.interfaces.security.authorization;

import r01f.api.interfaces.security.IsSecurityServiceInterface;
import r01f.locale.Language;
import r01f.model.persistence.FindResult;
import r01f.model.persistence.FindSummariesResult;
import r01f.model.security.authorization.AuthorizationTargetResource;
import r01f.model.security.context.SecurityContext;
import r01f.oids.CommonOIDs.AppCode;
import r01f.oids.CommonOIDs.AppComponent;
import r01f.oids.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.services.interfaces.FindServicesForModelObject;

public interface FindServicesForAuthorizationTargetResource
		 extends FindServicesForModelObject<AuthorizationTargetResourceOID,AuthorizationTargetResource>,
				 IsSecurityServiceInterface {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns all secured resources
	 * @param securityContext
	 * @return
	 */
	public FindResult<AuthorizationTargetResource> findAllResources(final SecurityContext securityContext);
	
	/**
	 * Returns all app's secured resources
	 * @param securityContext
	 * @param appCode
	 * @param appComponent
	 * @return
	 */
	public FindResult<AuthorizationTargetResource> findBy(final SecurityContext securityContext,
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
	public FindSummariesResult<AuthorizationTargetResource> findAllResourcesSummarized(final SecurityContext securityContext,
															 						   final Language lang);
	/**
	 * Returns secured resources summarized
	 * @param securityContext
	 * @param appCode
	 * @param appComponent
	 * @param lang
	 */
	public FindSummariesResult<AuthorizationTargetResource> findSummarizedBy(final SecurityContext securityContext,
																		     final AppCode appCode,final AppComponent appComponent,
																		     final Language lang);
}
