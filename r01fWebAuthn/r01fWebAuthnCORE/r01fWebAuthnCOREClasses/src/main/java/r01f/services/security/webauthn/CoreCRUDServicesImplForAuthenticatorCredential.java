package r01f.services.security.webauthn;

import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.webauthn.CRUDServicesForAuthenticatorCredential;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.webauthn.AuthenticatorCredential;
import r01f.model.security.webauthn.WebAuthnIDS.CredentialID;
import r01f.model.security.webauthn.WebAuthnOIDs.CredentialOID;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreCRUDServicesForModelObjectBase;

@Accessors(prefix="_")
public abstract class CoreCRUDServicesImplForAuthenticatorCredential<C extends AuthenticatorCredential>
			  extends CoreCRUDServicesForModelObjectBase<CredentialOID,C>
		   implements CRUDServicesForAuthenticatorCredential<C>,
		   			  IsSecurityCoreWebAuthnServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreCRUDServicesImplForAuthenticatorCredential(final ServicesCoreModuleConfig coreCfg,
											              final Marshaller modelObjectsMarshaller,
											              final EventBus eventBus,
											              final Provider<EntityManager> entityManagerProvider) {
		super(coreCfg,
			  modelObjectsMarshaller,
			  eventBus,
			  entityManagerProvider);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Transactional
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<C> loadBy(final SecurityContext securityContext,
								final UserOID userOid) {
		return this.forSecurityContext(securityContext)
				.createDelegateAs(CRUDServicesForAuthenticatorCredential.class)
					.loadBy(securityContext,
							userOid);
	}
	@Transactional
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<C> loadBy(final SecurityContext securityContext,
								final CredentialID id) {
		return this.forSecurityContext(securityContext)
				.createDelegateAs(CRUDServicesForAuthenticatorCredential.class)
					.loadBy(securityContext,
							id);
	}

	@Transactional
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<C> loadBy(final SecurityContext securityContext,
								final CredentialID id, final UserOID oid) {
		return this.forSecurityContext(securityContext)
				.createDelegateAs(CRUDServicesForAuthenticatorCredential.class)
					.loadBy(securityContext,
							id, oid);
	}
}

