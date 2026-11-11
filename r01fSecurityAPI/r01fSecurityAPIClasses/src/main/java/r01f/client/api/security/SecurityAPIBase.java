package r01f.client.api.security;

import java.util.Map;

import jakarta.inject.Provider;
import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.authorization.CRUDServicesForAuthorizationOnResource;
import r01f.api.interfaces.security.authorization.CRUDServicesForAuthorizationTargetResource;
import r01f.api.interfaces.security.authorization.CountServicesForAuthorizationOnResource;
import r01f.api.interfaces.security.authorization.CountServicesForAuthorizationTargetResource;
import r01f.api.interfaces.security.authorization.FindServicesForAuthorizationOnResource;
import r01f.api.interfaces.security.authorization.FindServicesForAuthorizationTargetResource;
import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.api.interfaces.security.login.FindServicesForLogin;
import r01f.api.interfaces.security.login.PairedPhoneLoginServices;
import r01f.api.interfaces.security.login.UserPasswordLoginServices;
import r01f.api.interfaces.security.login.entry.CRUDServicesForLoginEntry;
import r01f.api.interfaces.security.login.entry.FindServicesForLoginEntry;
import r01f.api.interfaces.security.principal.CRUDServicesForPrincipal;
import r01f.api.interfaces.security.principal.FindServicesForPrincipal;
import r01f.api.interfaces.security.principal.SearchServicesForPrincipal;
import r01f.api.interfaces.security.principal.impersonation.CRUDServicesForPrincipalImpersonation;
import r01f.api.interfaces.security.principal.impersonation.FindServicesForPrincipalImpersonation;
import r01f.api.interfaces.security.principal.impersonation.SecurityPrincipalImpersonationServices;
import r01f.client.api.sub.security.authorization.AuthOnResourceSubApi;
import r01f.client.api.sub.security.authorization.AuthorizationTargetResourceSubApi;
import r01f.client.api.sub.security.login.LoginSubApi;
import r01f.client.api.sub.security.login.entry.LoginEntrySubApi;
import r01f.client.api.sub.security.principal.PrincipalSubApi;
import r01f.client.api.sub.security.principal.impersonation.PrincipalImpersonationSubApi;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.services.api.ClientAPIImplBase;
import r01f.services.interfaces.ServiceInterface;

@Accessors(prefix="_")
public abstract class SecurityAPIBase<P extends IsSecurityPrincipal>
			  extends ClientAPIImplBase
		   implements SecurityAPI<P> {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final PrincipalSubApi<P> _principalsApi;
	@Getter private final PrincipalImpersonationSubApi _principalImpersonationApi;
	@Getter private final LoginSubApi _loginApi;
	@Getter private final LoginEntrySubApi _loginEntriesApi;
	@Getter private final AuthorizationTargetResourceSubApi _authorizationTargetResourcesApi;
	@Getter private final AuthOnResourceSubApi _userAuthorizationsOnResourcesApi;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("rawtypes")
	protected SecurityAPIBase(final Provider<SecurityContext> securityContextProvider,
							  final Marshaller modelObjectsMarshaller,
							  final Map<Class,ServiceInterface> securityServiceInterfaceTypesToImplOrProxy,	// comes from injection
							  // principal
							  final Class<? extends CRUDServicesForPrincipal<P>> principalCrudType,final Class<? extends FindServicesForPrincipal<P>> principalFindType,final Class<? extends SearchServicesForPrincipal<P,?,?>> principalSearchType,
							  // principal impersonatin
							  final Class<? extends CRUDServicesForPrincipalImpersonation> impersonationCrudType,final Class<? extends FindServicesForPrincipalImpersonation> impersonationFindType,final Class<? extends SecurityPrincipalImpersonationServices> impersonationServicesType,
							  // login 
							  final Class<? extends CRUDServicesForLogin> loginCrudType,final Class<? extends FindServicesForLogin> loginFindType,
							  final Class<? extends UserPasswordLoginServices> userPasswordLoginType,
							  final Class<? extends PairedPhoneLoginServices> pairedPhoneLoginType,
							  // login entry
							  final Class<? extends CRUDServicesForLoginEntry> loginEntryCrudType,final Class<? extends FindServicesForLoginEntry> loginEntryFindType,
							  // Authorization target resource
							  final Class<? extends CRUDServicesForAuthorizationTargetResource> authzResCrudType,final Class<? extends FindServicesForAuthorizationTargetResource> authzResFindType,final Class<? extends CountServicesForAuthorizationTargetResource> authzResCountType,
							  // Authorization on target resource
							  final Class<? extends CRUDServicesForAuthorizationOnResource> authzOnResCrudType,final Class<? extends FindServicesForAuthorizationOnResource> authzOnResFindType,final Class<? extends CountServicesForAuthorizationOnResource> authzOnResCountType) {
		// Services proxy
		super(securityContextProvider,
			  modelObjectsMarshaller,
			  securityServiceInterfaceTypesToImplOrProxy);
		// Create the sub apis
		_principalsApi = new PrincipalSubApi<>(this.getSecurityContextProvider(),
								 	 		   this.getModelObjectsMarshaller(),
								 	 		   this.getServiceInterfaceMappings(),
								 	 		   // services
								 	 		   principalCrudType,principalFindType,principalSearchType);
		if (impersonationCrudType != null 
		 && impersonationFindType != null 
		 && impersonationServicesType != null) {
			_principalImpersonationApi = new PrincipalImpersonationSubApi(this.getSecurityContextProvider(),
																  				  this.getModelObjectsMarshaller(),
																  				  this.getServiceInterfaceMappings(),
																  				  // services
																  				  impersonationCrudType,impersonationFindType,impersonationServicesType);
		} else {
			_principalImpersonationApi = null;
		}
		_loginApi = new LoginSubApi(this.getSecurityContextProvider(),
							 		this.getModelObjectsMarshaller(),
							 		this.getServiceInterfaceMappings(),
							 		// services							 		
							 		loginCrudType,loginFindType,
							 		userPasswordLoginType,pairedPhoneLoginType);
		_loginEntriesApi = new LoginEntrySubApi(this.getSecurityContextProvider(),
							 			  		this.getModelObjectsMarshaller(),
							 			  		this.getServiceInterfaceMappings(),
							 			  		// services
							 			  		loginEntryCrudType,loginEntryFindType);
		_authorizationTargetResourcesApi = new AuthorizationTargetResourceSubApi(this.getSecurityContextProvider(),
								 			  	 					 			 this.getModelObjectsMarshaller(),
								 			  	 					 			 this.getServiceInterfaceMappings(),
								 			  	 					 			 authzResCrudType,authzResFindType,authzResCountType);
		_userAuthorizationsOnResourcesApi = new AuthOnResourceSubApi(this.getSecurityContextProvider(),
								 			  						 this.getModelObjectsMarshaller(),
								 			  						 this.getServiceInterfaceMappings(),
								 			  						 // services
								 			  						 authzOnResCrudType,authzOnResFindType,authzOnResCountType);
	}
	@SuppressWarnings("rawtypes")
	protected SecurityAPIBase(final Provider<SecurityContext> securityContextProvider,
							  final Marshaller modelObjectsMarshaller,
							  final Map<Class,ServiceInterface> securityServiceInterfaceTypesToImplOrProxy,	// comes from injection
							  // principal
							  final Class<? extends CRUDServicesForPrincipal<P>> principalCrudType,final Class<? extends FindServicesForPrincipal<P>> principalFindType,
							  // principal impersonatin
							  final Class<? extends CRUDServicesForPrincipalImpersonation> impersonationCrudType,final Class<? extends FindServicesForPrincipalImpersonation> impersonationFindType,final Class<? extends SecurityPrincipalImpersonationServices> impersonationServicesType,
							  // login 
							  final Class<? extends CRUDServicesForLogin> loginCrudType,final Class<? extends FindServicesForLogin> loginFindType,
							  final Class<? extends UserPasswordLoginServices> userPasswordLoginType,
							  final Class<? extends PairedPhoneLoginServices> pairedPhoneLoginType,
							  // login entry
							  final Class<? extends CRUDServicesForLoginEntry> loginEntryCrudType,final Class<? extends FindServicesForLoginEntry> loginEntryFindType,
							  // Authorization target resource
							  final Class<? extends CRUDServicesForAuthorizationTargetResource> authzResCrudType,final Class<? extends FindServicesForAuthorizationTargetResource> authzResFindType,final Class<? extends CountServicesForAuthorizationTargetResource> authzResCountType,
							  // Authorization on target resource
							  final Class<? extends CRUDServicesForAuthorizationOnResource> authzOnResCrudType,final Class<? extends FindServicesForAuthorizationOnResource> authzOnResFindType,final Class<? extends CountServicesForAuthorizationOnResource> authzOnResCountType) {
		this(securityContextProvider,
			 modelObjectsMarshaller,
			 securityServiceInterfaceTypesToImplOrProxy,
			 // principal
			 principalCrudType,principalFindType,null,	// no principal search
			 // impersonation
			 impersonationCrudType,impersonationFindType,impersonationServicesType,
			 // login
			 loginCrudType,loginFindType,
			 userPasswordLoginType,pairedPhoneLoginType,
			 // login entry
			 loginEntryCrudType,loginEntryFindType,
			 // authorization target resource
			 authzResCrudType,authzResFindType,authzResCountType,
			 // authorization on target resource
			 authzOnResCrudType,authzOnResFindType,authzOnResCountType);
	}
	@SuppressWarnings("rawtypes")
	protected SecurityAPIBase(final Provider<SecurityContext> securityContextProvider,
							  final Marshaller modelObjectsMarshaller,
							  final Map<Class,ServiceInterface> securityServiceInterfaceTypesToImplOrProxy,	// comes from injection
							  // principal
							  final Class<? extends CRUDServicesForPrincipal<P>> principalCrudType,final Class<? extends FindServicesForPrincipal<P>> principalFindType,
							  // login 
							  final Class<? extends CRUDServicesForLogin> loginCrudType,final Class<? extends FindServicesForLogin> loginFindType,
							  final Class<? extends UserPasswordLoginServices> userPasswordLoginType,
							  final Class<? extends PairedPhoneLoginServices> pairedPhoneLoginType,
							  // login entry
							  final Class<? extends CRUDServicesForLoginEntry> loginEntryCrudType,final Class<? extends FindServicesForLoginEntry> loginEntryFindType,
							  // Authorization target resource
							  final Class<? extends CRUDServicesForAuthorizationTargetResource> authzResCrudType,final Class<? extends FindServicesForAuthorizationTargetResource> authzResFindType,final Class<? extends CountServicesForAuthorizationTargetResource> authzResCountType,
							  // Authorization on target resource
							  final Class<? extends CRUDServicesForAuthorizationOnResource> authzOnResCrudType,final Class<? extends FindServicesForAuthorizationOnResource> authzOnResFindType,final Class<? extends CountServicesForAuthorizationOnResource> authzOnResCountType) {
		this(securityContextProvider,
			 modelObjectsMarshaller,
			 securityServiceInterfaceTypesToImplOrProxy,
			 // principal
			 principalCrudType,principalFindType,null,	// no principal search
			 // impersonation
			 null,null,null,
			 // login
			 loginCrudType,loginFindType,
			 userPasswordLoginType,pairedPhoneLoginType,
			 // login entry
			 loginEntryCrudType,loginEntryFindType,
			 // authorization target resource
			 authzResCrudType,authzResFindType,authzResCountType,
			 // authorization on target resource
			 authzOnResCrudType,authzOnResFindType,authzOnResCountType);
	}
	@SuppressWarnings("rawtypes")
	protected SecurityAPIBase(final Provider<SecurityContext> securityContextProvider,
							  final Marshaller modelObjectsMarshaller,
							  final Map<Class,ServiceInterface> securityServiceInterfaceTypesToImplOrProxy,	// comes from injection
							  // principal
							  final Class<? extends CRUDServicesForPrincipal<P>> principalCrudType,final Class<? extends FindServicesForPrincipal<P>> principalFindType,final Class<? extends SearchServicesForPrincipal<P,?,?>> principalSearchType,
							  // login 
							  final Class<? extends CRUDServicesForLogin> loginCrudType,final Class<? extends FindServicesForLogin> loginFindType,
							  // login entry
							  final Class<? extends CRUDServicesForLoginEntry> loginEntryCrudType,final Class<? extends FindServicesForLoginEntry> loginEntryFindType,
							  final Class<? extends UserPasswordLoginServices> userPasswordLoginType,
							  final Class<? extends PairedPhoneLoginServices> pairedPhoneLoginType,
							  // Authorization target resource
							  final Class<? extends CRUDServicesForAuthorizationTargetResource> authzResCrudType,final Class<? extends FindServicesForAuthorizationTargetResource> authzResFindType,final Class<? extends CountServicesForAuthorizationTargetResource> authzResCountType,
							  // Authorization on target resource
							  final Class<? extends CRUDServicesForAuthorizationOnResource> authzOnResCrudType,final Class<? extends FindServicesForAuthorizationOnResource> authzOnResFindType,final Class<? extends CountServicesForAuthorizationOnResource> authzOnResCountType) {
		this(securityContextProvider,
			 modelObjectsMarshaller,
			 securityServiceInterfaceTypesToImplOrProxy,
			 // principal
			 principalCrudType,principalFindType,principalSearchType,	
			 // impersonation
			 null,null,null,
			 // login
			 loginCrudType,loginFindType,
			 userPasswordLoginType,
			 pairedPhoneLoginType,
			 // login entry
			 loginEntryCrudType,loginEntryFindType,
			 // authorization target resource
			 authzResCrudType,authzResFindType,authzResCountType,
			 // authorization on target resource
			 authzOnResCrudType,authzOnResFindType,authzOnResCountType);
	}
}
