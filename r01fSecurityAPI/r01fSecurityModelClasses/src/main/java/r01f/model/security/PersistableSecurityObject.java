package r01f.model.security;

import r01f.model.IndexableModelObject;
import r01f.model.PersistableModelObject;
import r01f.securitycontext.SecurityOIDs.IsSecurityObjectOID;

/**
 * Interface for every P12 Model Object
 * @param <O>
 * @param <ID>
 */
public interface PersistableSecurityObject<O extends IsSecurityObjectOID>
		 extends PersistableModelObject<O>,			// is persistable
				 IndexableModelObject,				// is indexable
				 SecurityObject {					// it's a security object

}
