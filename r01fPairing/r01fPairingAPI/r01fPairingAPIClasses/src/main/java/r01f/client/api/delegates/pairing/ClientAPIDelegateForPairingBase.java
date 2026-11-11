package r01f.client.api.delegates.pairing;

import jakarta.inject.Provider;
import r01f.api.interfaces.pairing.PairingService;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.pairing.IsPairingRequest;
import r01f.model.security.pairing.IsPairingResult;
import r01f.model.security.pairing.IsUnpairingRequest;
import r01f.model.security.pairing.IsUnpairingResult;
import r01f.model.security.pairing.operations.IsPairingOperationExecResult;
import r01f.objectstreamer.Marshaller;
import r01f.services.api.delegates.ClientAPIServiceDelegateBase;

public class ClientAPIDelegateForPairingBase<P extends IsPairingRequest, U extends IsUnpairingRequest>
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
	public IsPairingOperationExecResult<IsPairingResult> pair(final P pariringProcessInputData) {
		if (pariringProcessInputData == null) throw new IllegalArgumentException("Pariring Process InputData CANNOT be null");
		return  this.getServiceProxyAs(PairingService.class)
						.pair(this.getSecurityContext(),
						      pariringProcessInputData);
	}
	@SuppressWarnings("unchecked")
	public IsPairingOperationExecResult<IsUnpairingResult> unpair(final IsUnpairingRequest pariringProcessInputData) {
		if (pariringProcessInputData == null) throw new IllegalArgumentException("Unpariring Process InputData CANNOT be null");
		return  this.getServiceProxyAs(PairingService.class)
						.unpair(this.getSecurityContext(),
								pariringProcessInputData);

	}
}
