package r01f.services.security;

import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.api.interfaces.security.login.PairedPhoneLoginServices;
import r01f.api.interfaces.security.principal.CRUDServicesForPrincipal;
import r01f.eventbus.EventBus;
import r01f.internal.notifier.security.user.pairedphone.PairedPhoneLoginConsentNotifier;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.pairedphone.messages.login.PairedPhoneLoginConsentRequest;
import r01f.model.security.login.pairedphone.messages.login.PairedPhoneLoginConsentResponseFromPairedPhone;
import r01f.model.security.login.pairedphone.messages.login.PairedPhoneLoginResponse;
import r01f.model.security.operation.IsSecurityOperationResult;
import r01f.objectstreamer.Marshaller;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CorePersistenceServicesBase;
import r01f.types.contact.Phone;

public abstract class CorePairedPhoneLoginServicesImplBase<P extends IsSecurityPrincipal> 
			  extends CorePersistenceServicesBase
		   implements PairedPhoneLoginServices,
		   			  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	// Crud services
	protected final CRUDServicesForPrincipal<P> _principalCrud;
	protected final CRUDServicesForLogin _loginCrud;
	// Notifier services
	protected final PairedPhoneLoginConsentNotifier _loginConsentNotifier;	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public CorePairedPhoneLoginServicesImplBase(final ServicesCoreModuleConfig coreCfg,
												final Marshaller modelObjectsMarshaller,
												final EventBus eventBus,
												final Provider<EntityManager> entityManagerProvider,
												// Crud services
										 	    final CRUDServicesForPrincipal<P> principalCrud,
			                                    final CRUDServicesForLogin loginCrud,
											    // Notifier services
											    final PairedPhoneLoginConsentNotifier loginConsentNotifier) {
		super(coreCfg,
			  modelObjectsMarshaller,
			  eventBus,
			  entityManagerProvider);
		_principalCrud = principalCrud;
		_loginCrud = loginCrud;
		_loginConsentNotifier = loginConsentNotifier;
	}
/////////////////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public IsSecurityOperationResult<PairedPhoneLoginConsentRequest> createConsentRequestFor(final SecurityContext securityContext,
										        			 							     final Phone phone) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(PairedPhoneLoginServices.class)
				   .createConsentRequestFor(securityContext,
						   				    phone);	
	}
	@Override
	public IsSecurityOperationResult<PairedPhoneLoginConsentRequest> loadConsentRequest(final SecurityContext securityContext,
                                                   										final Phone phone) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(PairedPhoneLoginServices.class)
				   .loadConsentRequest(securityContext,
						   			   phone);
	}
	@Override
	public IsSecurityOperationResult<PairedPhoneLoginResponse> receiveConsentResponseFromPairedPhone(final SecurityContext securityContext,
		                                          													 final PairedPhoneLoginConsentResponseFromPairedPhone response) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(PairedPhoneLoginServices.class)
				   .receiveConsentResponseFromPairedPhone(securityContext,
						   				 				  response);	
	}
}
