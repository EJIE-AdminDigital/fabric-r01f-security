package r01f.model.security.login;

import r01f.model.security.PersistableSecurityObject;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.securitycontext.SecurityOIDs.UserOID;

/**
 * Login >> Do not get confused and keep in mind!   *
 * ------------------------------------------------------------------------------------
 *   1.   The Login Object represents the data required to authenticate in a system,
 *          f.e the UserPasswordLogin , an ApiKey pair, etc.. *
 *         for example in a ApiKeyLogin , the LoginOID represents the 'ApiKeyOID' itself
 *
 *   2.  There is also an object for Google, Xlnets, although logically the data to authenticate in these systems is outside
 *
 *   2.  The login object does not represent in any case any  and object model to get the login data once logged in
 *       .For that purpose exists the LoginEntry Object or in other systems the LoginSession.
 */
public interface Login
		 extends PersistableSecurityObject<LoginOID> {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public UserOID getUserOid();
	public void setUserOid(final UserOID userDataOid);

	public LoginID getLoginId();
	public void setLoginId(final LoginID login);
}
