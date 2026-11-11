package r01f.model.security.pushtoken;

import lombok.Getter;
import r01f.annotations.Immutable;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.OIDs;
import r01f.oids.SecurityOIDs.IsSecurityPersistableObjectOID;

@Immutable
@MarshallType(as="phoneTokenOid")
public record PushTokenOID(@Getter String id)
   implements IsSecurityPersistableObjectOID {
	
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