package r01f.db.crud.security.otp.persistence;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import r01f.api.interfaces.security.otp.persistence.CRUDServicesForOTPTask;
import r01f.db.entities.security.otp.persistence.DBEntityForOTPTask;
import r01f.db.transfer.security.otp.persistence.OTPTaskToDBEntityTransferDelegate;
import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.CRUDResultBuilder;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.model.security.otp.task.OTPTask;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.core.persistence.db.DBCRUDForModelObjectBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;
import r01f.services.security.otp.persistence.CoreOTPDBPersistenceService;
import r01f.types.contact.EMail;
import r01f.types.contact.Phone;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;

/**
 * Persistence layer
 */
public class DBCRUDForOTPTask
	 extends DBCRUDForModelObjectBase<OTPTaskOID,OTPTask,DBPrimaryKeyForModelObject,DBEntityForOTPTask>
  implements CRUDServicesForOTPTask,
  			 CoreOTPDBPersistenceService {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final OTPTaskToDBEntityTransferDelegate _transferDelegate;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBCRUDForOTPTask( final DBModuleConfig dbCfg,
						 	 final EntityManager entityManager,
						 	 final Marshaller marshaller) {
		super(OTPTask.class, DBEntityForOTPTask.class,
		      new OTPTaskToDBEntityTransferDelegate(),
		      dbCfg, entityManager, marshaller);
		_transferDelegate = new OTPTaskToDBEntityTransferDelegate();
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
//	API METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CRUDResult<OTPTask> create(final SecurityContext securityContext,
			                          final OTPTask task, long ttl, final TimeUnit timeUnit) {
		throw new IllegalStateException(" >>> Not implemented at this layer");
	}

	@Override
	public CRUDResult<OTPTask> loadByPhone(final SecurityContext securityContext,
										   final Phone phone) {
		TypedQuery<DBEntityForOTPTask> query = this.getEntityManager()
												   .createQuery(Strings.customized("SELECT entity " +
																					  "FROM {} entity " +
																						"WHERE entity._phone = :phone " +
																					  "ORDER BY entity._createDate DESC ",
																					_DBEntityType.getSimpleName()),
																					_DBEntityType)
												   .setParameter("phone", phone.asString());

		DBEntityForOTPTask dbEntity = (CollectionUtils.hasData(query.getResultList())) ? query.getResultList().get(0) : null;
		if (dbEntity == null) {
			return CRUDResultBuilder.using(securityContext)
										.on(_modelObjectType)
										.notLoaded()
										.becauseClientRequestedEntityWasNOTFound()
									.build();
		}
		return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.loaded()
									.dbEntity(dbEntity)
								.transformedToModelObjectUsing(_transferDelegate);
	}

	@Override
	public CRUDResult<OTPTask> loadByEmail(final SecurityContext securityContext,
										   final EMail email) {
		TypedQuery<DBEntityForOTPTask> query = this.getEntityManager()
												   .createQuery(Strings.customized("SELECT entity " +
																					  "FROM {} entity " +
																						"WHERE entity._email = :email " +
																					  "ORDER BY entity._createDate DESC ",
																					_DBEntityType.getSimpleName()),
																					_DBEntityType)
												   .setParameter("email", email.asString());

		DBEntityForOTPTask dbEntity = (CollectionUtils.hasData(query.getResultList())) ? query.getResultList().get(0) : null;
		if (dbEntity == null) {
			return CRUDResultBuilder.using(securityContext)
										.on(_modelObjectType)
										.notLoaded()
										.becauseClientRequestedEntityWasNOTFound()
									.build();
		} else if (dbEntity.getExpirationDate().isBefore(Instant.now())) {
			return CRUDResultBuilder.using(securityContext)
										.on(_modelObjectType)
										.notLoaded()
										.because(new Exception("OTP has expired"))
									.build();
		} else {
			return CRUDResultBuilder.using(securityContext)
										.on(_modelObjectType)
										.loaded()
										.dbEntity(dbEntity)
									.transformedToModelObjectUsing(_transferDelegate);
		}
	}

	@Override
	public CRUDResult<OTPTask> loadByEMail(final SecurityContext securityContext,
			                               final AppCode appCode, final AppComponent appComponent,
			                               final EMail eMail) {
		throw new IllegalStateException(" >>> Not implemented at this layer");
	}

	@Override
	public CRUDResult<OTPTask> loadByPhone(final SecurityContext securityContext,
			                               final AppCode appCode, final  AppComponent appComponent,
			                               final Phone phone) {
		throw new IllegalStateException(" >>> Not implemented at this layer");
	}
}

