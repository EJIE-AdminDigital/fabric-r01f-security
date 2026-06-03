package r01f.client.api.delegates.pairing;

import jakarta.inject.Provider;

import r01f.api.interfaces.pairing.PairingService;
import r01f.model.security.pairing.PairingRequest;
import r01f.model.security.pairing.PairingResult;
import r01f.model.security.pairing.UnpairingRequest;
import r01f.model.security.pairing.UnpairingResult;
import r01f.model.security.pairing.operations.PairingOperationExecResult;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.api.delegates.ClientAPIServiceDelegateBase;

public class ClientAPIDelegateForPairingBase<P extends PairingRequest, U extends UnpairingRequest>
			 extends ClientAPIServiceDelegateBase<PairingService<P, U>> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForPairingBase(final Provider<SecurityContext> securityContextProvider,
										   final Marshaller modelObjectsMarshaller,
										   final PairingService<P, U> usageServicesProxy) {
		super(securityContextProvider,
			  modelObjectsMarshaller,
			  usageServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
// PAIR / UNPAIR
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	public PairingOperationExecResult<PairingResult>  pair(final P pariringProcessInputData) {
		if (pariringProcessInputData == null) {
			throw new IllegalArgumentException("Pariring Process InputData CANNOT be null");
		}
		return  this.getServiceProxyAs(PairingService.class)
						.pair(this.getSecurityContext(),
						      pariringProcessInputData);

	}

	@SuppressWarnings("unchecked")
	public PairingOperationExecResult<UnpairingResult> unpair(final UnpairingRequest pariringProcessInputData) {
		if (pariringProcessInputData == null) {
			throw new IllegalArgumentException("Unpariring Process InputData CANNOT be null");
		}
		return  this.getServiceProxyAs(PairingService.class)
						.unpair(this.getSecurityContext(),
								pariringProcessInputData);

	}
}
