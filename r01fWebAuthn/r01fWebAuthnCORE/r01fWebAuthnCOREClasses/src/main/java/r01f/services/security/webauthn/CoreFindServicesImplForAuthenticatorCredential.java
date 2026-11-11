package r01f.services.security.webauthn;


import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.webauthn.FindServicesForAuthenticatorCredential;
import r01f.eventbus.EventBus;
import r01f.model.persistence.FindResult;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.webauthn.AuthenticatorCredential;
import r01f.model.security.webauthn.WebAuthnOIDs.CredentialOID;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreFindServicesForModelObjectBase;

@Accessors(prefix="_")
public abstract class CoreFindServicesImplForAuthenticatorCredential<C extends AuthenticatorCredential>
			  extends CoreFindServicesForModelObjectBase<CredentialOID,C>
		   implements FindServicesForAuthenticatorCredential<C>,
		   			  IsSecurityCoreWebAuthnServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreFindServicesImplForAuthenticatorCredential(final ServicesCoreModuleConfig coreCfg,
										                  final Marshaller modelObjectsMarshaller,
											              final EventBus eventBus,
											              final Provider<EntityManager> entityManagerProvider) {
		super(coreCfg,
			  modelObjectsMarshaller,
			  eventBus,
			  entityManagerProvider);
	}
/////////////////////////////////////////////////////////////////////////////////////////
// EXTENSION METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public FindResult<C> findBy(final SecurityContext securityContext,
			                    final SecurityPrincipalOID userOid ) {
		return this.forSecurityContext(securityContext)
						.createDelegateAs(FindServicesForAuthenticatorCredential.class)
							.findBy(securityContext,userOid);
	}
}

