package r01f.api.interfaces.security.login.pairedphone;

import r01f.api.interfaces.security.login.FindServicesForLogin;
import r01f.model.security.login.pairedphone.PairedPhoneLogin;
import r01f.services.interfaces.ExposedServiceInterface;

@ExposedServiceInterface
public interface FindServicesForPairedPhoneLogin<L extends PairedPhoneLogin>
		extends  FindServicesForLogin<L> {
	// nothing
}