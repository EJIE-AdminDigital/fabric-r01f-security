package r01f.model.security.otp;

import r01f.facets.HasOID;
import r01f.model.ModelObject;
import r01f.model.PersistableModelObject;
import r01f.model.security.otp.oids.OTPOIDs.IsOTPOID;

public interface OTPPersistableObject<O extends IsOTPOID>
	     extends PersistableModelObject<O>,
	     		 HasOID<O>,
	     		 ModelObject {
	// just a marker interface
}