package r01f.db.find.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.authorization.FindServicesForAuthorizationTargetResource;
import r01f.db.entities.security.DBEntityForAuthorizationTargetResource;
import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.locale.Language;
import r01f.model.persistence.FindResult;
import r01f.model.persistence.FindResultBuilder;
import r01f.model.persistence.FindSummariesResult;
import r01f.model.persistence.FindSummariesResultBuilder;
import r01f.model.security.authorization.AuthorizationTargetResource;
import r01f.model.security.summaries.SummarizedAuthorizationResource;
import r01f.model.security.summaries.UserAuthWithRoleCount;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.services.core.persistence.db.DBFindForModelObjectBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;

/**
 * Persistence layer finder.
 */
@Slf4j
public abstract class DBFindForAuthorizationTargetResource<R extends AuthorizationTargetResource,S extends SummarizedAuthorizationResource<R>,
													 	   DB extends DBEntityForAuthorizationTargetResource>
	    	  extends DBFindForModelObjectBase<AuthorizationTargetResourceOID,R,
	 								  		   DBPrimaryKeyForModelObject,DB>
  		   implements FindServicesForAuthorizationTargetResource<R> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBFindForAuthorizationTargetResource(final Class<R> modelObjectType,final Class<DB> dbEntityType,
										  		final DBModuleConfig dbCfg,
										  		final EntityManager entityManager,
										  		final Marshaller marshaller) {
		super(modelObjectType,dbEntityType,
			  dbCfg,
			  entityManager,
			  marshaller);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public FindResult<R> findAllResources(final SecurityContext securityContext) {
		return this.findBy(securityContext,
						   null,null);
	}
	@Override
	public FindResult<R> findBy(final SecurityContext securityContext,
								final AppCode appCode,final AppComponent appComp) {
		log.debug("DBFindForAuthorizationTargetResource.loading authorization target resources by appCode={} appComponent={}",
				  appCode,appComp);
		Collection<DB> dbEntities = _findDBEntities(appCode,appComp);
		FindResult<R> outFind = FindResultBuilder.using(securityContext)
												 .on(_modelObjectType)
												 .foundDBEntities(dbEntities)
												 .transformedToModelObjectsUsing(this);
		return outFind;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SUMMARIZED
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public FindSummariesResult<R> findAllResourcesSummarized(final SecurityContext securityContext,
															 final Language lang) {
		return this.findSummarizedBy(securityContext,
									 null,null,
									 lang);
	}
	@Override
	public FindSummariesResult<R> findSummarizedBy(final SecurityContext securityContext,
												   final AppCode appCode,final AppComponent appComp,
												   final Language lang) {
		log.debug("DBFindForAuthorizationTargetResource.loading summarized authorization target resources by appCode={} appComponent={}",
				  appCode,appComp);
		Collection<S> outSummaries = null;

		// [1] - Find the entities
		Collection<DB> dbEntities = _findDBEntities(appCode,appComp);

		// [2] - summarize every db entity
		if (CollectionUtils.hasData(dbEntities)) {
			outSummaries = dbEntities.stream()
									 .map(dbEntity -> {
												// a) get the auth target resource oid
												AuthorizationTargetResourceOID authResOid = AuthorizationTargetResourceOID.forId(dbEntity.getOid());

												// b) count the number of users by every role
												Collection<UserAuthWithRoleCount> userAuthCountByRole = _countUserAuthsByRole(securityContext,
																															  authResOid);

												// c) summarize
												S outSummary = _createSummaryFrom(dbEntity,
																				  userAuthCountByRole,
																				  lang);
												return outSummary;
											})
									 .collect(Collectors.toList());
		}

		// [99] - Return
		return FindSummariesResultBuilder.using(securityContext)
										 .on(_modelObjectType)
										 .foundSummaries(outSummaries);
	}
	/**
	 * Creates a summary from the db entity and the number of users with auth for every role
	 * @param dbEntity
	 * @param userAuthCountByRole
	 * @param lang
	 * @return
	 */
	protected abstract S _createSummaryFrom(final DB dbEntity,
										    final Collection<UserAuthWithRoleCount> userAuthCountByRole,
										    final Language lang);
	/**
	 * Counts [user auths] by [user role] on a target [resource]
	 * @param securityContext
	 * @param authResOid
	 * @return
	 */
	protected abstract Collection<UserAuthWithRoleCount> _countUserAuthsByRole(final SecurityContext securityContext,
																			   final AuthorizationTargetResourceOID authResOid);
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	private Collection<DB> _findDBEntities(final AppCode appCode,final AppComponent appComp) {
		String jpql = "SELECT entity " +
					    "FROM {} entity ";
		String appCodePred = appCode != null ? "entity._appCode = :appCode" : null;
		String appCompPred = appComp != null ? "entity._appComponent = :appComp" : null;
		if (appCodePred != null && appCompPred != null) {
			jpql = jpql + " WHERE " + appCodePred + " AND " + appCompPred;
		} else if (appCodePred != null) {
			jpql = jpql + " WHERE " + appCodePred;
		} else if (appCompPred != null) {
			jpql = jpql + " WHERE " + appCompPred;
		}

		TypedQuery<DB> query = this.getEntityManager()
								   .createQuery(Strings.customized(jpql,
																   _DBEntityType.getSimpleName()),
												_DBEntityType)
									 .setHint(QueryHints.READ_ONLY,HintValues.TRUE);
		if (appCode != null) query.setParameter("appCode",appCode.asString());
		if (appComp != null) query.setParameter("appComp",appComp.asString());

		Collection<DB> entities = query.getResultList();
		return entities;
	}
}
