package r01f.services.delegates.security;



import r01f.api.interfaces.security.user.CRUDServicesForUser;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.CRUDResultBuilder;
import r01f.model.persistence.PersistenceRequestedOperation;
import r01f.model.security.user.User;
import r01f.model.security.user.UserValidator;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.CRUDServicesForModelObjectDelegateBase;
import r01f.services.core.delegates.persistence.ValidatesModelObjectBeforeCreateOrUpdate;
import r01f.types.contact.EMail;
import r01f.types.contact.Phone;
import r01f.validation.ObjectValidationResult;

public abstract class CRUDServicesDelegateForUser<U extends User>
			  extends CRUDServicesForModelObjectDelegateBase<UserOID,U>
		   implements CRUDServicesForUser<U>,
		   			  ValidatesModelObjectBeforeCreateOrUpdate<U> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR   & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////

	public CRUDServicesDelegateForUser(final ServicesCoreModuleConfig coreCfg,
									   final Class<U> modelObjectType,
									   final CRUDServicesForUser<U> dbCrud,
									   final Marshaller marshaller,
									   final EventBus eventBus) {
		super(coreCfg,
			  modelObjectType,
			  dbCrud,
			  eventBus);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  EXTENSION METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<U> loadBy(final SecurityContext securityContext,
								final SecurityProviderID securityProviderId,final LoginID loginId) {
		if (securityProviderId == null || loginId == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientBadRequest("The securityProvider id and loginId MUST not be null")
									.build();
		}
		CRUDResult<U> outResult = this.getServiceImplAs(CRUDServicesForUser.class)
									  .loadBy(securityContext,
											  securityProviderId,loginId);
		return outResult;
	}
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<U> loadBy(final SecurityContext securityContext,
								final EMail email) {
		if (email == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientBadRequest("The email to load user MUST not be null")
									.build();
		}
		CRUDResult<U> outResult = this.getServiceImplAs(CRUDServicesForUser.class)
									  .loadBy(securityContext,
											  email);
		return outResult;
	}
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<U> loadBy(final SecurityContext securityContext,
								final Phone phone) {
		if (phone == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientBadRequest("The phone to load user MUST not be null")
									.build();
		}
		CRUDResult<U> outResult = this.getServiceImplAs(CRUDServicesForUser.class)
									  .loadBy(securityContext,
											  phone);
		return outResult;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	PARAMS VALIDATION ON CREATION / UPDATE
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public ObjectValidationResult<U> validateModelObjBeforeCreateOrUpdate(final SecurityContext securityContext,
																		  final PersistenceRequestedOperation requestedOp,
																		  final U owner) {
		// [1]: Do base validations
		ObjectValidationResult<U> outValid = new UserValidator<U>().validate(owner);

//		// [2]: Ensure there does NOT exist another owner with the same login id
//		if (outValid.isValid()
//		 && requestedOp.is(PersistenceRequestedOperation.CREATE)) {
//			CRUDResult<U> byUserCodeLoadResult = this.loadBy(securityContext,
//															 owner.getUserCode());
//			if (byUserCodeLoadResult.hasSucceeded()) {
//				return ObjectValidationResultBuilder.on(owner)
//													.notValidwithErrorCode(100)
//													.isNotValidBecause("There exists another api key owner with the same userCode={}",owner.getUserCode());
//			}
//			// Phone and Mail validations may be null in some UserData so dont do any validation
//		}
		// [99]: Return
		return outValid;
	}

}
