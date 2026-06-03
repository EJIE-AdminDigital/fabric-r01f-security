package r01f.db.find.security;

import java.util.Collection;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.user.FindServicesForUser;
import r01f.db.entities.security.DBEntityForUserBase;
import r01f.model.persistence.FindResult;
import r01f.model.persistence.FindResultBuilder;
import r01f.model.security.user.User;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.persistence.db.DBFindForModelObjectBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;
import r01f.services.interfaces.FindServicesForModelObject;
import r01f.util.types.Strings;

/**
 * Persistence layer User finder.
 */
@Slf4j
public class DBFindForUser<U extends User,
						   DB extends DBEntityForUserBase>
	 extends DBFindForModelObjectBase<UserOID,U,
	 								  DBPrimaryKeyForModelObject,DB>
  implements FindServicesForModelObject<UserOID,U>,
  			 FindServicesForUser<U> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBFindForUser(final Class<U> modelObjectType,final Class<DB> dbEntityType,
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
	public FindResult<U> findAllUsers(final SecurityContext securityContext) {
		log.debug("> DBFindForUser.loading all users ");
		TypedQuery<DB> query = this.getEntityManager()
										.createQuery(Strings.customized("SELECT entity " +
																		  "FROM {} entity ",
																		_DBEntityType.getSimpleName()),
																		_DBEntityType);
		Collection<DB> entities = query.getResultList();
		FindResult<U> outEntities = FindResultBuilder.using(securityContext)
													 .on(_modelObjectType)
													 .foundDBEntities(entities)
													 .transformedToModelObjectsUsing(this);
		return outEntities;
	}
	@Override
	public FindResult<U> findUsersWithSystemWideRole(final SecurityContext securityContext) {
		log.debug("> DBFindForUser.loading all users with any system-wide role");
		TypedQuery<DB> query = this.getEntityManager()
										.createQuery(Strings.customized("SELECT entity " +
																		  "FROM {} entity " +
																		 "WHERE entity._hasSystemWideRole = true",	// has any system-wide role
																		_DBEntityType.getSimpleName()),
																		_DBEntityType);
		Collection<DB> entities = query.getResultList();
		FindResult<U> outEntities = FindResultBuilder.using(securityContext)
													 .on(_modelObjectType)
													 .foundDBEntities(entities)
													 .transformedToModelObjectsUsing(this);
		return outEntities;
	}
	@Override
	public FindResult<U> findByText(final SecurityContext securityContext,
									final String text) {
		log.debug("> DBFindForUser.loading users by text={}",text);
//		throw new UnsupportedOperationException("must be implemented by subtype");

		// this is in advance of when different modules exist
		// ... for now just return all users having [system-wide-role] no matter the module
		// when there exist many modules just create a boolean DB col for each module
		// ... when the user has a role in a module the corresponding DB col will be true
		log.debug("> DBFindForUser.loading users by text={}",text);

		CriteriaBuilder criteriaBuilder = this.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<DB> query = criteriaBuilder.createQuery(this.getDBEntityType());
		Root<DB> root = query.from(_DBEntityType);
		Predicate nameByPredicate = criteriaBuilder.like(criteriaBuilder.upper(root.<String>get("_name")),
											 			 "%" + text.toUpperCase() + "%");
		Predicate surnameByPredicate = criteriaBuilder.like(criteriaBuilder.upper(root.<String>get("_surname")),
											 				"%" + text.toUpperCase() + "%");

		Predicate nameOrSurnamePredicate = criteriaBuilder.or(nameByPredicate,
						   									  surnameByPredicate);
		query.where(nameOrSurnamePredicate);

		Collection<DB> dbEntities = this.getEntityManager()
										.createQuery(query)
										.setHint(QueryHints.READ_ONLY,HintValues.TRUE)
									    .getResultList();

		FindResult<U> outEntities = FindResultBuilder.using(securityContext)
													 .on(_modelObjectType)
													 .foundDBEntities(dbEntities)
													 .transformedToModelObjectsUsing(this);
		return outEntities;
	}
}
