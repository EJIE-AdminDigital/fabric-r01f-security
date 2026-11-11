package r01f.services.security.otp;


import com.google.inject.Singleton;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.otp.OTPServiceBase;
import r01f.eventbus.EventBus;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.otp.OTPTask;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.objectstreamer.Marshaller;
import r01f.oids.CommonOIDs.AppCode;
import r01f.oids.CommonOIDs.AppComponent;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.persistence.CoreServicesBase;
import r01f.services.core.persistence.ServiceDelegateProvider;
import r01f.services.delegates.security.otp.OTPServicesDelegateBase;
import r01f.types.contact.EMail;
import r01f.types.contact.Phone;


@Singleton
@Accessors(prefix="_")
abstract class CoreOTPServiceImplBase<D extends OTPServicesDelegateBase> 	
	   extends CoreServicesBase 
    implements OTPServiceBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Getter private final ServiceDelegateProvider<D>  _delegateProvider;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public CoreOTPServiceImplBase(final ServicesCoreModuleConfig coreConfig,
		                          final Marshaller modelObjectsMarshaller,
			                      final EventBus eventBus,
			                      final ServiceDelegateProvider<D> delegate ) {		
		 super(coreConfig,
			   modelObjectsMarshaller,
			   eventBus);
		_delegateProvider = delegate;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public OTPTask getTaskByOID(final SecurityContext securityContext,
			                    final OTPTaskOID taskOid) {		
		return this.forSecurityContext(securityContext)
					.createDelegateAs(OTPServiceBase.class)
						.getTaskByOID(securityContext, taskOid);				
	}
	@Override
	public OTPTask getTaskByPhone(final SecurityContext securityContext,
			                      final AppCode appCode, final AppComponent appComponent, final  Phone phone) {
		return this.forSecurityContext(securityContext)
						.createDelegateAs(OTPServiceBase.class)
							.getTaskByPhone(securityContext, appCode,appComponent,phone);
	}
	@Override
	public OTPTask getTaskByEMail(final SecurityContext securityContext,
			                      final AppCode appCode, final AppComponent appComponent, final EMail eMail) {
		return this.forSecurityContext(securityContext)
						.createDelegateAs(OTPServiceBase.class)
							.getTaskByEMail(securityContext, appCode,appComponent,eMail);
	}	
}
