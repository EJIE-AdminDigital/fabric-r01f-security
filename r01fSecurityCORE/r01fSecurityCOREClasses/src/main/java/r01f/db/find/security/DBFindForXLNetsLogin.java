package r01f.db.find.security;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import com.google.common.collect.Sets;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.xlnets.FindServicesForXLNetsLogin;
import r01f.db.entities.security.DBEntityForXLNetsLogin;
import r01f.db.search.security.DBSearchQueryForXLNetsLogin;
import r01f.guids.CommonOIDs.WorkPlaceCode;
import r01f.model.persistence.FindResult;
import r01f.model.persistence.FindResultBuilder;
import r01f.model.security.login.xlnets.SearchFilterForXLNetsLoginBase;
import r01f.model.security.login.xlnets.XLNetsLogin;
import r01f.model.security.login.xlnets.XLNetsLoginWorkPlaceCodeSummary;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.interfaces.FindServicesForModelObject;
import r01f.util.types.collections.CollectionUtils;

/**
 * Persistence layer xlnets finder.
 */
@Slf4j
public class DBFindForXLNetsLogin<L extends XLNetsLogin,
                                  F extends SearchFilterForXLNetsLoginBase<L,?>,
						   		  DB extends DBEntityForXLNetsLogin>
	 extends DBFindForLoginBase<L,DB>
  implements FindServicesForModelObject<LoginOID,L>,
  			 FindServicesForXLNetsLogin<L,F> {
/////////////////////////////////////////////////////////////////////////////////////////
// MEMBERS
/////////////////////////////////////////////////////////////////////////////////////////
	final DBSearchQueryForXLNetsLogin<F,DB> _searchQuery;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBFindForXLNetsLogin(final Class<L> modelObjectType,final Class<DB> dbEntityType,
								final DBModuleConfig dbCfg,
								final EntityManager entityManager,
								final Marshaller marshaller) {
		super(modelObjectType,dbEntityType,
			  dbCfg,
			  entityManager,
			  marshaller);
		_searchQuery = new DBSearchQueryForXLNetsLogin<>(dbEntityType, dbCfg, entityManager, null);
	}
/////////////////////////////////////////////////////////////////////////////////////////
// EXTENSION METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("deprecation")
	@Override
	public FindResult<XLNetsLoginWorkPlaceCodeSummary> findWorkplaceSummaries(final SecurityContext securityContext) {
		log.info("DBFindForXLNetsLogin-indWorkplaceSummaries () : find all");
		CriteriaQuery<Tuple> criteriaQuery = _entityManager.getCriteriaBuilder().createTupleQuery();
		Root<DB> root = criteriaQuery.from(_DBEntityType);
        ///////// compose the sel cols
		Set<String> theColNames = Sets.newLinkedHashSet();
		theColNames.add("_workPlace"); // .    the workplace ,fe EJXXXXXX,IEXXXXXX
		theColNames.add("_oid");       // ..   the login oid if needed,
		theColNames.add("_loginId");   //  ... the login ID,  f.e IOLABARO, ISGARAL
		theColNames.add("_userOid");
		//ONLY the given cols (see http://stackoverflow.com/questions/12618489/jpa-criteria-api-select-only-specific-columns)
		List<Selection<?>> multisel = theColNames.stream()
												 .map(colName -> {
															Selection<?> outSel = root.get(colName);
															if (outSel == null) throw new IllegalArgumentException(colName + " is NOT a valid selection at [db entity] of type " + _DBEntityType);
															outSel.alias(colName);
															return outSel;
													  })
												  .collect(Collectors.toList());
		criteriaQuery = criteriaQuery.multiselect(multisel);  //  ...
		Collection<Tuple> dbEntities = _entityManager.createQuery(criteriaQuery)
																.setHint(QueryHints.READ_ONLY,HintValues.TRUE)
															.getResultList();
		FindResult<XLNetsLoginWorkPlaceCodeSummary> outResult = null;
		if (CollectionUtils.hasData(dbEntities)) {
			Collection<XLNetsLoginWorkPlaceCodeSummary> modelObjs =
					dbEntities.stream()
							  .map(dbTuple -> {
								                XLNetsLoginWorkPlaceCodeSummary  summary = new XLNetsLoginWorkPlaceCodeSummary();
								               	String workplaceCodeAsString = dbTuple.get("_workPlace", String.class);
								             	String loginIdAsString = dbTuple.get("_loginId", String.class);
								             	String loginOIDAsString = dbTuple.get("_oid", String.class);
								            	String userOIDAsString = dbTuple.get("_userOid", String.class);

								                summary.setLoginId(LoginID.forId(loginIdAsString));
								                summary.setLoginOID(LoginOID.forId(loginOIDAsString));
								                summary.setUserOID(UserOID.forId(userOIDAsString));
								               	if ( workplaceCodeAsString != null) {
								               		summary.setWorkPlaceCode(WorkPlaceCode.forId(workplaceCodeAsString));
								               	}
								               	return summary;
							                   })
						   	. collect(Collectors.toList());
			// build result
			outResult = FindResultBuilder.using(securityContext)
										 .on(XLNetsLoginWorkPlaceCodeSummary.class)
										 .foundEntities(modelObjs);
		} else {
			outResult = FindResultBuilder.using(securityContext)
					 					  .on(XLNetsLoginWorkPlaceCodeSummary.class)
										 .noEntityFound();
		}
		return outResult;
	}
	@Override
	public FindResult<XLNetsLoginWorkPlaceCodeSummary> findWorkplaceSummaries(final SecurityContext securityContext,
			                                                                  final F filter) {
		log.info("[[ DBFindForXLNetsLogin-requests].findBy filter={}",
				filter != null ? filter.getBooleanQuery().encodeAsString() : null);

		// [0] - Get the entity manager
		// TODO needs some research... really must have to call clear??
		_entityManager.clear();	// see CorePersistenceServiceBase and http://stackoverflow.com/questions/9146239/auto-cleared-sessions-with-guice-persist
		// BEWARE that the EntityManagerProvider reuses EntityManager instances and those instances
		// could have cached entity instances... discard them all*/


		@SuppressWarnings("unchecked")
		TypedQuery<DB> qry = (TypedQuery<DB>) _searchQuery.getResultsQuery(filter);
		Collection<DB> dbEntities = qry.getResultList();
		log.debug("[ XLNetsLoginDataFilter-requests].findBy jpa query={}",
		qry.toString());
		if (CollectionUtils.hasData(dbEntities)) {
			log.info(" >> Results ? {} [elements]" , dbEntities.size());
		} else {
			log.warn(" >> No data found");
		}
		// [2] - Compose the results
		return FindResultBuilder.using(securityContext)
									 .on(XLNetsLoginWorkPlaceCodeSummary.class)
									 .foundDBEntities(dbEntities)
								 .transformedToModelObjectsUsing(db-> XLNetsLoginWorkPlaceCodeSummary.createFrom(dbEntityToModelObject(securityContext, db)));


	}
}
