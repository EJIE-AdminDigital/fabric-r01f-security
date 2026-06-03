package r01f.bootstrap.core.security;

import lombok.EqualsAndHashCode;
import r01f.services.bootstrap.core.db.guice.DBPersistenceGuiceModuleBase;
import r01f.services.core.persistence.db.config.DBModuleConfig;


@EqualsAndHashCode(callSuper=true)				// This is important for guice modules
 public class DBGuiceModuleForSecurity
 	  extends DBPersistenceGuiceModuleBase {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public DBGuiceModuleForSecurity(final DBModuleConfig cfg) {
		super(cfg);
	}
}
