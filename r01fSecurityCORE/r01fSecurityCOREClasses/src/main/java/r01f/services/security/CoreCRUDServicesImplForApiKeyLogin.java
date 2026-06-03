package r01f.services.security;



import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.login.apikey.CRUDServicesForApiKeyLogin;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.login.apikey.ApiKeyLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityToken;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreCRUDServicesForModelObjectBase;
import r01f.types.AppVersion;

@Accessors(prefix="_")
public abstract class CoreCRUDServicesImplForApiKeyLogin<L extends ApiKeyLogin>
			  extends CoreCRUDServicesForModelObjectBase<LoginOID,L>
		   implements CRUDServicesForApiKeyLogin<L>,
		   			  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreCRUDServicesImplForApiKeyLogin(final ServicesCoreModuleConfig coreCfg,
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
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
								final UserOID userOid) {
		return this.forSecurityContext(securityContext)
				.createDelegateAs(CRUDServicesForApiKeyLogin.class)
					.loadBy(securityContext,
							userOid);
	}
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
								final LoginID login) {
		return this.forSecurityContext(securityContext)
				.createDelegateAs(CRUDServicesForApiKeyLogin.class)
					.loadBy(securityContext,
							login);
	}
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<L> loadByPublicKey(final SecurityContext securityContext,
										 final SecurityToken publicKey) {
		return this.forSecurityContext(securityContext)
				.createDelegateAs(CRUDServicesForApiKeyLogin.class)
					.loadByPublicKey(securityContext,
									 publicKey);
	}
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
							  	final LoginID loginId,final AppVersion version) {
		return this.forSecurityContext(securityContext)
					.createDelegateAs(CRUDServicesForApiKeyLogin.class)
						.loadBy(securityContext,
								loginId,version);
	}
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
							    final UserOID ownerOid,
							    final AppVersion version) {
		return this.forSecurityContext(securityContext)
					.createDelegateAs(CRUDServicesForApiKeyLogin.class)
						.loadBy(securityContext,
							  ownerOid,
							  version);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Transactional
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<L> issue(final SecurityContext securityContext,
							   final UserOID ownerOid,
							   final AppVersion version,final boolean readOnly) {
		return this.forSecurityContext(securityContext)
					.createDelegateAs(CRUDServicesForApiKeyLogin.class)
						.issue(securityContext,
							   ownerOid,
							   version,readOnly);
	}
	@Transactional
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<L> issueForLastVersion(final SecurityContext securityContext,
											 final UserOID ownerOid,
											 final boolean readOnly) {
		return this.forSecurityContext(securityContext)
					.createDelegateAs(CRUDServicesForApiKeyLogin.class)
						.issueForLastVersion(securityContext,
											 ownerOid,
											 readOnly);
	}
	@Transactional
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<L> invalidate(final SecurityContext securityContext,
									final LoginOID apiKeyOid) {
		return this.forSecurityContext(securityContext)
					.createDelegateAs(CRUDServicesForApiKeyLogin.class)
						.invalidate(securityContext,
									apiKeyOid);
	}
}

