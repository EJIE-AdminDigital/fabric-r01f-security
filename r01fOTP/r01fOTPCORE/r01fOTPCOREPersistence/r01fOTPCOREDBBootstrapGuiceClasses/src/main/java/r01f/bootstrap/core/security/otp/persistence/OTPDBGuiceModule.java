package r01f.bootstrap.core.security.otp.persistence;

import com.google.inject.Binder;

import lombok.EqualsAndHashCode;
import r01f.services.bootstrap.core.db.guice.DBPersistenceGuiceModuleBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;


@EqualsAndHashCode(callSuper=true)				// This is important for guice modules
  public class OTPDBGuiceModule
  	   extends DBPersistenceGuiceModuleBase {	
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public OTPDBGuiceModule(final DBModuleConfig cfg) {
		super(cfg);
	}
	
	@Override
	public void configure(final Binder binder) {
		super.configure(binder);
	}
}
