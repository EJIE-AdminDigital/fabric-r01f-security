package r01f.internal.notifier.security.user.passwordrecover;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import org.apache.velocity.app.VelocityEngine;

import lombok.extern.slf4j.Slf4j;
import r01f.core.notifier.NotifierResponse;
import r01f.core.notifier.NotifierResponseResult;
import r01f.core.notifier.NotifierResponseResultImpl;
import r01f.core.services.notifier.NotifierServiceForSMS;
import r01f.core.services.notifier.NotifierServiceResponse;
import r01f.core.services.notifier.config.NotifierConfigForSMS;
import r01f.events.security.UserPasswordRecoverMessage;
import r01f.internal.notifier.security.config.SecurityNotifierConfigForSMS;
import r01f.securitycontext.SecurityIDS.SecurityToken;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.types.Path;
import r01f.types.contact.Phone;
import r01f.util.types.Strings;

/**
 * SMS notifier
 */
@Singleton
@Slf4j
public class UserPasswordRecoverNotifierSMSImpl
     extends UserPasswordRecoverNotifierBase<NotifierConfigForSMS> {
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
	public UserPasswordRecoverNotifierSMSImpl(final NotifierConfigForSMS config,final NotifierServiceForSMS smsNotifier,
										  	  final VelocityEngine velocityEngine) {
		super(config,
			  velocityEngine);
		_smsNotifier = smsNotifier;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public NotifierResponse  sendNotification(final UserPasswordRecoverMessage pwdRecoverMsg) {
		if (!this.isEnabledConsidering(_smsNotifier)) {
			log.warn("SMS notifier is DISABLED!");
			return null;
		}
		log.info("[SMS Notifier ({})]================================================",
				 _smsNotifier.getClass().getSimpleName());
		if (pwdRecoverMsg.getPhone() == null) {
			log.warn("... NO phones to notify to");
			return null;
		}
		NotifierServiceResponse<Phone> smsNotifierResponse = _smsNotifier.notify(_config.getFrom(),pwdRecoverMsg.getPhone(),
																			     // msg factory
																			     () -> _composeIMMessage(_velocityEngine,_config.getAppConfigAs(SecurityNotifierConfigForSMS.class)
																														        .getMsgTemplatePath(),
																									     pwdRecoverMsg.getUserOid(),pwdRecoverMsg.getUserName(),
																							   		     pwdRecoverMsg.getToken()));
		return new NotifierResponse() {
						@Override
						public boolean wasSuccessful() {
							return smsNotifierResponse.wasSuccessful();
						}
						@Override @SuppressWarnings("unchecked")
						public NotifierResponseResult<Phone> getResponseResult() {
							return new NotifierResponseResultImpl<>(smsNotifierResponse);
						}};
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	PRIVATE METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	static String _composeIMMessage(final VelocityEngine velocityEngine,final Path alertMsgTemplatePath,
									final UserOID userOid,final String userName,
									final SecurityToken securityToken) {
		return Strings.customized("Utiliza el token [{}] para recuperar tu password",
								  userName,securityToken);
	}
}
