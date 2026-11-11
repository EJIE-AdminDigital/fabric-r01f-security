package r01f.db.crud.security;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.principal.CRUDServicesForPrincipal;
import r01f.db.entities.security.DBEntityForSecurityPrincipalBase;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.PersistenceRequestedOperation;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.SecurityPrincipalID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.core.persistence.db.DBCRUDForModelObjectBase;
import r01f.services.core.persistence.db.TransfersModelObjectStateToDBEntity;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;
import r01f.types.contact.EMail;
import r01f.util.types.Strings;

@Slf4j
public abstract class DBCRUDForSecurityPrincipal<P extends IsSecurityPrincipal,
												 DB extends DBEntityForSecurityPrincipalBase>
			  extends DBCRUDForModelObjectBase<SecurityPrincipalOID,P,
			  								   DBPrimaryKeyForModelObject,
			  								   DB>
		   implements CRUDServicesForPrincipal<P> {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	protected final TransfersModelObjectStateToDBEntity<P,DB> _objToDBEntityTransformer;

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBCRUDForSecurityPrincipal(final Class<P> modelObjectType,final Class<DB> dbEntityType,
									  final DBModuleConfig dbCfg,
									  final EntityManager entityManager,
									  final Marshaller marshaller,
									  final TransfersModelObjectStateToDBEntity<P,DB> modelObjToDBEntity) {
		super(modelObjectType,dbEntityType,
			  dbCfg,
			  entityManager,
			  marshaller);
		_objToDBEntityTransformer = modelObjToDBEntity;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
												 final P modelObj,final DB dbEntity) {
		_objToDBEntityTransformer.setDBEntityFieldsFromModelObject(securityContext,
													  			   modelObj,dbEntity);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EXTENSION METHODS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public CRUDResult<P> loadBy(final SecurityContext securityContext,
								final IdentityProvider identityProvider,final SecurityPrincipalID principalId) {
		log.debug("loadByLoginId {}/{}", 
				  identityProvider,principalId);
		// [1] - Create the JPQL
		String jpql = Strings.customized("SELECT dbPrincipal " +
									       "FROM {} dbPrincipal " +
									      "WHERE dbPrincipal._sourceIdentityProvider = :identityProvider " + 
									        "AND dbPrincipal._id = :principalId ",
									   _DBEntityType.getSimpleName());
		
		// [2] - Do the query
		TypedQuery<DB> query = this.getEntityManager()
								   .createQuery(jpql,
												_DBEntityType)
								   .setParameter("principalId",principalId.asString())
								   .setParameter("identityProvider",identityProvider);

		return _crudResultForSingleEntity(securityContext,
										  PersistenceRequestedOperation.LOAD,
										  query.getResultList(),
										  "loadBy(identityProvider,principalId)",Strings.customized("{}/{}",
																				 				    identityProvider,principalId));
	}
	@Override
	public CRUDResult<P> loadBy(final SecurityContext securityContext,
								final EMail principalEmail) {
		throw new UnsupportedOperationException("loadBy(principalEmail) is not supported by default: implement it if the [principal] supports loading by email because every [principal] has an email and NO different [principals] can hava the same [email]");
	}
}
