package r01f.services.delegates.security.webauthn;


import r01f.api.interfaces.security.webauthn.FindServicesForAuthenticatorCredential;
import r01f.eventbus.EventBus;
import r01f.model.persistence.FindResult;
import r01f.model.security.webauthn.AuthenticatorCredential;
import r01f.model.security.webauthn.WebAuthnOIDs.CredentialOID;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.FindServicesForModelObjectDelegateBase;

public abstract class FindServicesDelegateForAuthenticatorCredential<C extends AuthenticatorCredential>
			  extends FindServicesForModelObjectDelegateBase<CredentialOID,C>
		   implements FindServicesForAuthenticatorCredential<C> {

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR   & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public FindServicesDelegateForAuthenticatorCredential(final ServicesCoreModuleConfig coreCfg,
											              final Class<C> modelObjectType,
											              final FindServicesForAuthenticatorCredential<C> dbCrud,
											              final Marshaller marshaller,
											              final EventBus eventBus) {
		super(coreCfg,
			  modelObjectType,
			  dbCrud,
			  eventBus);
	}
/////////////////////////////////////////////////////////////////////////////////////////
// EXTENSION METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public FindResult<C> findBy(final SecurityContext securityContext,
			                    final UserOID userOid ) {
		return this.getServiceImplAs(FindServicesForAuthenticatorCredential.class)
							.findBy(securityContext,userOid);
	}
}
