package r01f.cwt.parser;

import java.io.IOException;

import com.authlete.cbor.CBORDecoder;
import com.authlete.cbor.CBORItem;
import com.authlete.cose.COSEMessage;
import com.authlete.cose.COSEProtectedHeader;
import com.authlete.cose.COSESign1;
import com.authlete.cose.COSEUnprotectedHeader;
import com.authlete.cwt.CWTClaimsSet;

import r01f.cwt.CWT;
import r01f.cwt.model.CWTCOSETypes.CWTCOSESign1;
import r01f.cwt.model.CWTDecoded;
import r01f.cwt.model.CWTPayload;
import r01f.cwt.model.CWTProtectedHeader;
import r01f.cwt.model.CWTUnprotectedHeader;
import r01f.types.StringHex;


public class CWTParserImpl
	extends CWTParserImplBase
 implements CWTParser {
/////////////////////////////////////////////////////////////////////////////////////////
//	CWT : UNSECURED NOT COSE
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	protected CWTDecoded _parseUnsecuredCWT(final CWT cwt) {
		try {
			CBORItem itemPayload = new CBORDecoder(cwt.getValue().decodeAsBytes()).next();
		    CWTClaimsSet payload = CWTClaimsSet.build(itemPayload);
			CWTPayload  payloadAsModelObject = _payloadTransformer.toModelObject(payload);
			// return
			CWTDecoded  cwtDecoded = new CWTDecoded();
			cwtDecoded.setPayload(payloadAsModelObject);
			return cwtDecoded;
		} catch (final IOException e) {
			e.printStackTrace();
			throw new IllegalArgumentException();
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CWT : SECURED COSE SIGN
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	protected CWTDecoded _parseCOSESign1CWT(final CWT cwt,final COSEMessage message) {
		 try {
			COSESign1 sign1Dec = (COSESign1)message;
			 // Interpret the payload as CWT Claims Set.
			CBORItem payloadAsCBORItem = sign1Dec.getPayload();
			CWTClaimsSet payload = CWTClaimsSet.build(payloadAsCBORItem);
			 // Interpret the protected and unprotected header.
			COSEProtectedHeader   protectedHeader = sign1Dec.getProtectedHeader();
			COSEUnprotectedHeader unprotectedHeader = sign1Dec.getUnprotectedHeader();

			// Build
			CWTPayload  payloadAsModelObject = _payloadTransformer.toModelObject(payload);
			CWTProtectedHeader protectedHeaderAsModelObject = _protectedHeaderTransformer.toModelObject(protectedHeader);
			CWTUnprotectedHeader unprotectedHeaderAsModelObject = _unprotectedHeaderTransformer.toModelObject(unprotectedHeader);
			CWTCOSESign1 coseAsModelObject = new CWTCOSESign1(cwt.getValue(), StringHex.encode(sign1Dec.getSignature().encode()));

			// return
			CWTDecoded  cwtDecoded = new CWTDecoded();
			cwtDecoded.setPayload(payloadAsModelObject);
			cwtDecoded.setCose(coseAsModelObject);
			cwtDecoded.setProtectedHeader(protectedHeaderAsModelObject);
			cwtDecoded.setUnprotectedHeader(unprotectedHeaderAsModelObject);
			return cwtDecoded;

		 } catch (final Throwable e) {
			e.printStackTrace();
			throw new IllegalArgumentException();
		 }
	}
	@Override
	protected CWTDecoded _parseCOSESignCWT(final CWT cwt,final COSEMessage cose) {
		throw new IllegalArgumentException("Not implemented yet");
	}
	@Override
	protected CWTDecoded _parseCOSEEncrypt(final CWT cwt,final COSEMessage cose) {
		throw new IllegalArgumentException("Not implemented yet");
	}
	@Override
	protected CWTDecoded _parseCOSEEncrypt0(final CWT cwt,final COSEMessage cose) {
		throw new IllegalArgumentException("Not implemented yet");
	}
	@Override
	protected CWTDecoded _parseCOSEMac(final CWT cwt,final COSEMessage cose) {
		throw new IllegalArgumentException("Not implemented yet");
	}
	@Override
	protected CWTDecoded _parseCOSEEMac0(final CWT cwt,final COSEMessage cose) {
		throw new IllegalArgumentException("Not implemented yet");
	}
}
