package r01f.db.count.security;

import java.util.Collection;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.authorization.CountServicesForUserAuthorizationOnResource;
import r01f.db.entities.security.DBEntityForUserAuthorizationOnResource;
import r01f.model.persistence.CountResult;
import r01f.model.persistence.CountResultBuilder;
import r01f.model.security.authorization.UserAuthorizationOnResource;
import r01f.model.security.summaries.UserAuthWithRoleCount;
import r01f.model.services.COREServiceMethod;
import r01f.model.services.COREServiceMethodExecOK;
import r01f.model.services.COREServiceMethodExecResult;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.UserRole;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.securitycontext.SecurityOIDs.UserAuthorizationOnResourceOID;
import r01f.services.core.persistence.db.DBCountForModelObjectBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;
import r01f.util.types.collections.CollectionUtils;

@Accessors(prefix="_")
public abstract class DBCountForUserAuthorizationOnResource<A extends UserAuthorizationOnResource,
														   DB extends DBEntityForUserAuthorizationOnResource>
			  extends DBCountForModelObjectBase<UserAuthorizationOnResourceOID,A,
											   DBPrimaryKeyForModelObject,DB>
		   implements CountServicesForUserAuthorizationOnResource<A> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBCountForUserAuthorizationOnResource(final Class<A> modelObjectType,final Class<DB> dbEntityType,
											     final DBModuleConfig dbCfg,
											     final EntityManager entityManager,
											     final Marshaller marshaller) {
		super(modelObjectType,dbEntityType,
			  dbCfg,
			  entityManager,
			  marshaller);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EXTENSION METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CountResult<A> countUserAuthsOnResource(final SecurityContext securityContext,
												   final AuthorizationTargetResourceOID resourceOid) {
		return this.countUserAuthsOnResource(securityContext,
											 resourceOid,
											 null);		// any role
	}
	@Override
	public CountResult<A> countUserAuthsOnResource(final SecurityContext securityContext,
												   final AuthorizationTargetResourceOID resourceOid,final UserRole userRole) {
		CriteriaBuilder criteriaBuilder = _entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<? extends DB> root = criteriaQuery.from(_DBEntityType);

		Predicate oidPredicate = resourceOid != null ? criteriaBuilder.equal(root.<String>get("_resourceOid"),
										 			   						 resourceOid.asString())
													 : null;
		Predicate userRolePredicate = userRole != null ? criteriaBuilder.equal(root.<String>get("_role"),
										 			   						   userRole.asString())
													   : null;

		criteriaQuery.select(criteriaBuilder.count(root));

		Collection<Predicate> preds = Lists.newArrayListWithExpectedSize(2);
		if (oidPredicate != null) preds.add(oidPredicate);
		if (userRolePredicate != null) preds.add(userRolePredicate);
		if (CollectionUtils.hasData(preds)) criteriaQuery.where(preds.toArray(new Predicate[preds.size()]));

		long countResult = _entityManager.createQuery(criteriaQuery)
										 .getSingleResult();
		return CountResultBuilder.using(securityContext)
								 .on(_modelObjectType)
								 .counted("authsOnResource(" + resourceOid + ")")
								 .resulting(countResult);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public COREServiceMethodExecResult<Collection<UserAuthWithRoleCount>> countUserAuthsOnResourceByRole(final SecurityContext securityContext,
																										 final AuthorizationTargetResourceOID resourceOid) {
		// compose query
		CriteriaBuilder criteriaBuilder = _entityManager.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
		Root<? extends DB> root = criteriaQuery.from(_DBEntityType);

		Predicate oidPredicate = resourceOid != null ? criteriaBuilder.equal(root.<String>get("_resourceOid"),
										 			   						 resourceOid.asString())
													 : null;

		criteriaQuery.multiselect(root.get("_role"),
								  criteriaBuilder.count(root));

		Collection<Predicate> preds = Lists.newArrayListWithExpectedSize(2);
		if (oidPredicate != null) preds.add(oidPredicate);
		if (CollectionUtils.hasData(preds)) criteriaQuery.where(preds.toArray(new Predicate[preds.size()]));
		criteriaQuery.groupBy(root.get("_role"));

		// execute query
		Collection<Object[]> resultList = _entityManager.createQuery(criteriaQuery)
				 										 .getResultList();

		// Return result
		Collection<UserAuthWithRoleCount> outResult = CollectionUtils.hasData(resultList)
															? resultList.stream()
						 												.map(tuple -> {
																				String role = (String)tuple[0];
																				Long count = (Long)tuple[1];
																				return new UserAuthWithRoleCount(UserRole.forId(role),
																												 count);
																			 })
						 												.collect(Collectors.toList())
					 										: null;
		return new COREServiceMethodExecOK<>(COREServiceMethod.named("countUserAuthsOnResourceByRole"),
					 												 outResult);
	}
}
