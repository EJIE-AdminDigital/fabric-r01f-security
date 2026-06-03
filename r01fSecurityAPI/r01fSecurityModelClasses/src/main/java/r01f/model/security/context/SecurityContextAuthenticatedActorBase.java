package r01f.model.security.context;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.securitycontext.SecurityContextAuthenticatedActor;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;

@Accessors(prefix="_")
abstract class SecurityContextAuthenticatedActorBase 
    implements SecurityContextAuthenticatedActor {
	
	private static final long serialVersionUID = -7186228864961079493L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="securityProviderId",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter protected final SecurityProviderID _securityProviderId;
	
	@MarshallField(as="loginId",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter protected final LoginID _loginId;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public SecurityContextAuthenticatedActorBase(final SecurityProviderID securityProviderId,final LoginID loginId) {
		_securityProviderId = securityProviderId;
		_loginId = loginId;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public boolean isForApp() {
		return this instanceof SecurityContextAuthenticatedActorForApp;
	}
	@Override
	public boolean isForUser() {
		return this instanceof SecurityContextAuthenticatedActorForUser
			|| this instanceof SecurityContextAuthenticatedActorForApiKeyUserBacked;	// not really needed since it extends from SecurityContextAuthenticatedActorForUser
	}
	@Override
	public boolean isForRegisteredDevice() {
		return this instanceof SecurityContextAuthenticatedActorForRegisteredDevice;
	}
	@Override
	public boolean isForSystem() {
		return this instanceof SecurityContextAuthenticatedActorForSystem;
	}
	@Override
	public boolean isForApiKey() {
		return this instanceof SecurityContextAuthenticatedActorForApiKeyUserBacked
			|| this instanceof SecurityContextAuthenticatedActorForApiKeyNotUserBacked;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public <A extends SecurityContextAuthenticatedActor> A as(final Class<A> actorType) {
		return (A)this;
	}
}
