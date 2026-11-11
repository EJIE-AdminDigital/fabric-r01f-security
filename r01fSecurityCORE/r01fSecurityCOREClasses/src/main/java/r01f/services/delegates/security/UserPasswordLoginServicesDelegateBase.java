package r01f.services.delegates.security;



import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.api.interfaces.security.login.UserPasswordLoginServices;
import r01f.api.interfaces.security.login.entry.CRUDServicesForLoginEntry;
import r01f.api.interfaces.security.otp.OTPService;
import r01f.api.interfaces.security.principal.CRUDServicesForPrincipal;
import r01f.db.crud.security.DBCRUDForLoginBase;
import r01f.eventbus.EventBus;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.Login;
import r01f.model.security.login.authn.secondfactor.messages.validation.IsLoginAuthnSecondFactorValidationRequest;
import r01f.model.security.login.userpassword.messages.login.UserPasswordLoginRequest;
import r01f.model.security.login.userpassword.messages.login.UserPasswordLoginResponse;
import r01f.model.security.login.userpassword.messages.password.change.UserPasswordChangeRequest;
import r01f.model.security.login.userpassword.messages.password.change.UserPasswordChangeResponse;
import r01f.model.security.login.userpassword.messages.password.recover.UserPasswordRecoverRequest;
import r01f.model.security.login.userpassword.messages.password.recover.UserPasswordRecoverResponse;
import r01f.model.security.login.userpassword.messages.password.reset.UserPasswordResetRequest;
import r01f.model.security.login.userpassword.messages.password.reset.UserPasswordResetResponse;
import r01f.model.security.login.userpassword.messages.registration.acknowledge.UserPasswordLoginRegistrationAcknowledgeRequest;
import r01f.model.security.login.userpassword.messages.registration.acknowledge.UserPasswordLoginRegistrationAcknowledgeResponse;
import r01f.model.security.operation.IsSecurityOperationResult;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityOIDs.LoginOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.PersistenceServicesDelegateForModelObjectBase;
import r01f.services.delegates.security.userpassword.UserPasswordLoginServicesSubDelegateForLogin;
import r01f.services.delegates.security.userpassword.UserPasswordLoginServicesSubDelegateForPasswordRecovery;
import r01f.services.delegates.security.userpassword.UserPasswordLoginServicesSubDelegateForPasswordReset;
import r01f.services.delegates.security.userpassword.UserPasswordLoginServicesSubDelegateForRegister;
import r01f.services.delegates.security.userpassword.secondfactor.IsLoginAuthnSecondFactorHandler;
import r01f.services.delegates.security.userpassword.secondfactor.LoginAuthnSecondFactorHandlerDefaultImpl;

@Slf4j
public abstract class UserPasswordLoginServicesDelegateBase<P extends IsSecurityPrincipal>
			  extends PersistenceServicesDelegateForModelObjectBase<LoginOID,Login>
		   implements UserPasswordLoginServices {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final UserPasswordLoginServicesSubDelegateForLogin<P> _loginSubDelegate;
	private final UserPasswordLoginServicesSubDelegateForPasswordReset<P> _passwordResetSubDelegate;
	private final UserPasswordLoginServicesSubDelegateForPasswordRecovery<P> _passwordRecoverySubDelegate;
	private final UserPasswordLoginServicesSubDelegateForRegister<P> _registerSubDelegate;
/////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTOR   & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public UserPasswordLoginServicesDelegateBase(final ServicesCoreModuleConfig coreCfg,
												 final Marshaller marshaller,
												 final EventBus eventBus,
												 // login authn second factor handler
											     final IsLoginAuthnSecondFactorHandler<P> loginAuthnSecondFactorHandler) {
		super(coreCfg,
			  Login.class,
			  null, 	// no CRUD services for Login
			  eventBus);
		
		// BEWARE!!! Use the DAO layer (the deepest one) since if the service layer was used (as usual),
		//			 the password / password hash will NOT be present at the returned object 
		//			 because the password is REMOVED at the CRUD delegate layer
		CRUDServicesForLogin loginCRUD = _createDBCRUDServicesForLogin();
		CRUDServicesForLoginEntry loginEntryCRUD = _createCRUDServicesForLoginEntry();
		CRUDServicesForPrincipal<P> principalCRUD = _createCRUDServicesForPrincipal();
		
		_loginSubDelegate = new UserPasswordLoginServicesSubDelegateForLogin<>(coreCfg,
																			   eventBus,
																			   // services
																			   principalCRUD,
																			   loginCRUD,loginEntryCRUD,
																			   // login authn second factor handler
																			   loginAuthnSecondFactorHandler);
		_passwordResetSubDelegate = new UserPasswordLoginServicesSubDelegateForPasswordReset<>(coreCfg,
																							   // services
																							   principalCRUD,
																							   loginCRUD,loginEntryCRUD);
		_passwordRecoverySubDelegate = new UserPasswordLoginServicesSubDelegateForPasswordRecovery<>(coreCfg,
																									 eventBus,
																									 // services
																									 principalCRUD,
																									 loginCRUD,loginEntryCRUD);
		_registerSubDelegate = new UserPasswordLoginServicesSubDelegateForRegister<>(coreCfg,
																				     eventBus,
																					 // services
																					 principalCRUD,
																					 loginCRUD,loginEntryCRUD);
	}
	public UserPasswordLoginServicesDelegateBase(final ServicesCoreModuleConfig coreCfg,
												 final Marshaller marshaller,
												 final EventBus eventBus,
												 // otp service for login auth factor handler
												 final OTPService otpService) {
		super(coreCfg,
			  Login.class,
			  null, 	// no CRUD services for Login
			  eventBus);
		
		// BEWARE!!! Use the DAO layer (the deepest one) since if the service layer was used (as usual),
		//			 the password / password hash will NOT be present at the returned object 
		//			 because the password is REMOVED at the CRUD delegate layer
		CRUDServicesForLogin loginCRUD = _createDBCRUDServicesForLogin();
		CRUDServicesForLoginEntry loginEntryCRUD = _createCRUDServicesForLoginEntry();
		CRUDServicesForPrincipal<P> principalCRUD = _createCRUDServicesForPrincipal();
		
		IsLoginAuthnSecondFactorHandler<P> loginAuthSecondFactorHandler = otpService == null 
																				? null 
													    		                : new LoginAuthnSecondFactorHandlerDefaultImpl<>(coreCfg,
						    	                		                                      							  otpService);
		_loginSubDelegate = new UserPasswordLoginServicesSubDelegateForLogin<>(coreCfg,
																			   eventBus,
																			   // services
																			   principalCRUD,
																			   loginCRUD,loginEntryCRUD,
																			   // login authn second factor handler
																			   loginAuthSecondFactorHandler);
		_passwordResetSubDelegate = new UserPasswordLoginServicesSubDelegateForPasswordReset<>(coreCfg,
																							   // services
																							   principalCRUD,
																							   loginCRUD,loginEntryCRUD);
		_passwordRecoverySubDelegate = new UserPasswordLoginServicesSubDelegateForPasswordRecovery<>(coreCfg,
																									 eventBus,
																									 // services
																									 principalCRUD,
																									 loginCRUD,loginEntryCRUD);
		_registerSubDelegate = new UserPasswordLoginServicesSubDelegateForRegister<>(coreCfg,
																				     eventBus,
																					 // services
																					 principalCRUD,
																					 loginCRUD,loginEntryCRUD);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	ABSTRACT METHODS
/////////////////////////////////////////////////////////////////////////////////////////	
	// BEWARE!!! Use the DAO layer (the deepest one) since if the service layer was used (as usual),
	//			 the password / password hash will NOT be present at the returned object 
	//			 because the password is REMOVED at the CRUD delegate layer
	protected abstract <CRUD extends DBCRUDForLoginBase<?>> CRUD _createDBCRUDServicesForLogin();
	
	protected abstract CRUDServicesForLoginEntry _createCRUDServicesForLoginEntry();
	
	protected abstract CRUDServicesForPrincipal<P> _createCRUDServicesForPrincipal();
/////////////////////////////////////////////////////////////////////////////////////////
//	LOGIN
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public IsSecurityOperationResult<UserPasswordLoginResponse> userPasswordLogin(final SecurityContext securityContext,
																    			  final UserPasswordLoginRequest loginRequest) {
		return _loginSubDelegate.userPasswordLogin(securityContext,
												   loginRequest);
	}
	@Override
	public IsSecurityOperationResult<UserPasswordLoginResponse> validateAuthSecondFactor(final SecurityContext securityContext,
		                                                                				 final IsLoginAuthnSecondFactorValidationRequest loginAuthSecondFactorValidationRequest) {
		return _loginSubDelegate.validateAuthSecondFactor(securityContext,
														  loginAuthSecondFactorValidationRequest);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	REGISTER & CONFIRM
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public IsSecurityOperationResult<Boolean> notifyLoginRegistration(final SecurityContext securityContext,
			                                                          final LoginID loginId) {
		return _registerSubDelegate.notifyLoginRegistration(securityContext,
															loginId);
	}
	@Override
	public IsSecurityOperationResult<UserPasswordLoginRegistrationAcknowledgeResponse> receiveLoginRegistrationAckowledge(final SecurityContext securityContext,
																		 			   	 					   			  final UserPasswordLoginRegistrationAcknowledgeRequest request) {
		return _registerSubDelegate.acknowledgeLoginRegistration(securityContext,
															 	 request);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CHANGE & RESET & RECOVER PASSWORD
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public IsSecurityOperationResult<UserPasswordChangeResponse> changePassword(final SecurityContext securityContext,
																				final UserPasswordChangeRequest request) {
		throw new UnsupportedOperationException("Change password is not yet supported");
	}
	@Override
	public IsSecurityOperationResult<UserPasswordResetResponse> resetPassword(final SecurityContext securityContext,
																	   		  final UserPasswordResetRequest request) {
		return _passwordResetSubDelegate.resetPassword(securityContext,
													   request);
	}
	@Override
	public IsSecurityOperationResult<UserPasswordRecoverResponse> recoverPassword(final SecurityContext securityContext,
																		  		  final UserPasswordRecoverRequest request) {
		return _passwordRecoverySubDelegate.recoverPassword(securityContext,
															request);
	}
}
