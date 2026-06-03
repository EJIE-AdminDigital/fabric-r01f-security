package r01f.api.interfaces.security.login.apikey;

import r01f.api.interfaces.security.SecurityServiceInterface;
import r01f.api.interfaces.security.login.FindServicesForLogin;
import r01f.model.persistence.FindResult;
import r01f.model.security.login.apikey.ApiKeyLogin;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.interfaces.ExposedServiceInterface;

@ExposedServiceInterface
public interface FindServicesForApiKeyLogin<L extends ApiKeyLogin>
		 extends FindServicesForLogin<L>,
				 SecurityServiceInterface {
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
	 * @param securityContext
	 * @param ownerOid
	 * @return
	 */
	public FindResult<L> findByOwnerOid(final SecurityContext securityContext,
			 						    final UserOID ownerOid);
	/**
	 * Finds al [api key logins] for a given [user]
	 * BEWARE that a [user] might have MULTIPLE [api key logins] BUT just one active
	 * 		  in a given date range & version
	 * 		  ... the {@link CRUDServicesForApiKeyLogin} have a loadBy(loginId) method
	 * 			  that returns a SINGLE [api key login] > the one currently active
	 * 		  ... on the contrary, this method returns ALL the [api key logins] whether
	 * 			  they're active or not
	 * @param securityContext
	 * @param ownerOid
	 * @return
	 */
	public FindResult<L> findByLoginId(final SecurityContext securityContext,
									   final LoginID loginId);
}