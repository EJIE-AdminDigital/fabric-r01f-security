package r01f.services.delegates.security;



import r01f.api.interfaces.security.login.google.CRUDServicesForGoogleLogin;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.CRUDResultBuilder;
import r01f.model.security.login.google.GoogleLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.CRUDServicesForModelObjectDelegateBase;
import r01f.types.contact.EMail;

public abstract class CRUDServicesDelegateForGoogleLogin<L extends GoogleLogin>
			  extends CRUDServicesForModelObjectDelegateBase<LoginOID,L>
		   implements CRUDServicesForGoogleLogin<L> {

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR   & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public CRUDServicesDelegateForGoogleLogin(final ServicesCoreModuleConfig coreCfg,
											  final Class<L> modelObjectType,
											  final CRUDServicesForGoogleLogin<L> dbCrud,
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
		CRUDResult<L> outResult = this.getServiceImplAs(CRUDServicesForGoogleLogin.class)
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
		CRUDResult<L> outResult = this.getServiceImplAs(CRUDServicesForGoogleLogin.class)
												.loadBy(securityContext,
														login);
		return outResult;
	}
	@Override
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
								final EMail email) {
		if (email == null
		 || !email.isGoogleEMail()) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientBadRequest("The email MUST be a not null google mail address")
									.build();
		}
		@SuppressWarnings("unchecked")
		CRUDResult<L> outResult = this.getServiceImplAs(CRUDServicesForGoogleLogin.class)
												.loadBy(securityContext,
														email);
		return outResult;
	}
}
