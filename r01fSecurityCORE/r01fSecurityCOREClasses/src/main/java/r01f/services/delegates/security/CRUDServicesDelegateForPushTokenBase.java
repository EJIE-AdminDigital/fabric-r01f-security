package r01f.services.delegates.security;


import jakarta.persistence.EntityManager;
import r01f.api.interfaces.security.pushtoken.CRUDServicesForPushToken;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.PersistenceRequestedOperation;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.pushtoken.IsPushDestinationID;
import r01f.model.security.pushtoken.IsPushToken;
import r01f.model.security.pushtoken.PushTokenOID;
import r01f.objectstreamer.Marshaller;
import r01f.oids.CommonOIDs.SystemID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.CRUDServicesForModelObjectDelegateBase;
import r01f.services.core.delegates.persistence.CompletesModelObjectBeforeCreateOrUpdate;
import r01f.services.core.delegates.persistence.ValidatesModelObjectBeforeCreateOrUpdate;
import r01f.validation.ObjectValidationResult;
import r01f.validation.ObjectValidationResultBuilder;
import r01f.validation.SelfValidates;
import r01f.validation.Validates;

public abstract class CRUDServicesDelegateForPushTokenBase<T extends IsPushToken>
	 		  extends CRUDServicesForModelObjectDelegateBase<PushTokenOID,T>
  		   implements CRUDServicesForPushToken<T>,
			 		  ValidatesModelObjectBeforeCreateOrUpdate<T>,
			 		  CompletesModelObjectBeforeCreateOrUpdate<T> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public CRUDServicesDelegateForPushTokenBase(final ServicesCoreModuleConfig cfg,
												final Class<T> modelObjType,
												final CRUDServicesForPushToken<T> dbCrud,
												final EntityManager entityManager,
												final Marshaller marshaller,
											    final EventBus eventBus) {
		super(cfg,
			  modelObjType,
			  dbCrud,
			  eventBus);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EXTENSION METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<T> loadBy(final SecurityContext securityContext,
								final SystemID srcSystemId,final IsPushDestinationID destId) {
		return this.getServiceImplAs(CRUDServicesForPushToken.class)
				   .loadBy(securityContext,
						   srcSystemId,destId);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	PARAMS VALIDATION ON CREATION / UPDATE
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public T completeModelObjBeforeCreateOrUpdate(final SecurityContext securityContext,
												  final PersistenceRequestedOperation requestedOp,
												  final T modelObj) {
		return modelObj;
	}
	@Override @SuppressWarnings("unchecked")
	public ObjectValidationResult<T> validateModelObjBeforeCreateOrUpdate(final SecurityContext securityContext,
																		  final PersistenceRequestedOperation requestedOp,
																		  final T entity) {
		ObjectValidationResult<T> outValid = null;

		// if the model object self-validates...
		if (entity instanceof SelfValidates) {
			outValid = ((SelfValidates<T>) entity).validate();
		} else if (entity instanceof Validates) {
			outValid = ((Validates<T>) entity).validate(entity);
		}
		// Validate that it does not exists another entity with the same id
		if (outValid == null && requestedOp.is(PersistenceRequestedOperation.CREATE)) {
			// TODO ...
		}
		// no errors... it's valid
		if (outValid == null) {
			outValid = ObjectValidationResultBuilder.on(entity).isValid();
		}
		return outValid;
	}
}
