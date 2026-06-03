package r01f.cwt.parser;

import java.io.IOException;

import com.authlete.cbor.CBORDecoder;
import com.authlete.cbor.CBORItem;
import com.authlete.cbor.CBORTaggedItem;
import com.authlete.cose.COSEMessage;
import com.authlete.cose.COSESign1;

import r01f.cwt.CWT;
import r01f.cwt.model.CWTDecoded;
import r01f.cwt.model.transformers.CWTPayloadTransformer;
import r01f.cwt.model.transformers.CWTProtectedHeaderTransformer;
import r01f.cwt.model.transformers.CWTUnprotectedHeaderTransformer;


public abstract class CWTParserImplBase
  implements CWTParser {

	CWTPayloadTransformer  _payloadTransformer;
	CWTProtectedHeaderTransformer _protectedHeaderTransformer;
	CWTUnprotectedHeaderTransformer  _unprotectedHeaderTransformer;
//////////////////////////////////////////////////////////////////////////////////////
//CONSTRUCTOR
//////////////////////////////////////////////////////////////////////////////////////
	public CWTParserImplBase( ) {
		_payloadTransformer  = new CWTPayloadTransformer();
		_protectedHeaderTransformer  = new CWTProtectedHeaderTransformer();
		_unprotectedHeaderTransformer  = new CWTUnprotectedHeaderTransformer();
	}
/////////////////////////////////////////////////////////////////////////////////////////
// ABSTRACT METHODS TO IMPLEMENT
/////////////////////////////////////////////////////////////////////////////////////////

	protected abstract  CWTDecoded _parseUnsecuredCWT(final CWT cwt);

	protected abstract  CWTDecoded _parseCOSESign1CWT(final CWT cwt, final COSEMessage cose);
	protected abstract  CWTDecoded _parseCOSESignCWT(final CWT cwt, final COSEMessage cose);

	protected abstract  CWTDecoded _parseCOSEEncrypt(final CWT cwt, final  COSEMessage cose);
	protected abstract  CWTDecoded _parseCOSEEncrypt0(final CWT cwt, final COSEMessage cose);

	protected abstract  CWTDecoded _parseCOSEMac(final CWT cwt, final COSEMessage cose);
	protected abstract  CWTDecoded _parseCOSEEMac0(final CWT cwt, final COSEMessage cose);

/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CWTDecoded parse(final CWT cwt) {
		return _parse(cwt);
	}
/////////////////////////////////////////////////////////////////////////////////////////
// PARSE
/////////////////////////////////////////////////////////////////////////////////////////
    protected CWTDecoded _parse(final CWT cwt) {     ;
        try {
        	CBORItem   item  = new CBORDecoder(cwt.getValue().decodeAsBytes()).next();
	        if ( item instanceof CBORTaggedItem) {
	        	return _parseCOSE(cwt);
	        } else {
	        	return _parseUnsecuredCWT(cwt);
	        }
        } catch (final Throwable  e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getLocalizedMessage());
        }
    }

/////////////////////////////////////////////////////////////////////////////////////////
// BUILD COSE
/////////////////////////////////////////////////////////////////////////////////////////
   protected CWTDecoded _parseCOSE( final CWT cwt) throws IOException {
	   CBORTaggedItem taggedItem =  (CBORTaggedItem) new CBORDecoder(cwt.getValue().decodeAsBytes()).next();
	   COSEMessage message = null;
	   if ( taggedItem.getTagNumber().intValue() == 61) {
		    //(optional) enabled if the CWT CBOR Tag (61) is added when the token is generated.
		   	//           Refer to the CBOR Web Token Internet Standards for more details on tags.
		    com.authlete.cwt.CWT  cwtDecodedAsAuthlete = (com.authlete.cwt.CWT)taggedItem;
		    message = cwtDecodedAsAuthlete.getMessage();
	   } else {
		   message = (COSESign1)taggedItem.getTagContent();
	   }
       int tagNumber = message.getTagNumber();

       switch (tagNumber) { //https://github.com/authlete/cbor
           case 98 : {
        	             return _parseCOSESignCWT(cwt,message);
           }
           case 18 :  {
                         return _parseCOSESign1CWT(cwt,message);
           }
           case 96 : {
        	            return _parseCOSEEncrypt(cwt,message);
           }
           case 16 : {
        	            return _parseCOSEEncrypt0(cwt,message);
           }
           case 97 : {
        	             return _parseCOSEMac(cwt,message);
           }
           case 17 : {
        	             return _parseCOSEEMac0(cwt,message);
           }
           default :
        	          throw new IllegalArgumentException("Unknown format of cwt, tagged as :" + tagNumber);
       }
   }

}
