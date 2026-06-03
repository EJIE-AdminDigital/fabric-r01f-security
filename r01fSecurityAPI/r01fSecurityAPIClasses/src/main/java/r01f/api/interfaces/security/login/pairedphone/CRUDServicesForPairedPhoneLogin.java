package r01f.api.interfaces.security.login.pairedphone;

import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.login.pairedphone.PairedPhoneLogin;
import r01f.securitycontext.SecurityContext;
import r01f.services.interfaces.ExposedServiceInterface;
import r01f.types.contact.Phone;

@ExposedServiceInterface
public interface CRUDServicesForPairedPhoneLogin<L extends PairedPhoneLogin>
		 extends CRUDServicesForLogin<L> {
	/**
	 * Returns a login for the given phone
	 * @param securityContext the user auth data & context info
	 * @param login the login
	 * @return a {@link CRUDResult} that encapsulates the entity if it was loaded successfully
	 */
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
								final Phone login);
}