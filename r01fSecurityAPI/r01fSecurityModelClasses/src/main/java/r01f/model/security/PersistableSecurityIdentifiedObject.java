package r01f.model.security;

import r01f.securitycontext.SecurityIDS.IsSecurityObjectID;
import r01f.securitycontext.SecurityOIDs.IsSecurityObjectOID;

/**
 * Interface for every  for P12PersistableObject with an ID and an OID
 * @param <O>
 * @param <ID>
 */
public interface PersistableSecurityIdentifiedObject<O extends IsSecurityObjectOID,
													 ID extends IsSecurityObjectID>
		 extends PersistableSecurityObject<O> {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public ID getId();

	public void setId(ID id);

}
