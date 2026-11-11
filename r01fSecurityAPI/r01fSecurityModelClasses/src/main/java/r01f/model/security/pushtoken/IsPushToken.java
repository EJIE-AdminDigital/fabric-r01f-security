package r01f.model.security.pushtoken;

import r01f.model.security.IsSecurityPersistableObject;
import r01f.oids.CommonOIDs.SystemID;
import r01f.oids.SecurityIDs.SecurityToken;

public interface IsPushToken 
	     extends IsSecurityPersistableObject<PushTokenOID> {
	
	public SystemID getSourceSystemId();
	public void setSourceSystemId(final SystemID id);
	
	public IsPushDestinationID getDestinationId();
	public void setDestinationId(final IsPushDestinationID id);
	
	public PushDestinationPlatform getDestinationPlatform();
	public void setDestinationPlatform(final PushDestinationPlatform plataform);
	
	public SecurityToken getToken();
	public void setToken(final SecurityToken token);

}
