package r01f.db.crud.security.webauthn;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.webauthn.CRUDServicesForAuthenticatorCredential;
import r01f.db.entities.security.webauthn.DBEntityForAuthenticatorCredential;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.PersistenceRequestedOperation;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.webauthn.AuthenticatorCredential;
import r01f.model.security.webauthn.WebAuthnIDS.CredentialID;
import r01f.model.security.webauthn.WebAuthnOIDs.CredentialOID;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.core.persistence.db.DBCRUDForModelObjectBase;
import r01f.services.core.persistence.db.TransfersModelObjectStateToDBEntity;
import r01f.services.core.persistence.db.TransformsDBEntityIntoModelObject;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;
import r01f.util.types.Strings;

@Slf4j
public  class DBCRUDForAuthenticatorCredential<C extends AuthenticatorCredential,
						  		               DB extends DBEntityForAuthenticatorCredential>
	   		  extends DBCRUDForModelObjectBase<CredentialOID,C,
	 								  		   DBPrimaryKeyForModelObject,DB>
  		   implements CRUDServicesForAuthenticatorCredential<C>,
  			   		  TransformsDBEntityIntoModelObject<DB,C> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final TransfersModelObjectStateToDBEntity<C,DB> _transformer;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBCRUDForAuthenticatorCredential(final Class<C> modelObjectType,final Class<DB> dbEntityType,
											final DBModuleConfig dbCfg,
										    final EntityManager entityManager,
											final Marshaller marshaller,
										    final TransfersModelObjectStateToDBEntity<C,DB> modelObjToDBEntity) {
		super(modelObjectType,dbEntityType,
			  dbCfg,
			  entityManager,
			  marshaller);
		_transformer = modelObjToDBEntity;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	MODEL <TO> DB
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
												 final C modelObj,final DB dbEntity) {
		_transformer.setDBEntityFieldsFromModelObject(securityContext,
													  modelObj,dbEntity);
	}
	@Override
	public C dbEntityToModelObject(final SecurityContext securityContext,
								   final DB dbEntity) {
		// use the descriptor to convert to a model object
		C out = super.dbEntityToModelObject(securityContext,
											dbEntity);
		return out;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override 	
	public CRUDResult<C> loadBy(final SecurityContext securityContext,
								final SecurityPrincipalOID principalOid) {
		log.warn("DBCRUDForAuthenticatorCredential ( {} ).loadBy securityPrincipalOid={} ",
				 _DBEntityType, 
				 principalOid.asString());
		TypedQuery<DB> query = this.getEntityManager()
									 .createQuery(Strings.customized("SELECT dbCredential " +
																	   "FROM {} dbCredential " +
																	  "WHERE dbCredential._securityPrincipalOid = :securityPrincipalOid ",
																	 _DBEntityType.getSimpleName()),
												_DBEntityType)
									 .setParameter("securityPrincipalOid",principalOid.asString());
		return _crudResultForSingleEntity(securityContext,
										  PersistenceRequestedOperation.LOAD,
								   		  query.getResultList());
	}

	@Override 	
	public CRUDResult<C> loadBy(final SecurityContext securityContext,
								final CredentialID login) {
		log.warn("DBCRUDForAuthenticatorCredential ( {} ).loadBy _credentialId={} ",
				                                                _DBEntityType, login.asString());
		// Do the query
		TypedQuery<DB> query = this.getEntityManager()
										.createQuery(Strings.customized("SELECT dbCredential " +
																		  "FROM {} dbCredential " +
																		 "WHERE dbCredential._credentialId = :credentialId",
																		_DBEntityType.getSimpleName()),
													 _DBEntityType)
									.setParameter("credentialId",login.asString());
		return _crudResultForSingleEntity(securityContext,
										  PersistenceRequestedOperation.LOAD,
								   		  query.getResultList());
	}

	@Override 	
	public CRUDResult<C> loadBy(final SecurityContext securityContext,
			                    final CredentialID id,final SecurityPrincipalOID securityPrincipalOid) {
		log.warn("\nDBCRUDForAuthenticatorCredential ( {} ).loadBy _credentialId={} / securityPrincipalOid ='{}' ",
                                                                _DBEntityType,
                                                                id.asString(),securityPrincipalOid);
		// Do the query
		@SuppressWarnings("resource")
		TypedQuery<DB> query = this.getEntityManager()
											.createQuery(Strings.customized("SELECT dbCredential " +
																			  "FROM {} dbCredential " +
																			 "WHERE dbCredential._credentialId = :credentialId",
																			 " AND  dbCredential._securityPrincipalOid = :securityPrincipalOid",
																			_DBEntityType.getSimpleName()),
														_DBEntityType)

											.setParameter("credentialId",id.asString())
										    .setParameter("securityPrincipalOid",securityPrincipalOid.asString());
		return _crudResultForSingleEntity(securityContext,
										  PersistenceRequestedOperation.LOAD,
								   		  query.getResultList());
	};
}
