package r01f.cwt.model;

import r01f.types.StringHex;

/**
 * 
 * COSE : Signature.or.Encryption
 * See: https://github.com/authlete/cbor
 *   	https://www.rfc-editor.org/rfc/rfc9052.html
 *  
 * <pre>
 * COSE Message	Semantics	                              		CBOR Tag
 * -------------------------------------------------------------------------
 *  COSE_Sign	    	COSE Signed Data Object						98
 * 	COSE_Sign1		    COSE Single Signer Data Object				18
 * 	COSE_Encrypt		COSE Encrypted Data Object					96
 * 	COSE_Encrypt0		COSE Single Recipient Encrypted Data Object	16
 * 	COSE_Mac	    	COSE MACed Data Object						97
 * 	COSE_Mac0	        COSE MAC without Recipients Object			17
 * </pre>
 */
public interface CWTCOSE
	     extends CWTModelObject {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public CWTCOSEType getType();

	public StringHex  getAsCWTEncodedBytes();
}
