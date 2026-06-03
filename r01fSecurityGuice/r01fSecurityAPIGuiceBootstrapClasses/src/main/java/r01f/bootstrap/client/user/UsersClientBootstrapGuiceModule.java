package r01f.bootstrap.client.user;

import com.google.inject.Binder;

import lombok.EqualsAndHashCode;
import r01f.client.api.security.SecurityAPIBase;
import r01f.inject.HasMoreBindings;
import r01f.services.bootstrap.api.guice.ServicesBootstrapClientAPIGuiceModuleBase;
import r01f.services.ids.ServiceIDs.ClientApiAppCode;

/**
 * Client-API bindings
 */
@EqualsAndHashCode(callSuper=true) // This is important for guice modules
public class UsersClientBootstrapGuiceModule
  	 extends ServicesBootstrapClientAPIGuiceModuleBase 	// this is a client guice bindings module
  implements HasMoreBindings {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public UsersClientBootstrapGuiceModule(final ClientApiAppCode clientApiAppCode,final Class<? extends SecurityAPIBase<?,?,?,?,?>> clientApiType) {
		super(clientApiAppCode,clientApiType);
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
