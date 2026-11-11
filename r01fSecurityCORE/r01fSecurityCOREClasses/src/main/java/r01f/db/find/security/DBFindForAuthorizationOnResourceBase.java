package r01f.db.find.security;

import java.util.Collection;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.authorization.FindServicesForAuthorizationOnResource;
import r01f.db.entities.security.DBEntityForAuthorizationOnResourceBase;
import r01f.db.entities.security.DBEntityForAuthorizationTargetResourceBase;
import r01f.model.persistence.FindResult;
import r01f.model.security.authorization.SecurityPrincipalAuthorizationOnResource;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.oids.CommonOIDs.AppCode;
import r01f.oids.CommonOIDs.AppComponent;
import r01f.oids.SecurityOIDs.AuthorizationOnResourceOID;
import r01f.oids.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.core.persistence.db.DBFindForModelObjectBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;
import r01f.util.types.Strings;

/**
 * Persistence layer finder.
 */
@Slf4j
public abstract class DBFindForAuthorizationOnResourceBase<DBAO extends DBEntityForAuthorizationOnResourceBase,DBA extends DBEntityForAuthorizationTargetResourceBase>
	    	  extends DBFindForModelObjectBase<AuthorizationOnResourceOID,SecurityPrincipalAuthorizationOnResource,
	 								  		   DBPrimaryKeyForModelObject,DBAO>
  		   implements FindServicesForAuthorizationOnResource {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final Class<DBA> _dbAuthRes;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBFindForAuthorizationOnResourceBase(final Class<DBAO> dbAuthOnResType,
												final Class<DBA> dbAuthResType,
										  		final DBModuleConfig dbCfg,
										  		final EntityManager entityManager,
										  		final Marshaller marshaller) {
		super(SecurityPrincipalAuthorizationOnResource.class,dbAuthOnResType,
			  dbCfg,
			  entityManager,
			  marshaller);
		_dbAuthRes = dbAuthResType;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EXTENSION METHODS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public FindResult<SecurityPrincipalAuthorizationOnResource> findBy(final SecurityContext securityContext,
																	   final SecurityPrincipalOID principalOid) {
		log.debug("loading authorizations for principal={}",
				  principalOid);
		TypedQuery<DBAO> query = this.getEntityManager()
									 .createQuery(Strings.customized(_composeJpql(principalOid,
											 									 null),	// no resource filter
																	 _DBEntityType.getSimpleName()),
												_DBEntityType);
		query.setParameter("principalOid",principalOid.asString());

		Collection<DBAO> entities = query.getResultList();
		log.debug("[auth on resource find] > principal={} has {} [auths] on any resource",
				  entities.size(),
				  principalOid);
		return _buildResultsFromDBEntities(securityContext,
										   entities);	
	}
	@Override
	public FindResult<SecurityPrincipalAuthorizationOnResource> findBy(final SecurityContext securityContext,
																	   final SecurityPrincipalOID principalOid,
																	   final AppCode appCode,final AppComponent appComp) {
		log.debug("loading authorizations for principal={} within appCode/appComp={}/{}",
				  principalOid,
				  appCode,appComp);
		TypedQuery<DBAO> query = this.getEntityManager()
									 .createQuery(Strings.customized(_composeJpql(appCode,appComp),
											 						 _dbAuthRes.getSimpleName(),
																	 _DBEntityType.getSimpleName()),
												_DBEntityType);
		query.setParameter("principalOid",principalOid.asString());
		if (appCode != null) query.setParameter("appCode",appCode.asString());
		if (appComp != null) query.setParameter("appComp",appComp.asString());

		Collection<DBAO> dbEntities = query.getResultList();
		log.debug("[auth on resource find] > principal={} has {} [auths] on resources of appCode/appComponent={}/{}",
				  dbEntities.size(),
				  principalOid,
				  appCode,appComp);
		return _buildResultsFromDBEntities(securityContext,
										   dbEntities);
	}
	@Override
	public FindResult<SecurityPrincipalAuthorizationOnResource> findBy(final SecurityContext securityContext,
																	   final AuthorizationTargetResourceOID resOid) {
		log.debug("loading authorizations for resource={}",
				  resOid);
		TypedQuery<DBAO> query = this.getEntityManager()
									 .createQuery(Strings.customized(_composeJpql(null,		// no principal filter
											 									  resOid),
																	 _DBEntityType.getSimpleName()),
												_DBEntityType);
		query.setParameter("resOid",resOid.asString());

		Collection<DBAO> dbEntities = query.getResultList();
		return _buildResultsFromDBEntities(securityContext,
										   dbEntities);
	}
	@Override
	public FindResult<SecurityPrincipalAuthorizationOnResource> findBy(final SecurityContext securityContext,
																	   final SecurityPrincipalOID principalOid,
																	   final AuthorizationTargetResourceOID resOid) {
		log.debug("loading authorizations for principal={}",
				  principalOid);
		TypedQuery<DBAO> query = this.getEntityManager()
									 .createQuery(Strings.customized(_composeJpql(principalOid,
											 									  resOid),
																	 _DBEntityType.getSimpleName()),
												_DBEntityType);
		if (principalOid != null) query.setParameter("principalOid",principalOid.asString());
		if (resOid != null) query.setParameter("resOid",resOid.asString());

		Collection<DBAO> dbEntities = query.getResultList();
		return _buildResultsFromDBEntities(securityContext,
										   dbEntities);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	private static String _composeJpql(final SecurityPrincipalOID principalOid,
									   final AuthorizationTargetResourceOID resOid) {
		String jpql = "SELECT dbAuthzOnRes " +
						"FROM {} dbAuthzOnRes ";
		String principalPred = principalOid != null ? "dbAuthzOnRes._securityPrincipalOid = :principalOid" : null;
		String resPred = resOid != null ? "dbAuthzOnRes._resourceOid = :resOid" : null;
		
		if (principalPred != null || resPred != null) jpql = jpql + " WHERE ";
		if (principalPred != null) jpql = jpql + principalPred;	
		
		if (principalPred != null && resPred != null) {
			jpql = jpql + " AND " + resPred;
		} else if (principalPred == null && resPred != null) {
			jpql = jpql + resPred;
		}
		return jpql;
	}
	private static String _composeJpql(final AppCode appCode,final AppComponent appComp) {
		String jpql = "SELECT dbAuthzOnRes " +
						"FROM {} dbAuthRes " +
						"JOIN {} dbAuthzOnRes " +
					   "WHERE dbAuthRes._oid = dbAuthzOnRes._resourceOid " +
						 "AND dbAuthzOnRes._securityPrincipalOid = :principalOid";		// principal oid is mandatory!";
		String appCodePred = appCode != null ? "dbAuthRes._appCode = :appCode" : null;
		String appCompPred = appComp != null ? "dbAuthRes._appComponent = :appComp" : null;
		if (appCodePred != null) {
			jpql = jpql + " AND " + appCodePred;
		}
		if (appCompPred != null) {
			jpql = jpql + " AND " + appCompPred;
		}
		return jpql;
	}
}
