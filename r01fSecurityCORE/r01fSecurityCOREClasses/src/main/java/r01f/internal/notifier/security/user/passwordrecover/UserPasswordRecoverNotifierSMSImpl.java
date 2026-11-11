package r01f.internal.notifier.security.user.passwordrecover;

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
import r01f.events.security.UserPasswordRecoverMessage;
import r01f.internal.notifier.security.config.SecurityNotifierConfigForSMS;
import r01f.oids.SecurityIDs.SecurityToken;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.types.Path;
import r01f.types.contact.NotificationMedium;
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
		Phone phone = _getContactMean(pwdRecoverMsg,
									  NotificationMedium.SMS,
									 _smsNotifier);
		NotifierServiceResponse<Phone> smsNotifierResponse = _smsNotifier.notify(_config.getFrom(),phone,
																			     // msg factory
																			     () -> _composeIMMessage(_velocityEngine,_config.getAppConfigAs(SecurityNotifierConfigForSMS.class)
																														        .getMsgTemplatePath(),
																									     pwdRecoverMsg.getPrincipalOid(),pwdRecoverMsg.getDisplayName(),
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
									final SecurityPrincipalOID userOid,final String userName,
									final SecurityToken securityToken) {
		return Strings.customized("Utiliza el token [{}] para recuperar tu password",
								  userName,securityToken);
	}
}
