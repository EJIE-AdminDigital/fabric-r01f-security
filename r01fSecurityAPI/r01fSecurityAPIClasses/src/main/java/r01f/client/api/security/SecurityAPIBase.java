package r01f.client.api.security;

import java.util.Map;

import com.google.common.collect.Maps;

import jakarta.inject.Provider;
import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.authorization.CRUDServicesForAuthorizationTargetResource;
import r01f.api.interfaces.security.authorization.CRUDServicesForUserAuthorizationOnResource;
import r01f.api.interfaces.security.authorization.CountServicesForAuthorizationTargetResource;
import r01f.api.interfaces.security.authorization.CountServicesForUserAuthorizationOnResource;
import r01f.api.interfaces.security.authorization.FindServicesForAuthorizationTargetResource;
import r01f.api.interfaces.security.authorization.FindServicesForUserAuthorizationOnResource;
import r01f.api.interfaces.security.login.entry.CRUDServicesForUserLoginEntry;
import r01f.api.interfaces.security.login.entry.FindServicesForUserLoginEntry;
import r01f.api.interfaces.security.login.giltza.CRUDServicesForGiltzaLogin;
import r01f.api.interfaces.security.login.giltza.FindServicesForGiltzaLogin;
import r01f.api.interfaces.security.login.google.CRUDServicesForGoogleLogin;
import r01f.api.interfaces.security.login.google.FindServicesForGoogleLogin;
import r01f.api.interfaces.security.login.pairedphone.CRUDServicesForPairedPhoneLogin;
import r01f.api.interfaces.security.login.pairedphone.FindServicesForPairedPhoneLogin;
import r01f.api.interfaces.security.login.pairedphone.PairedPhoneLoginConsentServices;
import r01f.api.interfaces.security.login.saml.CRUDServicesForSAMLLogin;
import r01f.api.interfaces.security.login.saml.FindServicesForSAMLLogin;
import r01f.api.interfaces.security.login.userpassword.CRUDServicesForUserPasswordLogin;
import r01f.api.interfaces.security.login.userpassword.FindServicesForUserPasswordLogin;
import r01f.api.interfaces.security.login.userpassword.UserPasswordLoginServices;
import r01f.api.interfaces.security.login.xlnets.CRUDServicesForXLNetsLogin;
import r01f.api.interfaces.security.login.xlnets.FindServicesForXLNetsLogin;
import r01f.api.interfaces.security.user.CRUDServicesForUser;
import r01f.api.interfaces.security.user.FindServicesForUser;
import r01f.api.interfaces.security.user.SearchServicesForUser;
import r01f.client.api.sub.security.authorization.AuthorizationTargetResourceSubApi;
import r01f.client.api.sub.security.authorization.UserAuthOnResourceSubApi;
import r01f.client.api.sub.security.login.SecurityProviderLoginSubApiBase;
import r01f.client.api.sub.security.login.entry.UserLoginEntrySubApi;
import r01f.client.api.sub.security.login.giltza.GiltzaLoginSubApi;
import r01f.client.api.sub.security.login.google.GoogleLoginSubApi;
import r01f.client.api.sub.security.login.pairedphone.PairedPhoneLoginSubApi;
import r01f.client.api.sub.security.login.saml.SAMLLoginSubApi;
import r01f.client.api.sub.security.login.userpassword.UserPasswordLoginSubApi;
import r01f.client.api.sub.security.login.xlnets.XLNetsLoginSubApi;
import r01f.client.api.sub.security.user.UserSubApi;
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
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.services.api.ClientAPIImplBase;
import r01f.services.interfaces.ServiceInterface;
import r01f.types.contact.HasMailAddress;
import r01f.types.contact.HasPhoneNumber;

@Accessors(prefix="_")
public abstract class SecurityAPIBase<U extends User & HasMailAddress & HasPhoneNumber,
									  E extends UserLoginEntry,
									  R extends AuthorizationTargetResource,S extends SummarizedAuthorizationResource<R>,
									  A extends UserAuthorizationOnResource>
			  extends ClientAPIImplBase
		   implements SecurityAPI<U,E,R,S,A> {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	private final Map<SecurityProviderID,? super SecurityProviderLoginSubApiBase<?>> _subApis = Maps.newHashMap();

	@Getter private final UserSubApi<U> _usersApi;
	@Getter private final UserLoginEntrySubApi<E> _userLoginEntriesApi;
	@Getter private final AuthorizationTargetResourceSubApi<R,S> _authorizationTargetResourcesApi;
	@Getter private final UserAuthOnResourceSubApi<A> _userAuthorizationsOnResourcesApi;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("rawtypes")
	protected SecurityAPIBase(final Provider<SecurityContext> securityContextProvider,
							  final Marshaller modelObjectsMarshaller,
							  final Map<Class,ServiceInterface> usersServiceInterfaceTypesToImplOrProxy,	// comes from injection
							  // user
							  final Class<? extends CRUDServicesForUser<U>> userCrudType,final Class<? extends FindServicesForUser<U>> userFindType,
							  // user login entries
							  final Class<? extends CRUDServicesForUserLoginEntry<E>> loginEntriesCrudType,final Class<? extends FindServicesForUserLoginEntry<E>> loginEntriesFindType,
							  // auth target resources
							  final Class<? extends CRUDServicesForAuthorizationTargetResource<R>> authorizationTargetResourceCrudType,final Class<? extends FindServicesForAuthorizationTargetResource<R>> authorizationTargetResourceFindType,final Class<? extends CountServicesForAuthorizationTargetResource<R>> authorizationTargetResourceCountType,
							  // user auth on resource
							  final Class<? extends CRUDServicesForUserAuthorizationOnResource<A>> userAuthOnResourceCrudType,final Class<? extends FindServicesForUserAuthorizationOnResource<A>> userAuthOnResourceFindType,final Class<? extends CountServicesForUserAuthorizationOnResource<A>> userAuthOnResourceCountType) {
		this(securityContextProvider,
			 modelObjectsMarshaller,
			 usersServiceInterfaceTypesToImplOrProxy,
			 userCrudType,userFindType,null,	// no user search
			 loginEntriesCrudType,loginEntriesFindType,
			 authorizationTargetResourceCrudType,authorizationTargetResourceFindType,authorizationTargetResourceCountType,
			 userAuthOnResourceCrudType,userAuthOnResourceFindType,userAuthOnResourceCountType);
	}
	@SuppressWarnings("rawtypes")
	protected SecurityAPIBase(final Provider<SecurityContext> securityContextProvider,
							  final Marshaller modelObjectsMarshaller,
							  final Map<Class,ServiceInterface> usersServiceInterfaceTypesToImplOrProxy,	// comes from injection
							  // user
							  final Class<? extends CRUDServicesForUser<U>> userCrudType,final Class<? extends FindServicesForUser<U>> userFindType,final Class<? extends SearchServicesForUser<U,?,?>> userSearchType,
							  // user login entries
							  final Class<? extends CRUDServicesForUserLoginEntry<E>> loginEntriesCrudType,final Class<? extends FindServicesForUserLoginEntry<E>> loginEntriesFindType,
							  // auth target resources
							  final Class<? extends CRUDServicesForAuthorizationTargetResource<R>> authorizationTargetResourceCrudType,final Class<? extends FindServicesForAuthorizationTargetResource<R>> authorizationTargetResourceFindType,final Class<? extends CountServicesForAuthorizationTargetResource<R>> authorizationTargetResourceCountType,
							  // user auth on resource
							  final Class<? extends CRUDServicesForUserAuthorizationOnResource<A>> userAuthOnResourceCrudType,final Class<? extends FindServicesForUserAuthorizationOnResource<A>> userAuthOnResourceFindType,final Class<? extends CountServicesForUserAuthorizationOnResource<A>> userAuthOnResourceCountType) {
		// Services proxy
		super(securityContextProvider,
			  modelObjectsMarshaller,
			  usersServiceInterfaceTypesToImplOrProxy);
		// Create the sub apis
		_usersApi = new UserSubApi<>(this.getSecurityContextProvider(),
								 	 this.getModelObjectsMarshaller(),
								 	 this.getServiceInterfaceMappings(),
								 	 // services
								 	 userCrudType,userFindType,userSearchType);
		_userLoginEntriesApi = new UserLoginEntrySubApi<>(this.getSecurityContextProvider(),
								 			  			  this.getModelObjectsMarshaller(),
								 			  			  this.getServiceInterfaceMappings(),
								 			  			  // services
								 			  			  loginEntriesCrudType,loginEntriesFindType);
		_authorizationTargetResourcesApi = new AuthorizationTargetResourceSubApi<>(this.getSecurityContextProvider(),
								 			  	 					 			   this.getModelObjectsMarshaller(),
								 			  	 					 			   this.getServiceInterfaceMappings(),
								 			  	 					 			   // services
								 			  	 					 			   authorizationTargetResourceCrudType,authorizationTargetResourceFindType,authorizationTargetResourceCountType);
		_userAuthorizationsOnResourcesApi = new UserAuthOnResourceSubApi<>(this.getSecurityContextProvider(),
								 			  							   this.getModelObjectsMarshaller(),
								 			  							   this.getServiceInterfaceMappings(),
								 			  							   // services
								 			  							   userAuthOnResourceCrudType,userAuthOnResourceFindType,userAuthOnResourceCountType);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	USER PASSWORD
/////////////////////////////////////////////////////////////////////////////////////////
	public <L extends UserPasswordLogin> UserPasswordLoginSubApi<L> addUserPasswordLoginApiUsing(final Class<? extends CRUDServicesForUserPasswordLogin<L>> userPasswordCrudType,final Class<? extends FindServicesForUserPasswordLogin<L>> userPasswordFindType,
							  									   							     final Class<? extends UserPasswordLoginServices<U,L>> userPasswordloginServicesType) {
		UserPasswordLoginSubApi<L> api = new UserPasswordLoginSubApi<>(this.getSecurityContextProvider(),
								 			  				   		   this.getModelObjectsMarshaller(),
								 			  				   		   this.getServiceInterfaceMappings(),
								 			  				   		   // services
								 			  				   		   userPasswordCrudType,userPasswordFindType,
								 			  				   		   userPasswordloginServicesType);
		_subApis.put(SecurityProviderID.USER_PASSWORD,api);
		return api;
	}
	@Override
	@SuppressWarnings("unchecked")
	public <L extends UserPasswordLogin> UserPasswordLoginSubApi<L> getUserPasswordLoginApi() {
		UserPasswordLoginSubApi<L> outApi = (UserPasswordLoginSubApi<L>)_subApis.get(SecurityProviderID.USER_PASSWORD);
		if (outApi == null) throw new IllegalStateException(SecurityProviderID.USER_PASSWORD + " security provider api was NOT set!");
		return outApi;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	GOOGLE
/////////////////////////////////////////////////////////////////////////////////////////
	public <GL extends GoogleLogin> GoogleLoginSubApi<GL> addGoogleLoginApiUsing(final Class<? extends CRUDServicesForGoogleLogin<GL>> googleLoginCrudType,final Class<? extends FindServicesForGoogleLogin<GL>> googleLoginFindType) {
		GoogleLoginSubApi<GL> api = new GoogleLoginSubApi<>(this.getSecurityContextProvider(),
								 			  	    	    this.getModelObjectsMarshaller(),
								 			  	    	    this.getServiceInterfaceMappings(),
								 			  	    		// services
								 			  	    		googleLoginCrudType,googleLoginFindType);
		_subApis.put(SecurityProviderID.GOOGLE,api);
		return api;
	}
	@Override
	@SuppressWarnings("unchecked")
	public <GL extends GoogleLogin> GoogleLoginSubApi<GL> getGoogleLoginApi() {
		GoogleLoginSubApi<GL> outApi = (GoogleLoginSubApi<GL>)_subApis.get(SecurityProviderID.GOOGLE);
		if (outApi == null) throw new IllegalStateException(SecurityProviderID.GOOGLE + " security provider api was NOT set!");
		return outApi;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SAML
/////////////////////////////////////////////////////////////////////////////////////////
	public <SL extends SAMLLogin> SAMLLoginSubApi<SL> addSAMLLoginApiUsing(final Class<? extends CRUDServicesForSAMLLogin<SL>> samlLoginCrudType,final Class<? extends FindServicesForSAMLLogin<SL>> samlLoginFindType) {
		SAMLLoginSubApi<SL> api = new SAMLLoginSubApi<>(this.getSecurityContextProvider(),
								 			  		    this.getModelObjectsMarshaller(),
								 			  			this.getServiceInterfaceMappings(),
								 			  			// services
								 			  			samlLoginCrudType,samlLoginFindType);
		_subApis.put(SecurityProviderID.SAML,api);
		return api;
	}
	@Override
	@SuppressWarnings("unchecked")
	public <SL extends SAMLLogin> SAMLLoginSubApi<SL> getSAMLLoginApi() {
		SAMLLoginSubApi<SL> outApi = (SAMLLoginSubApi<SL>)_subApis.get(SecurityProviderID.SAML);
		if (outApi == null) throw new IllegalStateException(SecurityProviderID.SAML + " security provider api was NOT set!");
		return outApi;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	XLNETS
/////////////////////////////////////////////////////////////////////////////////////////
	public <XL extends XLNetsLogin> XLNetsLoginSubApi<XL> addXlnetsLoginApiUsing(final Class<? extends CRUDServicesForXLNetsLogin<XL>> xlNetsLoginCrudType,final Class<? extends FindServicesForXLNetsLogin<XL,?>> xlNetsLoginFindType) {
		XLNetsLoginSubApi<XL> api = new XLNetsLoginSubApi<>(this.getSecurityContextProvider(),
								 			  	    		this.getModelObjectsMarshaller(),
								 			  	    		this.getServiceInterfaceMappings(),
								 			  	    		// services
								 			  	    		xlNetsLoginCrudType,xlNetsLoginFindType);
		_subApis.put(SecurityProviderID.XLNETS,api);
		return api;
	}
	@Override
	@SuppressWarnings("unchecked")
	public <XL extends XLNetsLogin> XLNetsLoginSubApi<XL> getXlnetsLoginApi() {
		XLNetsLoginSubApi<XL> outApi = (XLNetsLoginSubApi<XL>)_subApis.get(SecurityProviderID.XLNETS);
		if (outApi == null) throw new IllegalStateException(SecurityProviderID.XLNETS + " security provider api was NOT set!");
		return outApi;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	GILTZA
/////////////////////////////////////////////////////////////////////////////////////////
	public <G extends GiltzaLogin> GiltzaLoginSubApi<G> addGiltzaLoginApiUsing(final Class<? extends CRUDServicesForGiltzaLogin<G>> giltzaLoginCrudType,final Class<? extends FindServicesForGiltzaLogin<G>> giltzaLoginFindType) {
		GiltzaLoginSubApi<G> api = new GiltzaLoginSubApi<>(this.getSecurityContextProvider(),
								 			  	    	   this.getModelObjectsMarshaller(),
								 			  	    	   this.getServiceInterfaceMappings(),
								 			  	    	   // services
								 			  	    	   giltzaLoginCrudType,giltzaLoginFindType);
		_subApis.put(SecurityProviderID.GILTZA,api);
		return api;
	}
	@Override
	@SuppressWarnings("unchecked")
	public <G extends GiltzaLogin> GiltzaLoginSubApi<G> getGiltzaLoginApi() {
		GiltzaLoginSubApi<G> outApi = (GiltzaLoginSubApi<G>)_subApis.get(SecurityProviderID.GILTZA);
		if (outApi == null) throw new IllegalStateException(SecurityProviderID.GILTZA + " security provider api was NOT set!");
		return outApi;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	PHONE TOKEN
/////////////////////////////////////////////////////////////////////////////////////////
	public <P extends PairedPhoneLogin,
			C extends PairedPhoneLoginConsent> PairedPhoneLoginSubApi<P,C> addPairedPhoneLoginApiUsing(final Class<? extends CRUDServicesForPairedPhoneLogin<P>> pairedPhoneLoginCrudType,final Class<? extends FindServicesForPairedPhoneLogin<P>> pairedPhoneLoginFindType,
																									   final Class<? extends PairedPhoneLoginConsentServices<C>> consentServicesType) {
		PairedPhoneLoginSubApi<P,C> api = new PairedPhoneLoginSubApi<>(this.getSecurityContextProvider(),
								 			  	    	   			   this.getModelObjectsMarshaller(),
								 			  	    	   			   this.getServiceInterfaceMappings(),
								 			  	    	   			   // services
								 			  	    	   			   pairedPhoneLoginCrudType,pairedPhoneLoginFindType,
								 			  	    	   			   consentServicesType);
		_subApis.put(SecurityProviderID.PAIRED_PHONE,api);
		return api;
	}
	@SuppressWarnings("unchecked")
	public <P extends PairedPhoneLogin,
			C extends PairedPhoneLoginConsent> PairedPhoneLoginSubApi<P,C> getPairedPhoneLoginApi() {
		PairedPhoneLoginSubApi<P,C> outApi = (PairedPhoneLoginSubApi<P,C>)_subApis.get(SecurityProviderID.PAIRED_PHONE);
		if (outApi == null) throw new IllegalStateException(SecurityProviderID.PAIRED_PHONE + " security provider api was NOT set!");
		return outApi;
	}
	@Override
	@SuppressWarnings("unchecked")
	public <P extends PairedPhoneLogin,
			C extends PairedPhoneLoginConsent> PairedPhoneLoginSubApi<P,C> getPairedPhoneLoginApi(final Class<P> pairedPhoneLogin,final Class<C> pairedPhoneConsent) {
		PairedPhoneLoginSubApi<P,C> outApi = (PairedPhoneLoginSubApi<P,C>)_subApis.get(SecurityProviderID.PAIRED_PHONE);
		if (outApi == null) throw new IllegalStateException(SecurityProviderID.PAIRED_PHONE + " security provider api was NOT set!");
		return outApi;
	}
}
