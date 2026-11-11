package r01f.services.security;

import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import r01f.api.interfaces.security.login.UserPasswordLoginServices;
import r01f.eventbus.EventBus;
import r01f.model.security.context.SecurityContext;
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
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CorePersistenceServicesBase;

public abstract class CoreUserPasswordLoginServicesImplBase 
			  extends CorePersistenceServicesBase
		   implements UserPasswordLoginServices,
		   			  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreUserPasswordLoginServicesImplBase(final ServicesCoreModuleConfig coreCfg,
												 final Marshaller modelObjectsMarshaller,
												 final EventBus eventBus,
												 final Provider<EntityManager> entityManagerProvider) {
		super(coreCfg,
			  modelObjectsMarshaller,
			  eventBus,
			  entityManagerProvider);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LOGIN
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public IsSecurityOperationResult<UserPasswordLoginResponse> userPasswordLogin(final SecurityContext securityContext,
																    			  final UserPasswordLoginRequest loginRequest) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(UserPasswordLoginServices.class)
				   .userPasswordLogin(securityContext,
				   					  loginRequest); 
	}
	@Override
	public  IsSecurityOperationResult<UserPasswordLoginResponse> validateAuthSecondFactor(final SecurityContext securityContext,
		                                                                				  final IsLoginAuthnSecondFactorValidationRequest loginAuthSecondFactorValidationRequest) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(UserPasswordLoginServices.class)
				   .validateAuthSecondFactor(securityContext,
				   						     loginAuthSecondFactorValidationRequest);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	REGISTER & CONFIRM
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public IsSecurityOperationResult<Boolean> notifyLoginRegistration(final SecurityContext securityContext,
			                                                          final LoginID loginId) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(UserPasswordLoginServices.class)
				   .notifyLoginRegistration(securityContext,
				   						    loginId);
	}
	@Override
	public IsSecurityOperationResult<UserPasswordLoginRegistrationAcknowledgeResponse> receiveLoginRegistrationAckowledge(final SecurityContext securityContext,
																		 			   	 					   			  final UserPasswordLoginRegistrationAcknowledgeRequest request) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(UserPasswordLoginServices.class)
				   .receiveLoginRegistrationAckowledge(securityContext,
				   									   request);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CHANGE & RESET & RECOVER PASSWORD
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public IsSecurityOperationResult<UserPasswordChangeResponse> changePassword(final SecurityContext securityContext,
																							 final UserPasswordChangeRequest request) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(UserPasswordLoginServices.class)
				   .changePassword(securityContext,
				   				   request);
	}
	@Override
	public IsSecurityOperationResult<UserPasswordResetResponse> resetPassword(final SecurityContext securityContext,
																	   			   		   final UserPasswordResetRequest request) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(UserPasswordLoginServices.class)
				   .resetPassword(securityContext,
				   				  request);
	}
	@Override
	public IsSecurityOperationResult<UserPasswordRecoverResponse> recoverPassword(final SecurityContext securityContext,
																		  			  	  	   final UserPasswordRecoverRequest request) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(UserPasswordLoginServices.class)
				   .recoverPassword(securityContext,
				   				    request);
	}
}
