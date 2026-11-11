package r01f.services.delegates.security.webauthn;


import r01f.api.interfaces.security.webauthn.CRUDServicesForAuthenticatorCredential;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.CRUDResultBuilder;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.webauthn.AuthenticatorCredential;
import r01f.model.security.webauthn.WebAuthnIDS.CredentialID;
import r01f.model.security.webauthn.WebAuthnOIDs.CredentialOID;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.CRUDServicesForModelObjectDelegateBase;

public abstract class CRUDServicesDelegateForAuthenticatorCredential<C extends AuthenticatorCredential>
			  extends CRUDServicesForModelObjectDelegateBase<CredentialOID, C>
		   implements CRUDServicesForAuthenticatorCredential<C> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR   & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public CRUDServicesDelegateForAuthenticatorCredential(final ServicesCoreModuleConfig coreCfg,
														  final Class<C> modelObjectType,
														  final CRUDServicesForAuthenticatorCredential<C> dbCrud,
														  final Marshaller marshaller,
														  final EventBus eventBus) {
		super(coreCfg,
			  modelObjectType,
			  dbCrud,
			  eventBus);
	}
///////////////////////////////////////////////////////////////////////////////////////
// 	EXTENSION
///////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<C> loadBy(final SecurityContext securityContext,
								final SecurityPrincipalOID securityPrincipalOid) {
		if (securityPrincipalOid == null) return CRUDResultBuilder.using(securityContext)
																  .on(_modelObjectType)
																  .notLoaded()
																  .becauseClientBadRequest("The userOid MUST not be null")
																  .build();
		CRUDResult<C> outResult = this.getServiceImplAs(CRUDServicesForAuthenticatorCredential.class)
									  .loadBy(securityContext,
											  securityPrincipalOid);
		return outResult;
	}

	@Override @SuppressWarnings("unchecked")
	public CRUDResult<C> loadBy(final SecurityContext securityContext,
								final CredentialID login) {
		if (login == null) return CRUDResultBuilder.using(securityContext)
												   .on(_modelObjectType)
												   .notLoaded()
												   .becauseClientBadRequest("The login MUST not be null")
												   .build();
		CRUDResult<C> outResult = this.getServiceImplAs(CRUDServicesForAuthenticatorCredential.class)
									  .loadBy(securityContext,
											  login);
		return outResult;
	}

	@Override @SuppressWarnings("unchecked")
	public CRUDResult<C> loadBy(final SecurityContext securityContext,
								final CredentialID id,final SecurityPrincipalOID securityPrincipalOid) {
		if (id == null) return CRUDResultBuilder.using(securityContext)
												.on(_modelObjectType)
												.notLoaded()
												.becauseClientBadRequest("The login MUST not be null")
												.build();
		CRUDResult<C> outResult = this.getServiceImplAs(CRUDServicesForAuthenticatorCredential.class)
									  .loadBy(securityContext,
											  securityPrincipalOid);
		return outResult;
	}
}
