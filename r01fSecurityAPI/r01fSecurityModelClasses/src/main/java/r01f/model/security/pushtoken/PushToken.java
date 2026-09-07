package r01f.model.security.pushtoken;

import r01f.guids.CommonOIDs.SystemID;
import r01f.model.security.PersistableSecurityObject;
import r01f.securitycontext.SecurityIDS.SecurityToken;

public interface PushToken 
	     extends PersistableSecurityObject<PushTokenOID> {
	
	public SystemID getSystemId();
	public void setSystemId(final SystemID id);
	
	public IsPushTokenID getPushTokenId();
	public void setPushTokenId(final IsPushTokenID id);
	
	public SecurityToken getToken();
	public void setToken(final SecurityToken token);
	
	public PushTokenDevicePlataform getPlataform();
	public void  setPlataform(final PushTokenDevicePlataform plataform);
}
