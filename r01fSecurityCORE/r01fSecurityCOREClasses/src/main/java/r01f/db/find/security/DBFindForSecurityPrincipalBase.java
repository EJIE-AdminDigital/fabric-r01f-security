package r01f.db.find.security;

import java.util.Collection;

//import org.eclipse.persistence.config.HintValues;
//import org.eclipse.persistence.config.QueryHints;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.principal.FindServicesForPrincipal;
import r01f.db.entities.security.DBEntityForSecurityPrincipalBase;
import r01f.model.persistence.FindResult;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.core.persistence.db.DBFindForModelObjectBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;
import r01f.util.types.Strings;

@Slf4j
public class DBFindForSecurityPrincipalBase<P extends IsSecurityPrincipal,
						   					DB extends DBEntityForSecurityPrincipalBase>
	 extends DBFindForModelObjectBase<SecurityPrincipalOID,P,
	 								  DBPrimaryKeyForModelObject,DB>
  implements FindServicesForPrincipal<P> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBFindForSecurityPrincipalBase(final Class<P> modelObjectType,final Class<DB> dbEntityType,
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
	public FindResult<P> findAllPrincipals(final SecurityContext securityContext) {
		log.debug("> loading all");
		TypedQuery<DB> query = this.getEntityManager()
										.createQuery(Strings.customized("SELECT dbPrincipal " +
																		  "FROM {} dbPrincipal ",
																		_DBEntityType.getSimpleName()),
																		_DBEntityType);
		Collection<DB> dbEntities = query.getResultList();
		return _buildResultsFromDBEntities(securityContext,
										   dbEntities);		
	}
	@Override
	public FindResult<P> findHavingAnySystemWideRole(final SecurityContext securityContext) {
		log.debug("> loading all principals with any system-wide role");
		TypedQuery<DB> query = this.getEntityManager()
										.createQuery(Strings.customized("SELECT dbPrincipal " +
																		  "FROM {} dbPrincipal " +
																		 "WHERE entity._systemWideRoles IS NOT NULL",	// has any system-wide role
																		_DBEntityType.getSimpleName()),
																		_DBEntityType);
		Collection<DB> dbEntities = query.getResultList();
		return _buildResultsFromDBEntities(securityContext,
										   dbEntities);	
	}
	@Override
	public FindResult<P> findByText(final SecurityContext securityContext,
									final String text) {
		log.debug("> loading by text={}",
				  text);
		// this is in advance of when different modules exist
		// ... for now just return all [principals] having [system-wide-role] no matter the module
		// when there exist many modules just create a boolean DB col for each module
		// ... when the [principal] has a role in a module the corresponding DB col will be true

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
										//.setHint(QueryHints.READ_ONLY,HintValues.TRUE)
									    .getResultList();

		return _buildResultsFromDBEntities(securityContext,
										   dbEntities);	
	}
}
