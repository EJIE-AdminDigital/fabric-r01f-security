package r01f.services.delegates.security;


import r01f.api.interfaces.security.login.giltza.CRUDServicesForGiltzaLogin;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.CRUDResultBuilder;
import r01f.model.security.login.giltza.GiltzaLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.CRUDServicesForModelObjectDelegateBase;
import r01f.types.contact.EMail;

public abstract class CRUDServicesDelegateForGiltzaLogin<L extends GiltzaLogin>
			  extends CRUDServicesForModelObjectDelegateBase<LoginOID,L>
		   implements CRUDServicesForGiltzaLogin<L> {

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR   & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public CRUDServicesDelegateForGiltzaLogin(final ServicesCoreModuleConfig coreCfg,
											  final Class<L> modelObjectType,
											  final CRUDServicesForGiltzaLogin<L> dbCrud,
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
	@Override
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
								final UserOID userOid) {
		if (userOid == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientBadRequest("The userOid MUST not be null")
									.build();
		}
		@SuppressWarnings("unchecked")
		CRUDResult<L> outResult = this.getServiceImplAs(CRUDServicesForGiltzaLogin.class)
												.loadBy(securityContext,
														userOid);
		return outResult;
	}
	@Override
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
								final LoginID login) {
		if (login == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientBadRequest("The login MUST not be null")
									.build();
		}
		@SuppressWarnings("unchecked")
		CRUDResult<L> outResult = this.getServiceImplAs(CRUDServicesForGiltzaLogin.class)
												.loadBy(securityContext,
														login);
		return outResult;
	}
	@Override
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
								final EMail email) {
		if (email == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientBadRequest("The email MUST be a not null Giltza mail address")
									.build();
		}
		@SuppressWarnings("unchecked")
		CRUDResult<L> outResult = this.getServiceImplAs(CRUDServicesForGiltzaLogin.class)
												.loadBy(securityContext,
														email);
		return outResult;
	}
}
