package r01f.services.delegates.security;



import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.apikey.FindServicesForApiKeyLogin;
import r01f.eventbus.EventBus;
import r01f.model.persistence.FindResult;
import r01f.model.persistence.FindResultBuilder;
import r01f.model.security.login.apikey.ApiKeyLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.FindServicesForModelObjectDelegateBase;

@Slf4j
public abstract class FindServicesDelegateForApiKeyLogin<L extends ApiKeyLogin>
			  extends FindServicesForModelObjectDelegateBase<LoginOID,L>
		   implements FindServicesForApiKeyLogin<L> {

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR   & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public FindServicesDelegateForApiKeyLogin(final ServicesCoreModuleConfig coreCfg,
											  final Class<L> modelObjectType,
											  final FindServicesForApiKeyLogin<L> dbCrud,
											  final Marshaller marshaller,
											  final EventBus eventBus) {
		super(coreCfg,
			  modelObjectType,
			  dbCrud,
			  eventBus);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public FindResult<L> findByOwnerOid(final SecurityContext securityContext,
										final UserOID ownerOid) {
		log.info("..find apikey logins by userOid {}" ,ownerOid );
		if (ownerOid == null) {
			return FindResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.errorFindingEntities()
									.causedByClientBadRequest("The ownerOid MUST not be null");
		}
		FindResult<L> outResult = this.getServiceImplAs(FindServicesForApiKeyLogin.class)
											.findByOwnerOid(securityContext,
															ownerOid);
		return outResult;
	}
	@Override  @SuppressWarnings("unchecked")
	public FindResult<L> findByLoginId(final SecurityContext securityContext,
									   final LoginID loginId) {
		if (loginId == null) {
			return FindResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.errorFindingEntities()
									.causedByClientBadRequest("The loginId MUST not be null");
		}
		FindResult<L> outResult = this.getServiceImplAs(FindServicesForApiKeyLogin.class)
											.findByLoginId(securityContext,
														   loginId);
		return outResult;
	}
}
