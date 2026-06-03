package r01f.services.security;

import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.login.apikey.FindServicesForApiKeyLogin;
import r01f.eventbus.EventBus;
import r01f.model.persistence.FindResult;
import r01f.model.security.login.apikey.ApiKeyLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreFindServicesForModelObjectBase;

@Accessors(prefix="_")
public abstract class CoreFindServicesImplForApiKeyLogin<L extends ApiKeyLogin>
			  extends CoreFindServicesForModelObjectBase<LoginOID,L>
		   implements FindServicesForApiKeyLogin<L>,
		   			  IsSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public CoreFindServicesImplForApiKeyLogin(final ServicesCoreModuleConfig coreCfg,
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
	public FindResult<L> findByOwnerOid(final SecurityContext securityContext,
										final UserOID ownerOid) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(FindServicesForApiKeyLogin.class)
				   .findByOwnerOid(securityContext,
						   		   ownerOid);
	}
	@Override @SuppressWarnings("unchecked")
	public FindResult<L> findByLoginId(final SecurityContext securityContext,
									   final LoginID loginId) {
		return this.forSecurityContext(securityContext)
				   .createDelegateAs(FindServicesForApiKeyLogin.class)
				   .findByLoginId(securityContext,
						   		  loginId);
	}
}

