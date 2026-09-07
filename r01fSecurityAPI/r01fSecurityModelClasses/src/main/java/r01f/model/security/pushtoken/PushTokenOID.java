package r01f.model.security.pushtoken;

import lombok.Getter;
import r01f.annotations.Immutable;
import r01f.guids.OIDTyped;
import r01f.guids.OIDs;
import r01f.guids.PersistableObjectOID;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.securitycontext.SecurityOIDs.IsSecurityObjectOID;

@Immutable
@MarshallType(as="phoneTokenOid")
public record PushTokenOID(@Getter String id)
   implements OIDTyped<String>,
  			  PersistableObjectOID, 
  			  IsSecurityObjectOID {
	
	public static PushTokenOID from(final String id) {
		return new PushTokenOID(id);
	}
	public static PushTokenOID forId(final String id) {
		return new PushTokenOID(id);
	}
	public static PushTokenOID valueOf(final String s) {
		return PushTokenOID.forId(s);
	}
	public static PushTokenOID fromString(final String s) {
		return PushTokenOID.forId(s);
	}
	public static PushTokenOID supply() {
		return PushTokenOID.forId(OIDs.supplyOid());
	}
	
	@Override
	public String asString() {
		return this.id;
	}
	@Override
	public String toString() {
		return this.id;
	}
}