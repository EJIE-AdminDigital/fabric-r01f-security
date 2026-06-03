package r01f.db.find.security;

import java.util.Collection;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.authorization.FindServicesForUserAuthorizationOnResource;
import r01f.db.entities.security.DBEntityForAuthorizationTargetResource;
import r01f.db.entities.security.DBEntityForUserAuthorizationOnResource;
import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.model.persistence.FindResult;
import r01f.model.persistence.FindResultBuilder;
import r01f.model.security.authorization.UserAuthorizationOnResource;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.securitycontext.SecurityOIDs.UserAuthorizationOnResourceOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.persistence.db.DBFindForModelObjectBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;
import r01f.util.types.Strings;

/**
 * Persistence layer finder.
 */
@Slf4j
public abstract class DBFindForUserAuthorizationOnResource<A extends UserAuthorizationOnResource,
													 	   DBAO extends DBEntityForUserAuthorizationOnResource,DBA extends DBEntityForAuthorizationTargetResource>
	    	  extends DBFindForModelObjectBase<UserAuthorizationOnResourceOID,A,
	 								  		   DBPrimaryKeyForModelObject,DBAO>
  		   implements FindServicesForUserAuthorizationOnResource<A> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final Class<DBA> _dbEntityTypeForAuthRes;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBFindForUserAuthorizationOnResource(final Class<A> modelObjectType,final Class<DBAO> dbEntityTypeForUserAuthOnRes,final Class<DBA> dbEntityTypeForAuthRes,
										  		final DBModuleConfig dbCfg,
										  		final EntityManager entityManager,
										  		final Marshaller marshaller) {
		super(modelObjectType,dbEntityTypeForUserAuthOnRes,
			  dbCfg,
			  entityManager,
			  marshaller);
		_dbEntityTypeForAuthRes = dbEntityTypeForAuthRes;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EXTENSION METHODS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public FindResult<A> findBy(final SecurityContext securityContext,
								final UserOID userOid) {
		log.debug("DBFindForUserAuthorizationOnResource.loading authorizations for user oid={}",
				  userOid);
		TypedQuery<DBAO> query = this.getEntityManager()
									 .createQuery(Strings.customized(_composeJpql(userOid,null),
																	 _DBEntityType.getSimpleName()),
												_DBEntityType);
		query.setParameter("userOid",userOid.asString());

		Collection<DBAO> entities = query.getResultList();
		FindResult<A> outEntities = FindResultBuilder.using(securityContext)
													 .on(_modelObjectType)
													 .foundDBEntities(entities)
													 .transformedToModelObjectsUsing(this);
		log.debug("[user auth on resource find] > user={} has {} [auths] on any resource",
				  userOid);
		return outEntities;
	}
	@Override
	public FindResult<A> findBy(final SecurityContext securityContext,
								final UserOID userOid,
								final AppCode appCode,final AppComponent appComp) {
		log.debug("DBFindForUserAuthorizationOnResource.loading authorizations for user oid={} within appCode/appComp={}/{}",
				  userOid,
				  appCode,appComp);
		TypedQuery<DBAO> query = this.getEntityManager()
									 .createQuery(Strings.customized(_composeJpql(appCode,appComp),
											 						 _dbEntityTypeForAuthRes.getSimpleName(),
																	 _DBEntityType.getSimpleName()),
												_DBEntityType);
		query.setParameter("userOid",userOid.asString());
		if (appCode != null) query.setParameter("appCode",appCode.asString());
		if (appComp != null) query.setParameter("appComp",appComp.asString());

		Collection<DBAO> entities = query.getResultList();
		FindResult<A> outEntities = FindResultBuilder.using(securityContext)
													 .on(_modelObjectType)
													 .foundDBEntities(entities)
													 .transformedToModelObjectsUsing(this);
		log.debug("[user auth on resource find] > user={} has {} [auths] on resources of appCode/appComponent={}/{}",
				  userOid,
				  appCode,appComp);
		return outEntities;
	}
	@Override
	public FindResult<A> findBy(final SecurityContext securityContext,
								final AuthorizationTargetResourceOID resOid) {
		log.debug("DBFindForUserAuthorizationOnResource.loading authorizations for resource oid={}",
				  resOid);
		TypedQuery<DBAO> query = this.getEntityManager()
									 .createQuery(Strings.customized(_composeJpql(null,resOid),
																	 _DBEntityType.getSimpleName()),
												_DBEntityType);
		query.setParameter("resOid",resOid.asString());

		Collection<DBAO> entities = query.getResultList();
		FindResult<A> outEntities = FindResultBuilder.using(securityContext)
													 .on(_modelObjectType)
													 .foundDBEntities(entities)
													 .transformedToModelObjectsUsing(this);
		return outEntities;
	}
	@Override
	public FindResult<A> findBy(final SecurityContext securityContext,
								final UserOID userOid,final AuthorizationTargetResourceOID resOid) {
		log.debug("DBFindForUserAuthorizationOnResource.loading authorizations for user oid={}",
				  userOid);
		TypedQuery<DBAO> query = this.getEntityManager()
									 .createQuery(Strings.customized(_composeJpql(userOid,resOid),
																	 _DBEntityType.getSimpleName()),
												_DBEntityType);
		if (userOid != null) query.setParameter("userOid",userOid.asString());
		if (resOid != null) query.setParameter("resOid",resOid.asString());

		Collection<DBAO> entities = query.getResultList();
		FindResult<A> outEntities = FindResultBuilder.using(securityContext)
													 .on(_modelObjectType)
													 .foundDBEntities(entities)
													 .transformedToModelObjectsUsing(this);
		return outEntities;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	private static String _composeJpql(final UserOID userOid,
									   final AuthorizationTargetResourceOID resOid) {
		String jpql = "SELECT entity " +
						"FROM {} entity ";
		String userPred = userOid != null ? "entity._userOid = :userOid" : null;
		String resPred = resOid != null ? "entity._resourceOid = :resOid" : null;
		if (userPred != null || resPred != null) jpql = jpql + " WHERE ";
		if (userPred != null) jpql = jpql + userPred;
		if (userPred != null && resPred != null) {
			jpql = jpql + " AND " + resPred;
		} else if (userPred == null && resPred != null) {
			jpql = jpql + resPred;
		}
		return jpql;
	}
	private static String _composeJpql(final AppCode appCode,final AppComponent appComp) {
		String jpql = "SELECT authOnRes " +
						"FROM {} auth " +
						"JOIN {} authOnRes " +
					   "WHERE auth._oid = authOnRes._resourceOid " +
						 "AND authOnRes._userOid = :userOid";		// user oid is mandatory!";
		String appCodePred = appCode != null ? "auth._appCode = :appCode" : null;
		String appCompPred = appComp != null ? "auth._appComponent = :appComp" : null;
		if (appCodePred != null) {
			jpql = jpql + " AND " + appCodePred;
		}
		if (appCompPred != null) {
			jpql = jpql + " AND " + appCompPred;
		}
		return jpql;
	}
}
