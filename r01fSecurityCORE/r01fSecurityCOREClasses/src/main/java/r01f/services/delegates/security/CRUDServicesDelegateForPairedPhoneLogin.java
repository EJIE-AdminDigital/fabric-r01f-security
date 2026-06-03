package r01f.services.delegates.security;


import r01f.api.interfaces.security.login.pairedphone.CRUDServicesForPairedPhoneLogin;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.CRUDResultBuilder;
import r01f.model.security.login.pairedphone.PairedPhoneLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.delegates.persistence.CRUDServicesForModelObjectDelegateBase;
import r01f.types.contact.Phone;


public abstract class CRUDServicesDelegateForPairedPhoneLogin<L extends PairedPhoneLogin>
	 		  extends CRUDServicesForModelObjectDelegateBase<LoginOID,L>
  		   implements CRUDServicesForPairedPhoneLogin<L> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR   & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public CRUDServicesDelegateForPairedPhoneLogin(final ServicesCoreModuleConfig coreCfg,
												   final Class<L> modelObjectType,
											       final CRUDServicesForPairedPhoneLogin<L> dbCrud,
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
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
							    final UserOID userOid) {
		if (userOid == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientBadRequest("The userOid MUST not be null")
									.build();
		}
		CRUDResult<L> outResult = this.getServiceImplAs(CRUDServicesForPairedPhoneLogin.class)
																.loadBy(securityContext,
																		userOid);
		return outResult;
	}
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
								                       final LoginID login) {
		if (login == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientBadRequest("The login MUST not be null")
									.build();
		}
		CRUDResult<L> outResult = this.getServiceImplAs(CRUDServicesForPairedPhoneLogin.class)
																.loadBy(securityContext,
																		login);
		return outResult;
	}
	@Override @SuppressWarnings("unchecked")
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
								                       final Phone phone) {
		if (phone == null) {
			return CRUDResultBuilder.using(securityContext)
									.on(_modelObjectType)
									.notLoaded()
									.becauseClientBadRequest("The email MUST be a not null google mail address")
									.build();
		}
		CRUDResult<L> outResult = this.getServiceImplAs(CRUDServicesForPairedPhoneLogin.class)
																.loadBy(securityContext,
																		phone);
		return outResult;
	}

}
