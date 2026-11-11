package r01f.internal.notifier.security.user.registration;

import org.apache.velocity.app.VelocityEngine;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import r01f.core.notifier.NotifierResponse;
import r01f.core.notifier.NotifierResponseResult;
import r01f.core.notifier.NotifierResponseResultImpl;
import r01f.core.services.notifier.NotifierServiceForSMS;
import r01f.core.services.notifier.NotifierServiceResponse;
import r01f.core.services.notifier.config.NotifierConfigForSMS;
import r01f.events.security.UserRegistrationMessage;
import r01f.internal.notifier.security.config.SecurityNotifierConfigForSMS;
import r01f.oids.SecurityIDs.SecurityToken;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.patterns.Factory;
import r01f.types.Path;
import r01f.types.contact.Phone;
import r01f.util.types.Strings;

/**
 * SMS notifier
 */
@Singleton
@Slf4j
public class UserRegistrationNotifierSMSImpl
     extends UserRegistrationNotifierBase<NotifierConfigForSMS> {
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * SMS notifier services
	 */
	private final NotifierServiceForSMS _smsNotifier;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////
	@Inject
	public UserRegistrationNotifierSMSImpl(final NotifierConfigForSMS config,final NotifierServiceForSMS smsNotifier,
										   final VelocityEngine velocityEngine) {
		super(config,
			  velocityEngine);
		_smsNotifier = smsNotifier;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public NotifierResponse sendNotification(final UserRegistrationMessage usrRegistrationMsg) {
		if (!this.isEnabledConsidering(_smsNotifier)) {
			log.warn("SMS notifier is DISABLED!");
			return null;
		}
		log.info("[SMS Notifier ({})]================================================",
				 _smsNotifier.getClass().getSimpleName());
		if (usrRegistrationMsg.getPhone() == null) {
			log.warn("... NO phones to notify to");
			return  null;
		}

		NotifierServiceResponse<Phone> serviceResponse =
					_smsNotifier.notify(_config.getFrom(),usrRegistrationMsg.getPhone(),
										new Factory<String>() {
												@Override
												public String create() {
													return _composeIMMessage(_velocityEngine,_config.getAppConfigAs(SecurityNotifierConfigForSMS.class)
																									.getMsgTemplatePath(),
																	   		  usrRegistrationMsg.getUserOid(),usrRegistrationMsg.getUserName(),
																	   		  usrRegistrationMsg.getToken());
												}
										});
		return new NotifierResponse() {
							@Override
							public boolean wasSuccessful() {
								return serviceResponse.wasSuccessful();
							}

							@Override @SuppressWarnings("unchecked")
							public  NotifierResponseResult<Phone> getResponseResult() {
								return new NotifierResponseResultImpl<Phone>(serviceResponse);
							}} ;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	PRIVATE METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	static String _composeIMMessage(final VelocityEngine velocityEngine,final Path alertMsgTemplatePath,
									final SecurityPrincipalOID userOid,final String userName,
									final SecurityToken securityToken) {

		return Strings.customized("Utiliza el token [{}] para recuperar tu password",
																					  userName,
																					  securityToken);
	}
}
