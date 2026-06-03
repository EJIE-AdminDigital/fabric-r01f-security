package r01f.bootstrap.core.security;

import java.util.Properties;

import jakarta.inject.Provider;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.PrivateBinder;
import com.google.inject.Singleton;

import lombok.extern.slf4j.Slf4j;
import r01f.core.services.notifier.annotations.UseEMailNotifier;
import r01f.core.services.notifier.annotations.UseLogNotifier;
import r01f.core.services.notifier.annotations.UseMessagingNotifier;
import r01f.core.services.notifier.config.NotifierConfigForEMail;
import r01f.core.services.notifier.config.NotifierConfigForLog;
import r01f.core.services.notifier.config.NotifierConfigForSMS;
import r01f.core.services.notifier.config.NotifierEnums.NotifierType;
import r01f.core.services.notifier.config.NotifiersConfigs;
import r01f.internal.notifier.security.user.passwordrecover.UserPasswordRecoverNotifier;
import r01f.internal.notifier.security.user.passwordrecover.UserPasswordRecoverNotifierEMailImpl;
import r01f.internal.notifier.security.user.passwordrecover.UserPasswordRecoverNotifierLoggerImpl;
import r01f.internal.notifier.security.user.passwordrecover.UserPasswordRecoverNotifierSMSImpl;
import r01f.internal.notifier.security.user.registration.UserRegistrationNotifier;
import r01f.internal.notifier.security.user.registration.UserRegistrationNotifierEMailImpl;
import r01f.internal.notifier.security.user.registration.UserRegistrationNotifierLoggerImpl;
import r01f.internal.notifier.security.user.registration.UserRegistrationNotifierSMSImpl;
import r01f.util.types.collections.Lists;

@Slf4j
public class NotifierGuiceModuleForSecurity
  implements Module {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final boolean _installedInsideGuicePrivateModule;

	protected final NotifiersConfigs _notifierConfigs;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public NotifierGuiceModuleForSecurity(final NotifiersConfigs notifierConfigs) {
		this(notifierConfigs,
			 true);	// usually it's installed inside a private Guice Module
	}
	public NotifierGuiceModuleForSecurity(final NotifiersConfigs notifierConfigs,
										  final boolean installedInsideGuicePrivateModule) {
		if (notifierConfigs == null) throw new IllegalArgumentException("Notifier configs are mandatory!");
		_installedInsideGuicePrivateModule = installedInsideGuicePrivateModule;
		_notifierConfigs = notifierConfigs;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  MODULE
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void configure(final Binder binder) {
		_install(binder);
		_configureCommonBinding(binder);
		_configure(binder);
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	PROTECTED METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	protected void _configure(final Binder binder) {
		// bind user password recovery notifier
		_bindUserPasswordRecoverNotifier(binder);

		// bind user registration notifier
		_bindUserRegistrationNotifier(binder);

		// Velocity engine to create the messages
		_bindTemplateEngine(binder);
	}

	protected void _configureCommonBinding(final Binder binder) {
		// bind common notifier
		_bindCommonNotifier(binder);
	}

	protected void _install(final Binder binder) {
		// Nothing by default
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	protected void _bindUserPasswordRecoverNotifier(final Binder binder) {
		if (_notifierConfigs == null
		 || _notifierConfigs.doesNOTContainAnyConfiguredNotifier()
		 || !_notifierConfigs.isAnyNotifierConfigured(Lists.newArrayList(NotifierType.EMAIL,NotifierType.LOG,NotifierType.SMS))) {
			log.error("Could NOT bind any [user password recovery message notifier]: no notifier was configured!");
			return;
		}
		if (_notifierConfigs.isEMailNotifierConfigured()) {
			binder.bind(UserPasswordRecoverNotifier.class)
				  .annotatedWith(UseEMailNotifier.class)
				  .to(UserPasswordRecoverNotifierEMailImpl.class)	// gets injected with a java mail sender (see provider below)
				  .in(Singleton.class);
			if (_installedInsideGuicePrivateModule && binder instanceof PrivateBinder) ((PrivateBinder)binder).expose(Key.get(UserPasswordRecoverNotifier.class,UseEMailNotifier.class));
		}
		if (_notifierConfigs.isLogNotifierConfigured()) {
			binder.bind(UserPasswordRecoverNotifier.class)
				  .annotatedWith(UseLogNotifier.class)
				  .to(UserPasswordRecoverNotifierLoggerImpl.class)
				  .in(Singleton.class);
			if (_installedInsideGuicePrivateModule && binder instanceof PrivateBinder) ((PrivateBinder)binder).expose(Key.get(UserPasswordRecoverNotifier.class,UseLogNotifier.class));
		}
		if (_notifierConfigs.isMessagingNotifierConfigured()) {
			binder.bind(UserPasswordRecoverNotifier.class)
				  .annotatedWith(UseMessagingNotifier.class)
				  .to(UserPasswordRecoverNotifierSMSImpl.class)		// gets injected with SMS notifier service (see provider below)
				  .in(Singleton.class);
			if (_installedInsideGuicePrivateModule && binder instanceof PrivateBinder) ((PrivateBinder)binder).expose(Key.get(UserPasswordRecoverNotifier.class,UseMessagingNotifier.class));
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	protected void _bindUserRegistrationNotifier(final Binder binder) {
		if (_notifierConfigs == null
		 || _notifierConfigs.doesNOTContainAnyConfiguredNotifier()
		 || !_notifierConfigs.isAnyNotifierConfigured(Lists.newArrayList(NotifierType.EMAIL,NotifierType.LOG,NotifierType.SMS))) {
			log.error("Could NOT bind any [user regisstration message notifier]: no notifier was configured!");
			return;
		}
		if (_notifierConfigs.isEMailNotifierConfigured()) {
			binder.bind(UserRegistrationNotifier.class)
				  .annotatedWith(UseEMailNotifier.class)
				  .to(UserRegistrationNotifierEMailImpl.class)	// gets injected with a java mail sender (see provider below)
				  .in(Singleton.class);
			if (_installedInsideGuicePrivateModule && binder instanceof PrivateBinder) ((PrivateBinder)binder).expose(Key.get(UserRegistrationNotifier.class,UseEMailNotifier.class));
		}
		if (_notifierConfigs.isLogNotifierConfigured()) {
			binder.bind(UserRegistrationNotifier.class)
				  .annotatedWith(UseLogNotifier.class)
				  .to(UserRegistrationNotifierLoggerImpl.class)
				  .in(Singleton.class);
			if (binder instanceof PrivateBinder) ((PrivateBinder)binder).expose(Key.get(UserRegistrationNotifier.class,UseLogNotifier.class));
		}
		if (_notifierConfigs.isMessagingNotifierConfigured()) {
			binder.bind(UserRegistrationNotifier.class)
				  .annotatedWith(UseMessagingNotifier.class)
				  .to(UserRegistrationNotifierSMSImpl.class)		// gets injected with SMS notifier service (see provider below)
				  .in(Singleton.class);
			if (_installedInsideGuicePrivateModule && binder instanceof PrivateBinder) ((PrivateBinder)binder).expose(Key.get(UserRegistrationNotifier.class,UseMessagingNotifier.class));
		}
	}

/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	protected void _bindCommonNotifier(final Binder binder) {
		if (_notifierConfigs == null
		 || _notifierConfigs.doesNOTContainAnyConfiguredNotifier()
		 || !_notifierConfigs.isAnyNotifierConfigured(Lists.newArrayList(NotifierType.EMAIL,NotifierType.LOG,NotifierType.SMS))) {
			log.error("Could NOT bind any [user regisstration message notifier]: no notifier was configured!");
			return;
		}
		if (_notifierConfigs.isEMailNotifierConfigured()) {
			// [1] - bind the config
			binder.bind(NotifierConfigForEMail.class)
				  .toInstance(_notifierConfigs.getForEMail());
			//if (_installedInsideGuicePrivateModule && binder instanceof PrivateBinder) ((PrivateBinder)binder).expose(Key.get(NotifierConfigForEMail.class));
		}
		if (_notifierConfigs.isLogNotifierConfigured()) {
			// [1] - Bind Config
			binder.bind(NotifierConfigForLog.class)
				  .toInstance(_notifierConfigs.getForLog());
			//if (_installedInsideGuicePrivateModule && binder instanceof PrivateBinder) ((PrivateBinder)binder).expose(Key.get(NotifierConfigForLog.class));
		}
		if (_notifierConfigs.isMessagingNotifierConfigured()) {
			// [1] - Bind Config
			binder.bind(NotifierConfigForSMS.class)
				  .toInstance(_notifierConfigs.getForSMS());
			//if (_installedInsideGuicePrivateModule && binder instanceof PrivateBinder) ((PrivateBinder)binder).expose(Key.get(NotifierConfigForSMS.class));
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	VELOCITY: used to create the notified messages
/////////////////////////////////////////////////////////////////////////////////////////
	protected void _bindTemplateEngine(final Binder binder) {
		binder.bind(VelocityEngine.class)
			  .toProvider((Provider<VelocityEngine>) () -> {
								Properties velocityProps = new Properties();
								velocityProps.put(RuntimeConstants.DEFAULT_RUNTIME_LOG_NAME,"r01NotifierVelocity");
								velocityProps.put(RuntimeConstants.RESOURCE_LOADER,"class");
								velocityProps.setProperty("class.resource.loader.class",ClasspathResourceLoader.class.getName());

								VelocityEngine outVelocityEngine = new VelocityEngine();
								outVelocityEngine.setProperties(velocityProps);
								return outVelocityEngine;
						  })
			  .in(Singleton.class);
	}
}
