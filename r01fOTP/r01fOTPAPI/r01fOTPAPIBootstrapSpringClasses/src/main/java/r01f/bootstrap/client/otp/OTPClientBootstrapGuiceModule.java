package r01f.bootstrap.client.otp;

import com.google.inject.Binder;

import lombok.EqualsAndHashCode;
import r01f.client.api.otp.OTPServiceAPI;
import r01f.inject.HasMoreBindings;
import r01f.internal.R01FAppCodes;
import r01f.services.bootstrap.api.guice.ServicesBootstrapClientAPIGuiceModuleBase;
import r01f.services.ids.ServiceIDs.ClientApiAppCode;


/**
 * Client-API bindings
 */
@EqualsAndHashCode(callSuper=true) // This is important for guice modules
public class OTPClientBootstrapGuiceModule
  	 extends ServicesBootstrapClientAPIGuiceModuleBase 	// this is a client guice bindings module
  implements HasMoreBindings {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public OTPClientBootstrapGuiceModule() {
		super(ClientApiAppCode.forId(R01FAppCodes.R01_APP_CODE_STR),OTPServiceAPI.class);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  GUICE MODULE
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void configureMoreBindings(final Binder binder) {
		_bindModelObjectExtensionsModule(binder);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  MODEL EXTENSIONS
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * @param binder
	 * @return bindings for the model extensions
	 */
	private static void _bindModelObjectExtensionsModule(final Binder binder) {
		// nothing
	}
}
