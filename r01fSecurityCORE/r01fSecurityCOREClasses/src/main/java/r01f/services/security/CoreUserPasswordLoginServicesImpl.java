package r01f.services.security;


import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.login.entry.CRUDServicesForUserLoginEntry;
import r01f.api.interfaces.security.login.userpassword.CRUDServicesForUserPasswordLogin;
import r01f.api.interfaces.security.login.userpassword.FindServicesForUserPasswordLogin;
import r01f.api.interfaces.security.login.userpassword.UserPasswordLoginServices;
import r01f.api.interfaces.security.user.CRUDServicesForUser;
import r01f.eventbus.EventBus;
import r01f.model.security.login.entry.UserLoginEntry;
import r01f.model.security.login.userpassword.UserPasswordLogin;
import r01f.model.security.login.userpassword.UserPasswordLoginRequest;
import r01f.model.security.login.userpassword.UserPasswordLoginResponse;
import r01f.model.security.login.userpassword.confirmation.UserPasswordLoginConfirmationRequest;
import r01f.model.security.login.userpassword.confirmation.UserPasswordLoginConfirmationResponse;
import r01f.model.security.login.userpassword.create.UserPasswordLoginCreateRequest;
import r01f.model.security.login.userpassword.create.UserPasswordLoginCreateResponse;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorValidationRequest;
import r01f.model.security.login.userpassword.recovery.UserPasswordLoginRecoveryRequest;
import r01f.model.security.login.userpassword.recovery.UserPasswordLoginRecoveryResponse;
import r01f.model.security.login.userpassword.reset.UserPasswordLoginResetRequest;
import r01f.model.security.login.userpassword.reset.UserPasswordLoginResetResponse;
import r01f.model.security.operation.SecurityOperationResult;
import r01f.model.security.user.User;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CorePersistenceServicesBase;
import r01f.types.contact.HasMailAddress;
import r01f.types.contact.HasPhoneNumber;

@Accessors(prefix="_")
public abstract class CoreUserPasswordLoginServicesImpl<U extends User & HasMailAddress & HasPhoneNumber,
														L extends UserPasswordLogin,
														E extends UserLoginEntry>
			  extends CorePersistenceServicesBase
		   implements UserPasswordLoginServices<U,L> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final CRUDServicesForUser<U> _crudForUser;
	protected final CRUDServicesForUserPasswordLogin<L> _crudForUserPasswordLogin;
	protected final CRUDServicesForUserLoginEntry<E> _crudForUserLoginEntry;
	protected final FindServicesForUserPasswordLogin<L> _findForUserPasswordLogin;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreUserPasswordLoginServicesImpl(final ServicesCoreModuleConfig coreCfg,
										 	 final Marshaller modelObjectsMarshaller,
										 	 final EventBus eventBus,
										 	 final Provider<EntityManager> entityManagerProvider,
										 	 final CRUDServicesForUser<U> crudForUser,
										 	 final CRUDServicesForUserPasswordLogin<L> crudForUserPasswordLogin,
										 	 final CRUDServicesForUserLoginEntry<E> crudForUserLoginEntry) {
		this(coreCfg, 
			 modelObjectsMarshaller, 
			 eventBus, 
			 entityManagerProvider, 
			 crudForUser, 
			 crudForUserPasswordLogin, 
			 crudForUserLoginEntry, 
			 null);
	}
	public CoreUserPasswordLoginServicesImpl(final ServicesCoreModuleConfig coreCfg,
										 	 final Marshaller modelObjectsMarshaller,
										 	 final EventBus eventBus,
										 	 final Provider<EntityManager> entityManagerProvider,
										 	 // security services
										 	 final CRUDServicesForUser<U> crudForUser,
										 	 final CRUDServicesForUserPasswordLogin<L> crudForUserPasswordLogin,
										 	 final FindServicesForUserPasswordLogin<L> findForUserPasswordLogin,
										 	 final CRUDServicesForUserLoginEntry<E> crudForUserLoginEntry) {
		super(coreCfg,
			  modelObjectsMarshaller,
			  eventBus,
			  entityManagerProvider);
		_crudForUser = crudForUser;
		_crudForUserPasswordLogin = crudForUserPasswordLogin;
		_findForUserPasswordLogin = findForUserPasswordLogin;
		_crudForUserLoginEntry = crudForUserLoginEntry;
	}
	@Deprecated
	public CoreUserPasswordLoginServicesImpl(final ServicesCoreModuleConfig coreCfg,
										 	 final Marshaller modelObjectsMarshaller,
										 	 final EventBus eventBus,
										 	 final Provider<EntityManager> entityManagerProvider,
										 	 // security services
										 	 final CRUDServicesForUser<U> crudForUser,
										 	 final CRUDServicesForUserPasswordLogin<L> crudForUserPasswordLogin,
										 	 final CRUDServicesForUserLoginEntry<E> crudForUserLoginEntry,
										 	 final FindServicesForUserPasswordLogin<L> findForUserPasswordLogin) {
		this(coreCfg,
			 modelObjectsMarshaller,
			 eventBus,
			 entityManagerProvider,
			 crudForUser,
			 crudForUserPasswordLogin,findForUserPasswordLogin,
			 crudForUserLoginEntry);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	REGISTER & CONFIRM
/////////////////////////////////////////////////////////////////////////////////////////
	@Transactional
	@Override @SuppressWarnings("unchecked")
	public SecurityOperationResult<UserPasswordLoginCreateResponse> createLogin(final SecurityContext securityContext,
																		  		final UserPasswordLoginCreateRequest<U> request) {
		return this.forSecurityContext(securityContext)
						.createDelegateAs(UserPasswordLoginServices.class)
						.createLogin(securityContext,
									  request);
	}
	@Transactional
	@Override @SuppressWarnings("unchecked")
	public SecurityOperationResult<UserPasswordLoginConfirmationResponse> confirmLogin(final SecurityContext securityContext,
																		 			   final UserPasswordLoginConfirmationRequest request) {
		return this.forSecurityContext(securityContext)
						.createDelegateAs(UserPasswordLoginServices.class)
						.confirmLogin(securityContext,
									 request);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LOGIN
/////////////////////////////////////////////////////////////////////////////////////////
	@Transactional
	@Override @SuppressWarnings("unchecked")
	public SecurityOperationResult<UserPasswordLoginResponse> login(final SecurityContext securityContext,
																	final UserPasswordLoginRequest loginRequest) {
		return this.forSecurityContext(securityContext)
						.createDelegateAs(UserPasswordLoginServices.class)
						.login(securityContext,
							   loginRequest);
	}
	@Transactional
	@Override @SuppressWarnings("unchecked")
	public SecurityOperationResult<UserPasswordLoginResponse> validate(final SecurityContext securityContext,
			                                                           final LoginAuthFactorValidationRequest loginValidationnRequest) {
		return this.forSecurityContext(securityContext)
					.createDelegateAs(UserPasswordLoginServices.class)
									.validate(securityContext,
											  loginValidationnRequest);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	RESET & RECOVER PASSWORD
/////////////////////////////////////////////////////////////////////////////////////////
	@Transactional
	@Override @SuppressWarnings("unchecked")
	public SecurityOperationResult<UserPasswordLoginResetResponse> resetPassword(final SecurityContext securityContext,
																	   			 final UserPasswordLoginResetRequest request) {
		return this.forSecurityContext(securityContext)
						.createDelegateAs(UserPasswordLoginServices.class)
						.resetPassword(securityContext,
											  request);
	}
	@Transactional
	@Override @SuppressWarnings("unchecked")
	public SecurityOperationResult<UserPasswordLoginRecoveryResponse> recoverPassword(final SecurityContext securityContext,
																		  			  final UserPasswordLoginRecoveryRequest request) {
		return this.forSecurityContext(securityContext)
						.createDelegateAs(UserPasswordLoginServices.class)
						.recoverPassword(securityContext,
												 request);
	}
	@Transactional
	@Override @SuppressWarnings("unchecked")
	public SecurityOperationResult<Boolean> notifyLoginRegistration(final SecurityContext securityContext,
																    final LoginID loginId) {
		return this.forSecurityContext(securityContext)
						.createDelegateAs(UserPasswordLoginServices.class)
						.notifyLoginRegistration(securityContext,
												 loginId);
	}
}
