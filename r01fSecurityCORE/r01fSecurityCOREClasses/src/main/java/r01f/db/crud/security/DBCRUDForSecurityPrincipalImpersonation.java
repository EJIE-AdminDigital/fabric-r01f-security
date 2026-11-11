package r01f.db.crud.security;

import java.util.Collection;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.principal.impersonation.CRUDServicesForPrincipalImpersonation;
import r01f.db.entities.security.DBEntityForLoginBase;
import r01f.db.entities.security.DBEntityForSecurityPrincipalImpersonationBase;
import r01f.db.transfer.security.SecurityPrincipalImpersonationTransferToDB;
import r01f.db.transfer.security.TransformsDBEntityIntoPrincipalImpersonation;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.PersistenceRequestedOperation;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.impersonation.SecurityPrincipalImpersonation;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityOIDs.SecurityPrincipalImpersonationOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.core.persistence.db.DBCRUDForModelObjectBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;
import r01f.util.types.Strings;

@Slf4j
public abstract class DBCRUDForSecurityPrincipalImpersonation<DB extends DBEntityForSecurityPrincipalImpersonationBase>
	 		  extends DBCRUDForModelObjectBase<SecurityPrincipalImpersonationOID,SecurityPrincipalImpersonation,
	 								  		   DBPrimaryKeyForModelObject,DB>
           implements CRUDServicesForPrincipalImpersonation {

/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final SecurityPrincipalImpersonationTransferToDB<DB> _modelObjToDBEntity;
	private final TransformsDBEntityIntoPrincipalImpersonation<DB> _dbEntityToModelObj;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBCRUDForSecurityPrincipalImpersonation(final Class<DB> dbEntityType,
											   	   final Class<? extends DBEntityForLoginBase> dbLoginType,
											   	   final DBModuleConfig dbCfg,
											   	   final EntityManager entityManager,
											   	   final Marshaller marshaller) {
		super(SecurityPrincipalImpersonation.class,dbEntityType,
			  dbCfg,
			  entityManager,
			  marshaller);
		_modelObjToDBEntity = new SecurityPrincipalImpersonationTransferToDB<>();
		_dbEntityToModelObj = new TransformsDBEntityIntoPrincipalImpersonation<>();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	MODEL <TO> DB
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
												 final SecurityPrincipalImpersonation modelObj,final DB dbEntity) {
		_modelObjToDBEntity.setDBEntityFieldsFromModelObject(securityContext,
													  modelObj,dbEntity);
	}
	@Override
	public SecurityPrincipalImpersonation dbEntityToModelObject(final SecurityContext securityContext,
																final DB dbEntity) {
		return _dbEntityToModelObj.dbEntityToModelObject(securityContext,
														 dbEntity);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CRUDResult<SecurityPrincipalImpersonation> loadActiveForImpersonatedPrincipal(final SecurityContext securityContext,
												 	   						 			 final SecurityPrincipalOID impersonatedPrincipalOid) {
		String jpql = Strings.customized("SELECT dbEntry " +
									       "FROM {} dbEntry " +
									      "WHERE dbEntry._impersonatedPrincipalOid = :impersonatedPrincipalOid " +
									        "AND dbEntry._startedAt IS NOT NULL " +	// IMPERSONATION STARTED
									        "AND dbEntry._endedAt IS NULL ",		// IMPERSONATION NOT ENDED								         				   
									   _DBEntityType.getSimpleName());
		TypedQuery<DB> query = this.getEntityManager()
								   .createQuery(jpql,
												_DBEntityType)
								   .setParameter("impersonatedPrincipalOid",impersonatedPrincipalOid.asString());
		Collection<DB> loginEntries = query.getResultList();
		if (loginEntries.size() > 1) throw new IllegalStateException(Strings.customized("There are {} ACTIVE [impersonation] for the same impersonated [principal]={}!!!",
															 							loginEntries.size(),impersonatedPrincipalOid));
		return _crudResultForSingleEntity(securityContext,
										  PersistenceRequestedOperation.LOAD,
										  loginEntries,
										  "loadActiveForImpersonatedPrincipal(principalOid)",impersonatedPrincipalOid.asString());
	}
	@Override
	public CRUDResult<SecurityPrincipalImpersonation> loadActiveForImpersonatingPrincipal(final SecurityContext securityContext,
												 	   						  			  final SecurityPrincipalOID impersonatingPrincipalOid) {
		String jpql = Strings.customized("SELECT dbEntry " +
									       "FROM {} dbEntry " +
									      "WHERE dbEntry._impersonatingPrincipalOid = :impersonatingPrincipalOid " +
									        "AND dbEntry._startedAt IS NOT NULL " +	// IMPERSONATION STARTED
									        "AND dbEntry._endedAt IS NULL ",		// IMPERSONATION NOT ENDED								         				   
									   _DBEntityType.getSimpleName());
		TypedQuery<DB> query = this.getEntityManager()
								   .createQuery(jpql,
												_DBEntityType)
								   .setParameter("impersonatingPrincipalOid",impersonatingPrincipalOid.asString());
		Collection<DB> loginEntries = query.getResultList();
		if (loginEntries.size() > 1) throw new IllegalStateException(Strings.customized("There are {} ACTIVE [impersonation] for the same impersonating [principal]={}!!!",
															 							loginEntries.size(),impersonatingPrincipalOid));
		return _crudResultForSingleEntity(securityContext,
										  PersistenceRequestedOperation.LOAD,
										  loginEntries,
										  "loadActiveForImpersonatingPrincipal(principalOid)",impersonatingPrincipalOid.asString());
	}
}
