package r01f.db.crud.security.otp.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import r01f.api.interfaces.security.otp.persistence.CRUDServicesForOTPTask;
import r01f.db.entities.security.otp.persistence.DBEntityForOTPTask;
import r01f.db.transfer.security.otp.persistence.OTPTaskFromDBEntityTransformer;
import r01f.db.transfer.security.otp.persistence.OTPTaskToDBEntityTransfer;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.PersistenceRequestedOperation;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.otp.OTPTask;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.objectstreamer.Marshaller;
import r01f.oids.CommonOIDs.AppCode;
import r01f.oids.CommonOIDs.AppComponent;
import r01f.services.core.persistence.db.DBCRUDForModelObjectBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;
import r01f.services.security.otp.persistence.IsCoreOTPDBPersistenceService;
import r01f.types.contact.IsContactMean;
import r01f.util.types.Strings;

/**
 * Persistence layer
 */
public class DBCRUDForOTPTask
	 extends DBCRUDForModelObjectBase<OTPTaskOID,OTPTask,
	 								  DBPrimaryKeyForModelObject,DBEntityForOTPTask>
  implements CRUDServicesForOTPTask,
  			 IsCoreOTPDBPersistenceService {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final OTPTaskToDBEntityTransfer _transferDelegate;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBCRUDForOTPTask(final DBModuleConfig dbCfg,
						 	final EntityManager entityManager,
						 	final Marshaller marshaller) {
		super(OTPTask.class,DBEntityForOTPTask.class,
		      new OTPTaskFromDBEntityTransformer(),				// from DB entity to model object
		      dbCfg,entityManager, marshaller);
		_transferDelegate = new OTPTaskToDBEntityTransfer();	// from model object to DB entity
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EXTENSION METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setDBEntityFieldsFromModelObject(final SecurityContext securityContext,
												 final OTPTask modelObj,
												 final DBEntityForOTPTask dbEntity) {
		_transferDelegate.setDBEntityFieldsFromModelObject(securityContext, modelObj, dbEntity);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LOAD
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public <C extends IsContactMean> CRUDResult<OTPTask> loadByContactMean(final SecurityContext securityContext, 
			                                	 						   final AppCode appCode,final AppComponent appComponent,
			                                	 						   final C contactMean) {
		String jpqlBase = "SELECT entity " +
							      "FROM {} entity " +
							     "WHERE entity._contactMean = :contactMean " +
							       "AND entity._contactMeanType = :contactMeanType ";
		if (appCode != null) jpqlBase += "AND entity._appCode = :appCode ";
		if (appComponent != null) jpqlBase += "AND entity._appComponent = :appComponent ";
		jpqlBase += "ORDER BY entity._createDate DESC ";
		
		String jpql = Strings.customized(jpqlBase,
										 _DBEntityType.getSimpleName());
		
		TypedQuery<DBEntityForOTPTask> query = this.getEntityManager()
												   .createQuery(jpql,
																_DBEntityType)
												   .setParameter("contactMean",contactMean.asString())
												   .setParameter("contactMeanType",contactMean.getType());
		if (appCode != null) query.setParameter("appCode",appCode.asString());
		if (appComponent != null) query.setParameter("appComponent",appComponent.asString());
		
		return _crudResultForSingleEntity(securityContext,
										  PersistenceRequestedOperation.LOAD,
										  query.getResultList());
	}
}

