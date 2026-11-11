package r01f.api.interfaces.pairing;

import r01f.model.security.context.SecurityContext;
import r01f.model.security.pairing.IsPairingRequest;
import r01f.model.security.pairing.IsPairingResult;
import r01f.model.security.pairing.IsUnpairingRequest;
import r01f.model.security.pairing.IsUnpairingResult;
import r01f.model.security.pairing.operations.IsPairingOperationExecResult;
import r01f.services.interfaces.ExposedServiceInterface;
import r01f.services.interfaces.ServiceInterface;

@ExposedServiceInterface
public interface PairingService<P extends IsPairingRequest,
								U extends IsUnpairingRequest>
		 extends ServiceInterface,
				 PairingServiceInterface {
	
/////////////////////////////////////////////////////////////////////////////////////////
// METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * After the request, if the pairing result is ok, pair it !! and return the 
	 * @param securityContext
	 * @param apiKeyPublicationRequest
	 * @return
	 */
	public IsPairingOperationExecResult<IsPairingResult> pair(final SecurityContext securityContext,
														  final P pairingRequest);
		
	/**
	 * Unpairing
	 * @param securityContext
	 * @param apiKeyPublicationRequest
	 * @return
	 */
	public IsPairingOperationExecResult<IsUnpairingResult> unpair(final SecurityContext securityContext,
															  final U unpairingRequest);
}