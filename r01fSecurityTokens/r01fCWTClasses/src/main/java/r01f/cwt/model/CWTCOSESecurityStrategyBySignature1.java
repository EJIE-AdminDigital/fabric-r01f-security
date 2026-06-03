package r01f.cwt.model;

import java.security.PrivateKey;

import com.authlete.cbor.CBORByteArray;
import com.authlete.cose.COSEProtectedHeader;
import com.authlete.cose.COSESign1;
import com.authlete.cose.COSESign1Builder;
import com.authlete.cose.COSESigner;
import com.authlete.cose.COSEUnprotectedHeader;
import com.authlete.cose.SigStructure;
import com.authlete.cose.SigStructureBuilder;
import com.authlete.cose.constants.COSEAlgorithms;
import com.authlete.cwt.CWT;
import com.authlete.cwt.CWTClaimsSet;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.cwt.model.CWTCOSETypes.CWTCOSESign1;
import r01f.types.StringHex;

/**
 * See : https://github.com/authlete/cbor
 */
public  class  CWTCOSESecurityStrategyBySignature1
		extends  CWTCOSESecurityStrategyBase
	implements  CWTCOSESecurityStrategy {
//////////////////////////////////////////////////////////////////////////////////////
//  PK
//////////////////////////////////////////////////////////////////////////////////////
	final PrivateKey _privateKey;
//////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTOR
//////////////////////////////////////////////////////////////////////////////////////
	public CWTCOSESecurityStrategyBySignature1( final PrivateKey pk) {
		 super();
		_privateKey = pk;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS TO IMPLEMENT ( apply : sign|encrypt|mac) depending on CWTCOSESecurityStrategy
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public  CWTCOSE apply(final CWTCOSESecurityStrategyInputData dataIface) {

		CWTCOSESecurityStrategyBySignature1InputData data = (CWTCOSESecurityStrategyBySignature1InputData)dataIface;
		
		// [0] .. Transform data
		CWTClaimsSet payloadClaimSet = _payloadTransformer.fromModelObject(data.getPayload());
		COSEProtectedHeader protectedHeader = _protectedHeaderTransformer.fromModelObject(data.getProtectedHeader());
		COSEUnprotectedHeader unprotectedHeader = _unprotectedHeaderTransformer.fromModelObject(data.getUnprotectedHeader());

		// [1] .. Build SigStructure embedding payloadClaimSet as CBORByteArray
		byte[] encodedClaims = payloadClaimSet.encode();

		CBORByteArray payload = new CBORByteArray(encodedClaims);

		SigStructure structure = new SigStructureBuilder()
											.signature1()
											.bodyAttributes(protectedHeader)
											.payload(payload)
											.build();

		// [3] ..Sign the Sig_structure (= generate a signature).
		COSESigner signer = new COSESigner(_privateKey);

		byte[] signatureAsBytes= null;
		byte[] cwtAsBytes = null;
		try {
			if  (protectedHeader.getAlg() != null) {
				signatureAsBytes =	signer.sign(structure,
						                       (int) protectedHeader.getAlg());
			} else {
				signatureAsBytes =	signer.sign(structure,
						                        COSEAlgorithms.ES256);
			}
			COSESign1 sign1 = new COSESign1Builder()
								        .protectedHeader(protectedHeader)
								        .unprotectedHeader(unprotectedHeader)
								        .payload(payload)
								        .signature(signatureAsBytes)
								        .build();
			CWT cwt = new CWT(sign1);

			cwtAsBytes = cwt.encode(); 	// RFC 8392, 6. CWT CBOR Tag
                                      	//   If present, the CWT tag MUST prefix a tagged object using one of
                                      	//   the COSE CBOR tags, FE.COSE_Sign1 -> 18
		} catch (final Throwable th) {
			th.printStackTrace();
			throw new IllegalArgumentException(th.getLocalizedMessage());
		}
		return new CWTCOSESign1(StringHex.encode(cwtAsBytes), StringHex.encode(signatureAsBytes));
	}
//////////////////////////////////////////////////////////////////////////////////////
// INPUT DATA
//////////////////////////////////////////////////////////////////////////////////////
	@NoArgsConstructor
	@Accessors(prefix="_")
	public static class  CWTCOSESecurityStrategyBySignature1InputData
			 implements CWTCOSESecurityStrategyInputData {
		@Getter @Setter CWTPayload _payload;
		@Getter @Setter CWTProtectedHeader _protectedHeader;
		@Getter @Setter CWTUnprotectedHeader _unprotectedHeader;
	}
}