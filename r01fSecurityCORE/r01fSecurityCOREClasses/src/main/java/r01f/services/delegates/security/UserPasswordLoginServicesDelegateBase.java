package r01f.services.delegates.security;



import r01f.api.interfaces.security.login.entry.CRUDServicesForUserLoginEntry;
import r01f.api.interfaces.security.login.userpassword.CRUDServicesForUserPasswordLogin;
import r01f.api.interfaces.security.user.CRUDServicesForUser;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CRUDError;
import r01f.model.security.login.entry.UserLoginEntry;
import r01f.model.security.login.userpassword.UserPasswordLogin;
import r01f.model.security.user.User;
import r01f.model.services.COREServiceErrorType;
import r01f.model.services.COREServiceErrorTypes;
import r01f.patterns.Factory;
import r01f.services.core.ServicesCoreModuleConfig;

public class UserPasswordLoginServicesDelegateBase<U extends User,L extends UserPasswordLogin,
											       E extends UserLoginEntry> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final ServicesCoreModuleConfig _coreConfig;
	protected final EventBus _eventBus;
	
	protected final CRUDServicesForUser<U> _userCrud;
	protected final CRUDServicesForUserPasswordLogin<L> _userPasswordLoginCrud;
	
	protected final Factory<E> _userLoginEntryFactory;
	protected final CRUDServicesForUserLoginEntry<E> _userLoginEntryCrud;	
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	protected UserPasswordLoginServicesDelegateBase(final ServicesCoreModuleConfig coreCfg,
										             final EventBus eventBus,
										             // services
										            final CRUDServicesForUser<U> userCrud,
										            final CRUDServicesForUserPasswordLogin<L> userPasswordLoginCrud,
										            // used to create login entries
										            final Factory<E> loginEntryFactory,
										            final CRUDServicesForUserLoginEntry<E> userLoginEntryCrud) {
		_coreConfig = coreCfg;
		_eventBus = eventBus;
		
		_userCrud = userCrud;
		_userPasswordLoginCrud = userPasswordLoginCrud;
		
		_userLoginEntryFactory = loginEntryFactory;
		_userLoginEntryCrud = userLoginEntryCrud;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("rawtypes")
	protected static COREServiceErrorType _getOperationErrorResultFromCRUDResult(final CRUDError crudError) {
		if (crudError.wasBecauseAClientError()) {
			return COREServiceErrorTypes.BAD_CLIENT_REQUEST;
		} else if (crudError.wasBecauseClientRequestedEntityValidationErrors()) {
			return COREServiceErrorTypes.BAD_CLIENT_REQUEST;
		} else if (crudError.wasBecauseAnOptimisticLockingError()) {
			return COREServiceErrorTypes.SERVER_ERROR;
		} else if (crudError.wasBecauseAServerError()) {
			return COREServiceErrorTypes.SERVER_ERROR;
		} else if (crudError.wasBecauseClientCouldNotConnectToServer()) {
			return COREServiceErrorTypes.SERVER_ERROR;
		} else if (crudError.wasBecauseClientRequestedEntityWasInAnIllegalStatus()) {
			return COREServiceErrorTypes.SERVER_ERROR;
		}
		return COREServiceErrorTypes.UNKNOWN;
	}
}
