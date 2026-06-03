package r01f.client.api.sub.security.login.apikey;

import java.util.Collection;

import jakarta.inject.Provider;

import r01f.api.interfaces.security.login.apikey.CRUDServicesForApiKeyLogin;
import r01f.api.interfaces.security.login.apikey.FindServicesForApiKeyLogin;
import r01f.client.api.sub.security.login.ClientAPIDelegateForLoginFindServicesBase;
import r01f.model.persistence.FindResult;
import r01f.model.security.login.apikey.ApiKeyLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.api.delegates.ClientAPIModelObjectChangesTrack;

public class ClientAPIDelegateForApiKeyLoginFindServices<L extends ApiKeyLogin>
	 extends ClientAPIDelegateForLoginFindServicesBase<L> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForApiKeyLoginFindServices(final Provider<SecurityContext>  securityContextProvider,
													   final Marshaller marshaller,
													   final FindServicesForApiKeyLogin<L> findServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  findServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	BEWARE!
// 	When using [api key login] a certain [user]/[loginId] might have MANY [api key login]
//	entries multiple [api key login] can be issued but just one might be active in a
//	certain [api key version] (other issued [api key login] within the SAME version
// 	are INVALIDATED
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Finds al [api key logins] for a given [user]
	 * BEWARE that a [user] might have MULTIPLE [api key logins] BUT just one active
	 * 		  in a given date range
	 * 		  ... the {@link CRUDServicesForApiKeyLogin} have a loadBy(userOid) method
	 * 			  that returns a SINGLE [api key login] > the one currently active
	 * 		  ... on the contrary, this method returns ALL the [api key logins] whether
	 * 			  they're active or not
	 * @param ownerOid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Collection<L> findByOwnerOid(final UserOID ownerOid) {
		FindResult<L> find = this.getServiceProxyAs(FindServicesForApiKeyLogin.class)
								 .findByOwnerOid(this.getSecurityContext(),
										 		 ownerOid);
		Collection<L> logins = find.getOrThrow();
		ClientAPIModelObjectChangesTrack.startTrackingChangesOnLoaded(logins);
		return logins;
	}
	/**
	 * Finds al [api key logins] for a given [user]
	 * BEWARE that a [user] might have MULTIPLE [api key logins] BUT just one active
	 * 		  in a given date range
	 * 		  ... the {@link CRUDServicesForApiKeyLogin} have a loadBy(userOid) method
	 * 			  that returns a SINGLE [api key login] > the one currently active
	 * 		  ... on the contrary, this method returns ALL the [api key logins] whether
	 * 			  they're active or not
	 * @param loginId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Collection<L> findByLoginId(final LoginID loginId) {
		FindResult<L> find = this.getServiceProxyAs(FindServicesForApiKeyLogin.class)
								 .findByLoginId(this.getSecurityContext(),
										 		loginId);
		Collection<L> logins = find.getOrThrow();
		ClientAPIModelObjectChangesTrack.startTrackingChangesOnLoaded(logins);
		return logins;
	}
}
