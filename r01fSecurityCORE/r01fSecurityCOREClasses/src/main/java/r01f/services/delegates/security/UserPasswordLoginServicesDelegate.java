package r01f.services.delegates.security;

import java.time.Instant;

import r01f.api.interfaces.security.login.entry.CRUDServicesForUserLoginEntry;
import r01f.api.interfaces.security.login.userpassword.CRUDServicesForUserPasswordLogin;
import r01f.api.interfaces.security.login.userpassword.FindServicesForUserPasswordLogin;
import r01f.api.interfaces.security.login.userpassword.UserPasswordLoginServices;
import r01f.api.interfaces.security.otp.OTPService;
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
import r01f.patterns.Factory;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.UserLoginEntryID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.ServicesDelegateBase;
import r01f.services.delegates.sub.security.UserPasswordLoginServicesSubDelegateForLogin;
import r01f.services.delegates.sub.security.UserPasswordLoginServicesSubDelegateForPasswordRecovery;
import r01f.services.delegates.sub.security.UserPasswordLoginServicesSubDelegateForPasswordReset;
import r01f.services.delegates.sub.security.UserPasswordLoginServicesSubDelegateForRegister;
import r01f.types.contact.HasMailAddress;
import r01f.types.contact.HasPhoneNumber;

public abstract class UserPasswordLoginServicesDelegate<U extends User  & HasMailAddress & HasPhoneNumber,
														L extends UserPasswordLogin,
														E extends UserLoginEntry>
			  extends ServicesDelegateBase
		   implements UserPasswordLoginServices<U,L> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final UserPasswordLoginServicesSubDelegateForRegister<U,L,E> _registerDelegate;
	private final UserPasswordLoginServicesSubDelegateForLogin<U,L,E> _loginDelegate;
	private final UserPasswordLoginServicesSubDelegateForPasswordRecovery<U,L,E> _passwordRecoveryDelegate;
	private final UserPasswordLoginServicesSubDelegateForPasswordReset<U,L,E> _passwordResetDelegate;
/////////////////////////////////////////////////////////////////////////////////////////
// 	CONSTRUCTOR   & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public UserPasswordLoginServicesDelegate(final ServicesCoreModuleConfig coreCfg,
											 final EventBus eventBus,
											 // object factory
											 final Factory<L> loginFactory,
											 final Factory<E> userLoginEntryFactory,
											 // services
											 final CRUDServicesForUser<U> userCrud,
											 final CRUDServicesForUserPasswordLogin<L> usrPwdLoginCrud,final FindServicesForUserPasswordLogin<L> usrPwdLoginFind,
											 final CRUDServicesForUserLoginEntry<E> loginEntryCrud) {
		this(coreCfg,
			 null,
			 eventBus,
			 loginFactory,userLoginEntryFactory,
			 userCrud,
			 usrPwdLoginCrud,usrPwdLoginFind,
			 loginEntryCrud,
			 null);
	}	
	
	public UserPasswordLoginServicesDelegate(final ServicesCoreModuleConfig coreCfg,
											 final Marshaller marshaller,
											 final EventBus eventBus,
											 // object factory
											 final Factory<L> loginFactory,
											 final Factory<E> userLoginEntryFactory,
											 // services
											 final CRUDServicesForUser<U> userCrud,
											 final CRUDServicesForUserPasswordLogin<L> usrPwdLoginCrud,final FindServicesForUserPasswordLogin<L> usrPwdLoginFind,
											 final CRUDServicesForUserLoginEntry<E> loginEntryCrud) {
	
		this(coreCfg,
			 marshaller,
			 eventBus,
			 loginFactory,userLoginEntryFactory,
			 userCrud,
			 usrPwdLoginCrud,usrPwdLoginFind,
			 loginEntryCrud,
			 null);
	}
	
	public UserPasswordLoginServicesDelegate(final ServicesCoreModuleConfig coreCfg,
											 final Marshaller marshaller,
											 final EventBus eventBus,
											 // object factory
											 final Factory<L> loginFactory,
											 final Factory<E> userLoginEntryFactory,
											 // services
											 final CRUDServicesForUser<U> userCrud,
											 final CRUDServicesForUserPasswordLogin<L> userPwdLoginCrud,final FindServicesForUserPasswordLogin<L> usrPwdLoginFind,
											 final CRUDServicesForUserLoginEntry<E> loginEntryCrud,
											 // otp service 
											 final OTPService otpService) {
		
		

		super(coreCfg,
			  eventBus);
		// Delegates
		_registerDelegate = new UserPasswordLoginServicesSubDelegateForRegister<>(coreCfg,
																					   eventBus,
																					   // factory
																					   loginFactory,userLoginEntryFactory,
																					   // services
																					   userCrud,
																					   userPwdLoginCrud,
																					   loginEntryCrud);
		_loginDelegate = new UserPasswordLoginServicesSubDelegateForLogin<>(coreCfg,
																			     eventBus,
																			     // factories
																			     userLoginEntryFactory,
																			     // services
																		   	     userCrud,
																		   	     userPwdLoginCrud,
																		   	     loginEntryCrud,
																		   	    // otp service
																		   	     otpService );
		
		_passwordRecoveryDelegate = new UserPasswordLoginServicesSubDelegateForPasswordRecovery<U,L,E>(coreCfg,
																			  				           eventBus,
																			  				         // factories
																			  				          userLoginEntryFactory,
																			  				         // services
																			  				          userCrud,
																			  				          userPwdLoginCrud,
																			  				          loginEntryCrud);
		_passwordResetDelegate = new UserPasswordLoginServicesSubDelegateForPasswordReset<U,L,E>(coreCfg,
																			  			         eventBus,
																			  			         // factories
																			  			         userLoginEntryFactory,
																			  			        // services
																			  			         userCrud,
																			  			         userPwdLoginCrud,
																			  			         loginEntryCrud);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	REGISTER & CONFIRM
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public SecurityOperationResult<UserPasswordLoginCreateResponse> createLogin(final SecurityContext securityContext,
																		  		final UserPasswordLoginCreateRequest<U> request) {
		return _registerDelegate.createLogin(securityContext,
											 request);
	}
	@Override
	public SecurityOperationResult<UserPasswordLoginConfirmationResponse> confirmLogin(final SecurityContext securityContext,
																		 			   final UserPasswordLoginConfirmationRequest request) {
		return _registerDelegate.confirmLogin(securityContext,
									 		  request);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LOGIN
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public SecurityOperationResult<UserPasswordLoginResponse> login(final SecurityContext securityContext,
																	final UserPasswordLoginRequest loginRequest) {
		return _loginDelegate.login(securityContext,
									loginRequest);
	}
	
	@Override
	public  SecurityOperationResult<UserPasswordLoginResponse> validate(final SecurityContext securityContext,
																		final LoginAuthFactorValidationRequest loginValidationRequest) {
		return _loginDelegate.validate(securityContext,
			                    	   loginValidationRequest);
	}
	
	
	protected abstract E _createUserLoginEntryFrom(final SecurityContext securityContext,
												   final UserOID userOid,final LoginID loginId,
												   final UserLoginEntryID id,
												   final Instant date);
/////////////////////////////////////////////////////////////////////////////////////////
//	RECOVER
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public SecurityOperationResult<UserPasswordLoginRecoveryResponse> recoverPassword(final SecurityContext securityContext,
																					  final UserPasswordLoginRecoveryRequest request) {
		return _passwordRecoveryDelegate.recoverPassword(securityContext,
														 request);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	RESET
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public SecurityOperationResult<UserPasswordLoginResetResponse> resetPassword(final SecurityContext securityContext,
																				 final UserPasswordLoginResetRequest request) {
		return _passwordResetDelegate.resetPassword(securityContext,
													request);
	}
	
	@Override
	public SecurityOperationResult<Boolean> notifyLoginRegistration(final SecurityContext securityContext,
																    final LoginID loginId) {
		return _registerDelegate.notifyLoginRegistration(securityContext, loginId);
	}
}
