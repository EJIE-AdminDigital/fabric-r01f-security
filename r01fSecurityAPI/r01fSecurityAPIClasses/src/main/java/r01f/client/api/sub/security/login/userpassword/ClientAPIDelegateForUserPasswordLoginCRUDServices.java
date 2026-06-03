package r01f.client.api.sub.security.login.userpassword;

import jakarta.inject.Provider;

import r01f.api.interfaces.security.login.userpassword.CRUDServicesForUserPasswordLogin;
import r01f.client.api.sub.security.login.ClientAPIDelegateForLoginCRUDServicesBase;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.login.userpassword.UserPasswordLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.Password;
import r01f.securitycontext.SecurityOIDs.UserOID;

public class ClientAPIDelegateForUserPasswordLoginCRUDServices<L extends UserPasswordLogin>
	 extends ClientAPIDelegateForLoginCRUDServicesBase<L> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForUserPasswordLoginCRUDServices(final Provider<SecurityContext>  securityContextProvider,
														 	 final Marshaller marshaller,
														 	 final CRUDServicesForUserPasswordLogin<L> crudServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  crudServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Updates a login password
	 * @param login
	 * @param password
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public L updatePassword(final LoginID login,
							final Password password) {
		CRUDResult<L> opResult = this.getServiceProxyAs(CRUDServicesForUserPasswordLogin.class)
									 .updatePassword(this.getSecurityContext(),
											 		 login,password);
		L outEntity = opResult.getOrThrow();
		return outEntity;
	}
	@SuppressWarnings("unchecked")
	public L updatePassword(final LoginID login,
							final Password oldPassword,final Password newPassword) {
		CRUDResult<L> opResult = this.getServiceProxyAs(CRUDServicesForUserPasswordLogin.class)
									 .updatePassword(this.getSecurityContext(),
											 		 login,oldPassword,newPassword);
		L outEntity = opResult.getOrThrow();
		return outEntity;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public L changeLoginId(final UserOID userOid,final LoginID newLoginId) {
		// check the NEW login id DOES NOT already exists
		L newLogin = this.loadByOrNull(newLoginId);
		if (newLogin != null) throw new IllegalArgumentException("loginId=" + newLoginId + " already exists!!!");

		// load the login
		L login = this.loadBy(userOid);
		if (login.getLoginId().is(newLoginId)) return login;	// do nothing;

		// update the login
		login.setLoginId(newLoginId);
		L updatedLogin = this.update(login);

		// TODO update ALL [user password login entries]!

		return updatedLogin;
	}
}
