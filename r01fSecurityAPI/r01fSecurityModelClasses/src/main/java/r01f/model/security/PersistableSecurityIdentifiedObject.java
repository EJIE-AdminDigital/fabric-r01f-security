package r01f.model.security;

import r01f.securitycontext.SecurityIDS.IsSecurityID;
import r01f.securitycontext.SecurityOIDs.SecurityObjectOID;

/**
 * Interface for every  for P12PersistableObject with an ID and an OID
 * @param <O>
 * @param <ID>
 */
public interface PersistableSecurityIdentifiedObject<O extends SecurityObjectOID,
													 ID extends IsSecurityID>
		 extends PersistableSecurityObject<O> {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public ID getId();

	public void setId(ID id);

}
