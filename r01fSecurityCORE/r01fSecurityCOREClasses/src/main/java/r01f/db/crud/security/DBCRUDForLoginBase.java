package r01f.db.crud.security;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.db.entities.security.DBEntityForLoginBase;
import r01f.db.transfer.security.LoginTransferToDB;
import r01f.db.transfer.security.SecurityObjectTransferToDBBase;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.PersistenceRequestedOperation;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.Login;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityIDs.SecurityPrincipalID;
import r01f.oids.SecurityOIDs.LoginOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.core.persistence.db.DBCRUDForModelObjectBase;
import r01f.services.core.persistence.db.TransformsDBEntityIntoModelObject;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;
import r01f.util.types.Strings;

@Slf4j
public abstract class DBCRUDForLoginBase<DB extends DBEntityForLoginBase>
	   		  extends DBCRUDForModelObjectBase<LoginOID,Login,
	 								  		   DBPrimaryKeyForModelObject,DB>
  		   implements CRUDServicesForLogin,
  			   		  TransformsDBEntityIntoModelObject<DB,Login> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final SecurityObjectTransferToDBBase<Login,DB> _objToDBEntityTransformer;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBCRUDForLoginBase(final Class<DB> dbEntityType,
							  final DBModuleConfig dbCfg,
							  final EntityManager entityManager,
							  final Marshaller marshaller) {
		super(Login.class,dbEntityType,
			  dbCfg,
			  entityManager,
			  marshaller);
		_objToDBEntityTransformer = new LoginTransferToDB<>();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	MODEL <TO> DB
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
												 final Login modelObj,final DB dbEntity) {
		_objToDBEntityTransformer.setDBEntityFieldsFromModelObject(securityContext,
													  			   modelObj,dbEntity);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CRUDResult<Login> loadBy(final SecurityContext securityContext,
									final IdentityProvider identityProvider,final AuthenticationMethod authnMethod,final SecurityPrincipalOID principalOid) {
		log.debug("> loadBy principalOid={} from identityProvider={}",
				  principalOid,identityProvider);
		String jpql = Strings.customized("SELECT dbLogin " +
									       "FROM {} dbLogin " +
									      "WHERE dbLogin._securityPrincipalOid = :principalOid " +
									        "AND dbLogin._securityPrincipalSourceIdentityProvider = :identityProvider " +
									        "AND dbLogin._authenticationMethod = :authnMethod",
									     _DBEntityType.getSimpleName());
		
		TypedQuery<DB> query = this.getEntityManager()
								   .createQuery(jpql,
											    _DBEntityType)
								   .setParameter("principalOid",principalOid.asString())
								   .setParameter("identityProvider",identityProvider)
								   .setParameter("authnMethod",authnMethod);
		
		return _crudResultForSingleEntity(securityContext,
										  PersistenceRequestedOperation.LOAD,
										  query.getResultList());
	}
	@Override
	public CRUDResult<Login> loadBy(final SecurityContext securityContext,
									final IdentityProvider identityProvider,final AuthenticationMethod authnMethod,final SecurityPrincipalID principalId) {
		log.debug("> loadBy principalId={} from identityProvider={}",
				  principalId,identityProvider);
		String jpql = Strings.customized("SELECT dbLogin " +
									       "FROM {} dbLogin " +
									      "WHERE dbLogin._securityPrincipalId = :principalId " +
									        "AND dbLogin._securityPrincipalSourceIdentityProvider = :identityProvider " +
									        "AND dbLogin._authenticationMethod = :authnMethod",
									     _DBEntityType.getSimpleName());
		
		TypedQuery<DB> query = this.getEntityManager()
								   .createQuery(jpql,
											    _DBEntityType)
								   .setParameter("principalId",principalId.asString())
								   .setParameter("identityProvider",identityProvider)
								   .setParameter("authnMethod",authnMethod);
		
		return _crudResultForSingleEntity(securityContext,
										  PersistenceRequestedOperation.LOAD,
										  query.getResultList());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public CRUDResult<Login> loadBy(final SecurityContext securityContext,
									final IdentityProvider identityProvider,final AuthenticationMethod authnMethod,final LoginID login) {
		log.debug("loadBy loginId={} from identityProvider={}",
				  login,identityProvider);
		String jpql = Strings.customized("SELECT dbLogin " +
									       "FROM {} dbLogin " +
									      "WHERE dbLogin._id = :id " +
									        "AND dbLogin._authenticationMethod = :authnMethod " +
									        "AND dbLogin._securityPrincipalSourceIdentityProvider = :identityProvider",
									     _DBEntityType.getSimpleName());
		
		// Do the query
		TypedQuery<DB> query = this.getEntityManager()
								   .createQuery(jpql,
												_DBEntityType)
								   .setParameter("id",login.asString())
								   .setParameter("authnMethod",authnMethod)
								   .setParameter("identityProvider",identityProvider);
		
		return _crudResultForSingleEntity(securityContext,
										  PersistenceRequestedOperation.LOAD,
										  query.getResultList());
	}
}
