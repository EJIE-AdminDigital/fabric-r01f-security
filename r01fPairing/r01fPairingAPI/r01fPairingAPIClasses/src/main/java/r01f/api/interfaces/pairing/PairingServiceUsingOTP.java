package r01f.api.interfaces.pairing;

import r01f.model.security.context.SecurityContext;
import r01f.model.security.pairing.IsUnpairingRequest;
import r01f.model.security.pairing.operations.IsPairingOperationExecResult;
import r01f.model.security.pairing.otp.IsPairingOTPRequest;
import r01f.model.security.pairing.otp.PairingOTPRequestResult;
import r01f.model.security.pairing.otp.IsPairingRequestUsingOTP;
import r01f.services.interfaces.ExposedServiceInterface;

/**
 * Sometimes the request for pairing requires one and only one unit action.. others cases it is done in two steps
 *      For example in the case of Jantoki Lakua is to send an OTP
 * .... In the pairing service @T00PairingService, the pair method will receive the OTP given by the user....
 * 
 *     LAKUA Service : 
 *          First:   @TK00PairingService.request                    -----------> Sends a (  OTP or another thing...).
 *          Second   @TK00PairingService.pair (input data)          -----------> Receives the user given otp, and if is ok, then pair it.
 *          
 *     ARKAUTE Service :            
 *          First   @TK00PairingService.pair (input data)           -----------> Receives a known user data (bith date, dni, etc..) , check and pair! 
 * 
 * @param <RQ>
 * @param <RP>
 */
@ExposedServiceInterface
public interface PairingServiceUsingOTP<P extends IsPairingRequestUsingOTP, 
									 U extends IsUnpairingRequest>
		 extends PairingService<P, U>,
		 		 PairingServiceInterface {
/////////////////////////////////////////////////////////////////////////////////////////
// METHODS
/////////////////////////////////////////////////////////////////////////////////////////

	/** Request to pair in the instalation via otp .fe of the mobile customer app
	 * @param securityContext
	 * @param keyRequest
	 * @return	 */
	public IsPairingOperationExecResult<PairingOTPRequestResult> request(final SecurityContext securityContext, 
																final IsPairingOTPRequest otpRequest);

}