package r01f.services.security;

import com.google.inject.Singleton;

import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.login.pairedphone.CRUDServicesForPairedPhoneLoginConsent;
import r01f.api.interfaces.security.login.pairedphone.PairedPhoneLoginConsentServices;
import r01f.api.interfaces.security.user.CRUDServicesForUser;
import r01f.eventbus.EventBus;
import r01f.internal.notifier.security.user.pairedphone.PairedPhoneLoginConsentNotifier;
import r01f.model.security.login.pairedphone.PairedPhoneLoginConsent;
import r01f.model.security.login.pairedphone.PairedPhoneLoginConsentRequest;
import r01f.model.security.login.pairedphone.PairedPhoneLoginConsentResponse;
import r01f.model.security.operation.SecurityOperationResult;
import r01f.model.security.user.UserBase;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.PairedPhoneLoginConsentOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreCRUDServicesForModelObjectBase;



@Singleton
@Accessors(prefix="_")
public abstract class CoreUserPairedPhoneLoginConsentServicesImpl<U extends UserBase<U>,C extends PairedPhoneLoginConsent>
	 extends CoreCRUDServicesForModelObjectBase<PairedPhoneLoginConsentOID, C>
  implements PairedPhoneLoginConsentServices<C>,
             IsSecurityCoreServiceImpl {
//////////////////////////////////////////////////////////////////////////////////////////
//	SERVICES
/////////////////////////////////////////////////////////////////////////////////////////
    protected final PairedPhoneLoginConsentNotifier _loginConsentNotifierService;
	protected final CRUDServicesForUser<U> _crudServicesForUser;
	protected final CRUDServicesForPairedPhoneLoginConsent<C> _crudForLoginConsent;
//////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreUserPairedPhoneLoginConsentServicesImpl(final ServicesCoreModuleConfig coreCfg,
												 	   final Marshaller modelObjectsMarshaller,
												 	   final EventBus eventBus,
												 	   final Provider<EntityManager> entityManagerProvider,
												 	   // Crud services
												 	   final CRUDServicesForUser<U> crudServicesForUser,
					                                   final CRUDServicesForPairedPhoneLoginConsent<C> crudForLoginConsent,
													   // Notifier services
													   final PairedPhoneLoginConsentNotifier loginConsentNotifierService) {
			super(coreCfg,
				  modelObjectsMarshaller,
			      eventBus,
			      entityManagerProvider);

		_loginConsentNotifierService = loginConsentNotifierService;
		_crudForLoginConsent = crudForLoginConsent;
		_crudServicesForUser = crudServicesForUser;

	}
/////////////////////////////////////////////////////////////////////////////////////////
// METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	@Transactional @SuppressWarnings("unchecked")
	public SecurityOperationResult<C> request(final SecurityContext securityContext,
										     final PairedPhoneLoginConsentRequest req) {

		return this.forSecurityContext(securityContext)
					.createDelegateAs(PairedPhoneLoginConsentServices.class)
						.request(securityContext, req);
	}

	@Override
	@Transactional @SuppressWarnings("unchecked")
	public SecurityOperationResult<C> response(final SecurityContext securityContext,
			                                   final PairedPhoneLoginConsentResponse response) {
		return this.forSecurityContext(securityContext)
					.createDelegateAs(PairedPhoneLoginConsentServices.class)
						.response(securityContext, response);
	}

	@Override
	@Transactional @SuppressWarnings("unchecked")
	public SecurityOperationResult<C> loadConsent(final SecurityContext securityContext,
			                                      final PairedPhoneLoginConsentOID oid) {

		return this.forSecurityContext(securityContext)
				.createDelegateAs(PairedPhoneLoginConsentServices.class)
					.loadConsent(securityContext, oid);
	}


}
