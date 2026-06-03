package r01f.client.api.security;

import lombok.experimental.Accessors;
import r01f.client.api.sub.security.login.giltza.GiltzaLoginSubApi;
import r01f.client.api.sub.security.login.google.GoogleLoginSubApi;
import r01f.client.api.sub.security.login.pairedphone.PairedPhoneLoginSubApi;
import r01f.client.api.sub.security.login.saml.SAMLLoginSubApi;
import r01f.client.api.sub.security.login.userpassword.UserPasswordLoginSubApi;
import r01f.client.api.sub.security.login.xlnets.XLNetsLoginSubApi;
import r01f.model.security.authorization.AuthorizationTargetResource;
import r01f.model.security.authorization.UserAuthorizationOnResource;
import r01f.model.security.login.entry.UserLoginEntry;
import r01f.model.security.login.giltza.GiltzaLogin;
import r01f.model.security.login.google.GoogleLogin;
import r01f.model.security.login.pairedphone.PairedPhoneLogin;
import r01f.model.security.login.pairedphone.PairedPhoneLoginConsent;
import r01f.model.security.login.saml.SAMLLogin;
import r01f.model.security.login.userpassword.UserPasswordLogin;
import r01f.model.security.login.xlnets.XLNetsLogin;
import r01f.model.security.summaries.SummarizedAuthorizationResource;
import r01f.model.security.user.User;

@Accessors(prefix="_")
public interface  SecurityAPI<U extends User,
							  E extends UserLoginEntry,
							  R extends AuthorizationTargetResource,S extends SummarizedAuthorizationResource<R>,
							  A extends UserAuthorizationOnResource> {
/////////////////////////////////////////////////////////////////////////////////////////
//	USER PASSWORD
/////////////////////////////////////////////////////////////////////////////////////////
	public <L extends UserPasswordLogin> UserPasswordLoginSubApi<L> getUserPasswordLoginApi();
/////////////////////////////////////////////////////////////////////////////////////////
//	GOOGLE
/////////////////////////////////////////////////////////////////////////////////////////
	public <GL extends GoogleLogin> GoogleLoginSubApi<GL> getGoogleLoginApi();
/////////////////////////////////////////////////////////////////////////////////////////
//	SAML
/////////////////////////////////////////////////////////////////////////////////////////
	public <SL extends SAMLLogin> SAMLLoginSubApi<SL> getSAMLLoginApi();
/////////////////////////////////////////////////////////////////////////////////////////
//	XLNETS
/////////////////////////////////////////////////////////////////////////////////////////
	public <XL extends XLNetsLogin> XLNetsLoginSubApi<XL> getXlnetsLoginApi();
/////////////////////////////////////////////////////////////////////////////////////////
//	GILTZA
/////////////////////////////////////////////////////////////////////////////////////////
	public <G extends GiltzaLogin> GiltzaLoginSubApi<G> getGiltzaLoginApi();
/////////////////////////////////////////////////////////////////////////////////////////
//	PHONE TOKEN
/////////////////////////////////////////////////////////////////////////////////////////
	public <P extends PairedPhoneLogin,	C extends PairedPhoneLoginConsent> PairedPhoneLoginSubApi<P,C> getPairedPhoneLoginApi(final Class<P> pairedPhoneLogin,final Class<C> pairedPhoneConsent);
}
