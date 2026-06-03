package r01f.db.crud.security;


import java.time.Instant;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import r01f.api.interfaces.security.login.apikey.CRUDServicesForApiKeyVersion;
import r01f.db.entities.security.DBEntityForApiKeyVersion;
import r01f.db.transfer.security.ApiKeyVersionTransferToDBBase;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.login.apikey.ApiKeyVersion;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.ApiKeyVersionOID;
import r01f.services.core.persistence.db.DBCRUDForModelObjectBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;
import r01f.types.AppVersion;
import r01f.util.types.Strings;


public abstract class DBCRUDForApiKeyVersion<DB extends DBEntityForApiKeyVersion>
	 		  extends DBCRUDForModelObjectBase<ApiKeyVersionOID,ApiKeyVersion,
	 		  								   DBPrimaryKeyForModelObject,DB>
  		   implements CRUDServicesForApiKeyVersion {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final ApiKeyVersionTransferToDBBase<DB> _transformer;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBCRUDForApiKeyVersion(final Class<DB> dbEntityType,
								  final DBModuleConfig dbCfg,
							   	  final EntityManager entityManager,
							   	  final Marshaller marshaller,
							   	  final ApiKeyVersionTransferToDBBase<DB> modelObjToDBEntity) {
		super(ApiKeyVersion.class,dbEntityType,
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
												 final ApiKeyVersion modelObj,final DB dbEntity) {
		_transformer.setDBEntityFieldsFromModelObject(securityContext,
													  modelObj,dbEntity);
	}
	@Override
	public ApiKeyVersion dbEntityToModelObject(final SecurityContext securityContext,
								   			   final DB dbEntity) {
		// use the descriptor to convert to a model object
		ApiKeyVersion out = super.dbEntityToModelObject(securityContext,
														dbEntity);
		return out;
	}
/////////////////////////////////////////////////////////////////////////////////////////
// EXTENSION METHOD
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CRUDResult<ApiKeyVersion> loadCurrent(final SecurityContext securityContext) {
		TypedQuery<DB> qry = _entityManager.createQuery(Strings.customized("SELECT v " +
						 													 "FROM {} v " +
						 													"WHERE (v._validFrom <= :now AND (v._validTo IS NULL OR v._validTo >= :now)) " +
						 													  "AND v._active = 1 " +
						 												 "ORDER BY v._createDate," +
						 													  	  "v._id DESC",
						 													_DBEntityType.getSimpleName()),
											    	    _DBEntityType)
										   .setParameter("now",Instant.now());
		qry.setHint(QueryHints.READ_ONLY,HintValues.TRUE);

		CRUDResult<ApiKeyVersion> outVersion = _crudResultForSingleEntity(securityContext,
																		  qry.getResultList(),
																		  "active","true");
		return outVersion;
	}
	@Override
	public CRUDResult<ApiKeyVersion> loadById(final SecurityContext securityContext,
										  	  final AppVersion versionId) {
		TypedQuery<DB> qry = _entityManager.createQuery(Strings.customized("SELECT v " +
						 												     "FROM {} v " +
																		    "WHERE v._id = :id",
																		   _DBEntityType.getSimpleName()),
														_DBEntityType)
										 	.setParameter("id",versionId.asString());
		qry.setHint(QueryHints.READ_ONLY,HintValues.TRUE);

		CRUDResult<ApiKeyVersion> outVersion = _crudResultForSingleEntity(securityContext,
																		  qry.getResultList(),
																		  "id",versionId.asString());
		return outVersion;
	}
}
