package r01f.api.interfaces.pairing;

import r01f.model.security.pairing.PairingRequest;
import r01f.model.security.pairing.PairingResult;
import r01f.model.security.pairing.UnpairingRequest;
import r01f.model.security.pairing.UnpairingResult;
import r01f.model.security.pairing.operations.PairingOperationExecResult;
import r01f.securitycontext.SecurityContext;
import r01f.services.interfaces.ExposedServiceInterface;
import r01f.services.interfaces.ServiceInterface;

@ExposedServiceInterface
public interface PairingService<P extends PairingRequest,
								U extends UnpairingRequest>
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
	public PairingOperationExecResult<PairingResult> pair(final SecurityContext securityContext,
														  final P pairingRequest);
		
	/**
	 * Unpairing
	 * @param securityContext
	 * @param apiKeyPublicationRequest
	 * @return
	 */
	public PairingOperationExecResult<UnpairingResult> unpair(final SecurityContext securityContext,
															  final U unpairingRequest);
}