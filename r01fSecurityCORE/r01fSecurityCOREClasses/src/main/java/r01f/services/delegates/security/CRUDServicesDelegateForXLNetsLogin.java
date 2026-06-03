package r01f.services.delegates.security;



import r01f.api.interfaces.security.login.xlnets.CRUDServicesForXLNetsLogin;
import r01f.eventbus.EventBus;
import r01f.guids.CommonOIDs.WorkPlaceCode;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.CRUDResultBuilder;
import r01f.model.security.login.xlnets.XLNetsLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.CRUDServicesForModelObjectDelegateBase;

public abstract class CRUDServicesDelegateForXLNetsLogin<L extends XLNetsLogin>
			  extends CRUDServicesForModelObjectDelegateBase<LoginOID,L>
		   implements CRUDServicesForXLNetsLogin<L> {

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR   & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public CRUDServicesDelegateForXLNetsLogin(final ServicesCoreModuleConfig coreCfg,
											  final Class<L> modelObjectType,
											  final CRUDServicesForXLNetsLogin<L> dbCrud,
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
		CRUDResult<L> outResult = this.getServiceImplAs(CRUDServicesForXLNetsLogin.class)
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
		CRUDResult<L> outResult = this.getServiceImplAs(CRUDServicesForXLNetsLogin.class)
												.loadBy(securityContext,
														login);
		return outResult;
	}

	@Override
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
								final WorkPlaceCode  login) {
		if (login == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientBadRequest("The login WorkPlaceCode  MUST not be null")
									.build();
		}
		@SuppressWarnings("unchecked")
		CRUDResult<L> outResult = this.getServiceImplAs(CRUDServicesForXLNetsLogin.class)
												.loadBy(securityContext,
														login);
		return outResult;
	}
}
