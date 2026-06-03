package r01f.db.crud.security.webauthn;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.webauthn.CRUDServicesForAuthenticatorCredential;
import r01f.db.entities.security.webauthn.DBEntityForAuthenticatorCredential;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.CRUDResultBuilder;
import r01f.model.security.webauthn.AuthenticatorCredential;
import r01f.model.security.webauthn.WebAuthnIDS.CredentialID;
import r01f.model.security.webauthn.WebAuthnOIDs.CredentialOID;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.persistence.db.DBCRUDForModelObjectBase;
import r01f.services.core.persistence.db.TransfersModelObjectStateToDBEntity;
import r01f.services.core.persistence.db.TransformsDBEntityIntoModelObject;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;

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
	@Override 	@SuppressWarnings("resource")
	public CRUDResult<C> loadBy(final SecurityContext securityContext,
								final UserOID userOid) {
		log.warn("DBCRUDForAuthenticatorCredential ( {} ) .loadBy userOid={} ",
				                                             _DBEntityType, userOid.asString());
		TypedQuery<DB> query = this.getEntityManager()
									 .createQuery(Strings.customized("SELECT entity " +
																	   "FROM {} entity " +
																	  "WHERE entity._userOid = :userOid ",
																	 _DBEntityType.getSimpleName()),
												_DBEntityType)
									 .setParameter("userOid",userOid.asString());
		DB dbEntity = (CollectionUtils.hasData(query.getResultList()))? query.getResultList().get(0) : null;
		return _buildCRUDResultFor(securityContext,
								   dbEntity);
	}

	@Override 	@SuppressWarnings("resource")
	public CRUDResult<C> loadBy(final SecurityContext securityContext,
								final CredentialID login) {
		log.warn("DBCRUDForAuthenticatorCredential ( {} ) .loadBy _credentialId={} ",
				                                                _DBEntityType, login.asString());
		// Do the query
		TypedQuery<DB> query = this.getEntityManager()
										.createQuery(Strings.customized("SELECT entity " +
																		  "FROM {} entity " +
																		 "WHERE entity._credentialId = :credentialId",
																		_DBEntityType.getSimpleName()),
													 _DBEntityType)
									.setParameter("credentialId",login.asString());
		DB dbEntity = (CollectionUtils.hasData(query.getResultList()))? query.getResultList().get(0) : null;
		return _buildCRUDResultFor(securityContext,
								   dbEntity);
	}

	@Override 	@SuppressWarnings("resource")
	public CRUDResult<C> loadBy(final SecurityContext securityContext,
			                    final CredentialID id, final UserOID userOid) {
		log.warn("\nDBCRUDForAuthenticatorCredential ( {} ) .loadBy _credentialId={} / userOid ='{}' ",
                                                                _DBEntityType,
                                                                id.asString(),userOid);
		// Do the query
		@SuppressWarnings("resource")
		TypedQuery<DB> query = this.getEntityManager()
											.createQuery(Strings.customized("SELECT entity " +
																			  "FROM {} entity " +
																			 "WHERE entity._credentialId = :credentialId",
																			 " AND  entity._userOid = :userOid",
																			_DBEntityType.getSimpleName()),
														_DBEntityType)

											.setParameter("credentialId",id.asString())
										    .setParameter("userOid",userOid.asString());

		DB dbEntity = (CollectionUtils.hasData(query.getResultList()))? query.getResultList().get(0) : null;
		if ( dbEntity == null) {
			log.warn("..not found any");
		}
		return _buildCRUDResultFor(securityContext,
		                            dbEntity);
	};
///////////////////////////////////////////////////////////////////////////
// PROTECTED
///////////////////////////////////////////////////////////////////////////
	protected CRUDResult<C> _buildCRUDResultFor(final SecurityContext securityContext,
											  final DB dbEntity) {
		if (dbEntity == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientRequestedEntityWasNOTFound()
									.build();
		}
		CRUDResult<C> outCRUDResult = CRUDResultBuilder.using(securityContext)
												 .on(_modelObjectType)
												 .loaded()
												 .dbEntity(dbEntity)
												 .transformedToModelObjectUsing(this);
		return outCRUDResult;
	}
}
