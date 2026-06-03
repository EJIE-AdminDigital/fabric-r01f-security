package r01f.db.entities.security;

import r01f.model.HasTrackingInfo;
import r01f.model.facets.HasEntityVersion;
import r01f.services.core.persistence.db.DBEntityHasOID;
import r01f.services.core.persistence.db.entities.DBEntityForModelObject;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;

public interface DBEntityForSecurityObject 
		 extends DBEntityForModelObject<DBPrimaryKeyForModelObject>,
		 		 DBEntityHasOID,
		 		 HasEntityVersion,
		   		 HasTrackingInfo {
	// just an interface
}
