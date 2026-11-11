package r01f.db.find.security;

import java.util.Collection;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.authorization.FindServicesForAuthorizationTargetResource;
import r01f.db.entities.security.DBEntityForAuthorizationTargetResourceBase;
import r01f.locale.Language;
import r01f.model.persistence.FindResult;
import r01f.model.persistence.FindSummariesResult;
import r01f.model.security.authorization.AuthorizationTargetResource;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.summaries.AuthorizationsWithRoleCount;
import r01f.model.security.summaries.SummarizedAuthorizationTargetResource;
import r01f.objectstreamer.Marshaller;
import r01f.oids.CommonOIDs.AppCode;
import r01f.oids.CommonOIDs.AppComponent;
import r01f.oids.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.services.core.persistence.db.DBFindForModelObjectBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;
import r01f.util.types.Strings;

/**
 * Persistence layer finder.
 */
@Slf4j
public abstract class DBFindForAuthorizationTargetResourceBase<DB extends DBEntityForAuthorizationTargetResourceBase>
	    	  extends DBFindForModelObjectBase<AuthorizationTargetResourceOID,AuthorizationTargetResource,
	 								  		   DBPrimaryKeyForModelObject,DB>
  		   implements FindServicesForAuthorizationTargetResource {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBFindForAuthorizationTargetResourceBase(final Class<DB> dbEntityType,
											  		final DBModuleConfig dbCfg,
											  		final EntityManager entityManager,
											  		final Marshaller marshaller) {
		super(AuthorizationTargetResource.class,dbEntityType,
			  dbCfg,
			  entityManager,
			  marshaller);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public FindResult<AuthorizationTargetResource> findAllResources(final SecurityContext securityContext) {
		return this.findBy(securityContext,
						   null,null);
	}
	@Override
	public FindResult<AuthorizationTargetResource> findBy(final SecurityContext securityContext,
														  final AppCode appCode,final AppComponent appComp) {
		log.debug("loading authorization target resources by appCode={} appComponent={}",
				  appCode,appComp);
		Collection<DB> dbEntities = _findDBEntities(appCode,appComp);
		return _buildResultsFromDBEntities(securityContext,
										   dbEntities);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SUMMARIZED
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public FindSummariesResult<AuthorizationTargetResource> findAllResourcesSummarized(final SecurityContext securityContext,
															 						   final Language lang) {
		return this.findSummarizedBy(securityContext,
									 null,null,
									 lang);
	}
	@Override
	public FindSummariesResult<AuthorizationTargetResource> findSummarizedBy(final SecurityContext securityContext,
																		     final AppCode appCode,final AppComponent appComp,
																		     final Language lang) {
		log.debug("loading summarized authorization target resources by appCode={} appComponent={}",
				  appCode,appComp);
		
		// [1] - Find the entities
		Collection<DB> dbEntities = _findDBEntities(appCode,appComp);

		// [99] - Return
		return _buildSummariesResultFromDBEntities(securityContext,
												   dbEntities,
												   dbEntity -> {
														// a) get the auth target resource oid
														AuthorizationTargetResourceOID authResOid = AuthorizationTargetResourceOID.forId(dbEntity.getOid());
		
														// b) count the number of [principals] by every role
														Collection<AuthorizationsWithRoleCount> authCountByRole = _countAuthsByRole(securityContext,
																																	authResOid);
														// c) summarize
														SummarizedAuthorizationTargetResource outSummary = _createSummaryFrom(dbEntity,
																						  									  authCountByRole,
																						  									  lang);
														return outSummary;
												   });
	}
	/**
	 * Creates a summary from the db entity and the number of [principals] with auth for every role
	 * @param dbEntity
	 * @param authCountByRole
	 * @param lang
	 * @return
	 */
	protected abstract SummarizedAuthorizationTargetResource _createSummaryFrom(final DB dbEntity,
																			    final Collection<AuthorizationsWithRoleCount> authCountByRole,
																			    final Language lang);
	/**
	 * Counts [auths] by [role] on a target [resource]
	 * @param securityContext
	 * @param authResOid
	 * @return
	 */
	protected abstract Collection<AuthorizationsWithRoleCount> _countAuthsByRole(final SecurityContext securityContext,
																			   	 final AuthorizationTargetResourceOID authResOid);
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	private Collection<DB> _findDBEntities(final AppCode appCode,final AppComponent appComp) {
		String jpql = "SELECT dbAuthRes " +
					    "FROM {} dbAuthRes ";
		String appCodePred = appCode != null ? "dbAuthRes._appCode = :appCode" : null;
		String appCompPred = appComp != null ? "dbAuthRes._appComponent = :appComp" : null;
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
												_DBEntityType);
									// .setHint(QueryHints.READ_ONLY,HintValues.TRUE);
		if (appCode != null) query.setParameter("appCode",appCode.asString());
		if (appComp != null) query.setParameter("appComp",appComp.asString());

		Collection<DB> entities = query.getResultList();
		return entities;
	}
}
