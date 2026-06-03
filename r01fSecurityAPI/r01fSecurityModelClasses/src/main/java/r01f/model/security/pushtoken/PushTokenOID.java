package r01f.model.security.pushtoken;

import lombok.NoArgsConstructor;
import r01f.annotations.Immutable;
import r01f.guids.OIDBaseMutable;
import r01f.guids.OIDs;
import r01f.guids.PersistableObjectOID;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.securitycontext.SecurityOIDs.SecurityObjectOID;

@Immutable
@MarshallType(as="phoneTokenOid")
@NoArgsConstructor
public class PushTokenOID
	 extends OIDBaseMutable<String> 
  implements PersistableObjectOID, 
  			 SecurityObjectOID {

	private static final long serialVersionUID = -2720782897077004303L;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public PushTokenOID(final String oid) {
		super(oid);
	}
	public static PushTokenOID valueOf(final String s) {
		return PushTokenOID.forId(s);
	}
	public static PushTokenOID fromString(final String s) {
		return PushTokenOID.forId(s);
	}
	public static PushTokenOID forId(final String id) {
		return new PushTokenOID(id);
	}
	public static PushTokenOID supply() {
		return PushTokenOID.forId(OIDs.supplyOid());
	}
}