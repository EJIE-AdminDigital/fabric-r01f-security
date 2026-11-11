package r01f.internal.notifier.security.user.passwordrecover;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import r01f.core.notifier.NotifierResponse;
import r01f.core.notifier.NotifierResponseResult;
import r01f.core.notifier.NotifierResponseResultImpl;
import r01f.core.services.mail.model.EMailRFC822Address;
import r01f.core.services.mail.notifier.JavaMailSenderNotifierService;
import r01f.core.services.notifier.NotifierServiceForEMail;
import r01f.core.services.notifier.NotifierServiceResponse;
import r01f.core.services.notifier.config.NotifierConfigForEMail;
import r01f.events.security.UserPasswordRecoverMessage;
import r01f.internal.R01F;
import r01f.internal.notifier.security.config.SecurityNotifierConfigForEMail;
import r01f.oids.SecurityIDs.SecurityToken;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.patterns.Factory;
import r01f.types.Path;
import r01f.types.contact.EMail;
import r01f.types.contact.NotificationMedium;
import r01f.types.url.Url;

/**
 * EMail notifier
 */
@Slf4j
@Singleton
public class UserPasswordRecoverNotifierEMailImpl
     extends UserPasswordRecoverNotifierBase<NotifierConfigForEMail> {
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final NotifierServiceForEMail _mailNotifier;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@Inject
	public UserPasswordRecoverNotifierEMailImpl(final NotifierConfigForEMail notifierConfig,final NotifierServiceForEMail notifier,
												final VelocityEngine velocityEngine) {
		super(notifierConfig,
			  velocityEngine);
		_mailNotifier = notifier;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public NotifierResponse sendNotification(final UserPasswordRecoverMessage pwdRecoverMsg) {
		EMail email = _getContactMean(pwdRecoverMsg,
									 NotificationMedium.EMAIL,
									 _mailNotifier);
		NotifierServiceResponse<EMailRFC822Address> serviceResponse = _sendEMailMessage(EMailRFC822Address.of(email),
								  													    pwdRecoverMsg.getPrincipalOid(),pwdRecoverMsg.getDisplayName(),
								  													    pwdRecoverMsg.getToken());
		return new NotifierResponse() {
						@Override
						public boolean wasSuccessful() {
							return serviceResponse.wasSuccessful();
						}
						@Override @SuppressWarnings("unchecked")
						public NotifierResponseResult<EMailRFC822Address> getResponseResult() {
							return new NotifierResponseResultImpl<EMailRFC822Address>(serviceResponse);
						}
				};
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Send a mail message when an alarm message is received
	 * @param to
	 * @param org
	 * @param log
	 * @param workPlace
	 * @return
	 */
	private NotifierServiceResponse<EMailRFC822Address> _sendEMailMessage(final EMailRFC822Address to,
																          final SecurityPrincipalOID userOid,final String userName,
																          final SecurityToken securityToken) {
		log.info("\t-->sending email to {} using {}",
				 to,_mailNotifier.getClass());

		// [1] - Create a MimeMessagePreparator
		final String subject = _composeMailMessageSubject();
		final String body = _composeMailMessageBody(_velocityEngine,_config.getAppConfigAs(SecurityNotifierConfigForEMail.class)
																		   .getMsgTemplatePath(),
													userOid,userName,
													securityToken,
													_config.getAppConfigAs(SecurityNotifierConfigForEMail.class)
																		   .getPwdRstRedirectUrl());

		  // [2] - Send the message
        return _mailNotifier.notify(_config.getFrom(),to,
        					 // mime message factory
        					 new Factory<MimeMessage>() {
									@Override @SneakyThrows
									public MimeMessage create() {
										JavaMailSender mailSender = ((JavaMailSenderNotifierService)_mailNotifier).getSpringJavaMailSender();
										MimeMessage mimeMessage = mailSender.createMimeMessage();
									    MimeMessageHelper msgHelper = new MimeMessageHelper(mimeMessage,
									    												    true);	// multi-part!!
									    // To & From
									    msgHelper.setTo(EMailRFC822Address.asRFC822Address(to));
									    msgHelper.setFrom(_config.getFrom().asRFC822Address());

									    // Subject & Text
									    msgHelper.setSubject(subject);
									    msgHelper.setText(body,
									    				  true);	// html message
									    // Header image (ALWAYS AFTER message.setText())
								        ClassPathResource res = new ClassPathResource(_config.getAppConfigAs(SecurityNotifierConfigForEMail.class)
								        													 .getMsgImagePath()
								        													 .asRelativeString());
								        msgHelper.addInline("logo",res);
								        return mimeMessage;
									}
							 });
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  MAIL MESSAGE COMPOSING
/////////////////////////////////////////////////////////////////////////////////////////
	private static String _composeMailMessageSubject() {
		return "Recuperación de password";
	}
	private static String _composeMailMessageBody(final VelocityEngine velocityEngine,final Path alertMsgTemplatePath,
												  final SecurityPrincipalOID userOid,final String userName,
								   				  final SecurityToken securityToken, final Url redirectUrl) {

		// Text... using velocity
	    Map<String,Object> model = new HashMap<String,Object>();
	    model.put("id",userOid);
	    model.put("token",securityToken);
	    model.put("redirectUrl", redirectUrl);

	    VelocityContext context = new VelocityContext(model);
	    StringWriter sw = new StringWriter();
		velocityEngine.mergeTemplate(alertMsgTemplatePath.asRelativeString(),
			  						  R01F.DEFAULT_CHARSET.name(),
			  						  context,
			  						  sw);
		sw.flush();
	    return sw.toString();
	}
}
