package r01f.bootstrap.core.security.webauthn;

import lombok.EqualsAndHashCode;
import r01f.services.bootstrap.core.db.guice.DBPersistenceGuiceModuleBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;


@EqualsAndHashCode(callSuper=true)				// This is important for guice modules
 public class DBGuiceModuleForWebAuthnSecurity
 	  extends DBPersistenceGuiceModuleBase {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBGuiceModuleForWebAuthnSecurity(final DBModuleConfig cfg) {
		super(cfg);
	}
}
