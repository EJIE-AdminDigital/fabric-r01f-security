package r01f.services.delegates.security.userpassword;

import java.time.Instant;

import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.api.interfaces.security.login.entry.CRUDServicesForLoginEntry;
import r01f.api.interfaces.security.principal.CRUDServicesForPrincipal;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.IsSecurityResponse;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.Login;
import r01f.model.security.login.entry.LoginEntry;
import r01f.model.security.operation.IsSecurityOperationResult;
import r01f.model.security.operation.SecurityOperation;
import r01f.model.security.operation.SecurityOperationExecResultBuilder;
import r01f.model.security.principal.SecurityPrincipalAndLogin;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.LoginEntryEvent;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityOIDs.LoginOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.core.ServicesCoreModuleConfig;

@Slf4j
abstract class UserPasswordLoginServicesSubDelegateBase<P extends IsSecurityPrincipal> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final ServicesCoreModuleConfig _coreCfg;
	protected final EventBus _eventBus;
	protected final CRUDServicesForLogin _loginCrud;
	protected final CRUDServicesForLoginEntry _loginEntryCrud;
	protected final CRUDServicesForPrincipal<P> _principalCrud;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	UserPasswordLoginServicesSubDelegateBase(final ServicesCoreModuleConfig coreCfg,
											 final EventBus eventBus,
										     // services
											 final CRUDServicesForPrincipal<P> principalCrud,
											 final CRUDServicesForLogin loginCrud,final CRUDServicesForLoginEntry loginEntryCrud) {
		_coreCfg = coreCfg;
		_eventBus = eventBus;
		_loginCrud = loginCrud;
		_loginEntryCrud = loginEntryCrud;
		_principalCrud = principalCrud;
	}
	UserPasswordLoginServicesSubDelegateBase(final ServicesCoreModuleConfig coreCfg,
										     // services
											 final CRUDServicesForPrincipal<P> principalCrud,
											 final CRUDServicesForLogin loginCrud,final CRUDServicesForLoginEntry loginEntryCrud) {
		this(coreCfg,
			 null,	// no event bus
			 principalCrud,
			 loginCrud,loginEntryCrud);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LOAD SECURITY PRINCIPAL AND LOGIN
/////////////////////////////////////////////////////////////////////////////////////////
	@FunctionalInterface
	interface UserPasswordSecurityOperationOnErrorSubscriber<R extends IsSecurityResponse<?>> {
		public void onError(final IsSecurityOperationResult<R> error);
	}
	interface UserPasswordSecurityErrorResponseBuilder<R extends IsSecurityResponse<?>> {
		public R buildErrorResponse(final LoginOID loginOid,final LoginID loginId);
	}
	protected <R extends IsSecurityResponse<?>> SecurityPrincipalAndLogin<P> _loadSecurityPrincipalAndLogin(final SecurityContext securityContext,
												  															final LoginID loginId,
												  															final SecurityOperation operation,
												  															final UserPasswordSecurityErrorResponseBuilder<R> errorResponseBuilder,
												  															final UserPasswordSecurityOperationOnErrorSubscriber<R> onErrorSubscriber) {
		// [1] - Load the login
		//		 BEWARE!!! 	The DAO layer (the deepest one) is used since if the service layer was used (as usual),
		//					the password / password hash will NOT be present at the returned object since it's REMOVED at the delegate layer
		CRUDResult<Login> loginLoad = _loginCrud.loadBy(securityContext,
														IdentityProvider.FABRIC,AuthenticationMethod.USER_PASSWORD,loginId);
		if (loginLoad.hasFailed()
		 && loginLoad.asCRUDError()
					 .wasBecauseClientRequestedEntityWasNOTFound()) {
			log.warn("login with id {} was NOT found!",
					 loginId);
			onErrorSubscriber.onError(SecurityOperationExecResultBuilder.executed(operation)
													 					.returning(errorResponseBuilder.buildErrorResponse(null,loginId)));
			return null;
		} else if (loginLoad.hasFailed()) {
			onErrorSubscriber.onError(SecurityOperationExecResultBuilder.notExecuted(operation)
													 					.because(loginLoad.asCRUDError()));
			return null;
		}
		Login login = loginLoad.getOrThrow();
		
		// [2] - Load the [principal]
		P principal = _loadPrincipal(securityContext,
									 login.getSecurityPrincipalOid(),
									 login,
									 operation,
									 errorResponseBuilder,
									 onErrorSubscriber);
		
		// [99] - Return the [principal] and [login]
		return new SecurityPrincipalAndLogin<>(principal,login);
	}
	protected <R extends IsSecurityResponse<?>> SecurityPrincipalAndLogin<P> _loadSecurityPrincipalAndLogin(final SecurityContext securityContext,
												  															final LoginOID loginOid,
												  															final SecurityOperation operation,
												  															final UserPasswordSecurityErrorResponseBuilder<R> errorResponseBuilder,
												  															final UserPasswordSecurityOperationOnErrorSubscriber<R> onErrorSubscriber) {
		// [1] - Load the login
		//		 BEWARE!!! 	The DAO layer (the deepest one) is used since if the service layer was used (as usual),
		//					the password / password hash will NOT be present at the returned object since it's REMOVED at the delegate layer
		CRUDResult<Login> loginLoad = _loginCrud.load(securityContext,
													  loginOid);
		if (loginLoad.hasFailed()
		 && loginLoad.asCRUDError()
					 .wasBecauseClientRequestedEntityWasNOTFound()) {
			log.warn("login with oid {} was NOT found!",
					 loginOid);
			onErrorSubscriber.onError(SecurityOperationExecResultBuilder.executed(operation)
													 					.returning(errorResponseBuilder.buildErrorResponse(loginOid,null)));
			return null;
		} else if (loginLoad.hasFailed()) {
			onErrorSubscriber.onError(SecurityOperationExecResultBuilder.notExecuted(operation)
													 					.because(loginLoad.asCRUDError()));
			return null;
		}
		Login login = loginLoad.getOrThrow();
		
		// [2] - Load the [principal]
		P principal = _loadPrincipal(securityContext,
							 		 login.getSecurityPrincipalOid(),
							 		 login,
							 		 operation,
							 		 errorResponseBuilder,
							 		 onErrorSubscriber);
		
		// [99] - Return the [principal] and [login]
		return new SecurityPrincipalAndLogin<>(principal,login);
	}
	protected <R extends IsSecurityResponse<?>> P _loadPrincipal(final SecurityContext securityContext,
					  											 final SecurityPrincipalOID principalOid,
					  											 final Login login,
					  											 final SecurityOperation operation,
					  											 final UserPasswordSecurityErrorResponseBuilder<R> errorResponseBuilder,
					  											 final UserPasswordSecurityOperationOnErrorSubscriber<R> onErrorSubscriber) {
		CRUDResult<P> principalLoad = _principalCrud.load(securityContext,
														  principalOid);
		if (principalLoad.hasFailed()
		 && principalLoad.asCRUDError()
						 .wasBecauseClientRequestedEntityWasNOTFound()) {
			onErrorSubscriber.onError(SecurityOperationExecResultBuilder.executed(operation)
													 					.returning(errorResponseBuilder.buildErrorResponse(login.getOid(),login.getId())));
			return null;
		} else if (principalLoad.hasFailed()) {
			onErrorSubscriber.onError(SecurityOperationExecResultBuilder.notExecuted(operation)
													 					.because(principalLoad.asCRUDError()));
			return null;
		}
		return principalLoad.getOrThrow();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	Login entry
/////////////////////////////////////////////////////////////////////////////////////////
	protected void _registerLoginEntry(final SecurityContext securityContext,
									   final Login login,
									   final P principal,
									   final LoginEntryEvent event) {
		LoginEntry loginEntry = new LoginEntry();
		loginEntry.setEvent(event);
		loginEntry.setAt(Instant.now());
		loginEntry.setLoginOid(login.getOid());
		loginEntry.setLoginId(login.getId());
		loginEntry.setSecurityPrincipalOid(principal.getOid());
		
		_loginEntryCrud.create(securityContext,
							   loginEntry);
	}
}
