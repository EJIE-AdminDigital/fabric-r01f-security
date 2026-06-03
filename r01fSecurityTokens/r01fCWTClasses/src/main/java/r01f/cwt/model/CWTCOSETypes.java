package r01f.cwt.model;

import java.util.Collection;

import lombok.Getter;
import r01f.types.StringHex;

/**
 *  COSE_Sign,
	COSE_Sign1,
	COSE_Encrypt,
	COSE_Encrypt0,
	COSE_Mac,
	COSE_Mac0;
 */
public class CWTCOSETypes {
/////////////////////////////////////////////////////////////////////////////////////////
//	COSE_Sign
/////////////////////////////////////////////////////////////////////////////////////////
	public static class CWTCOSESign
		 		extends CWTCOSEBase {
		@Getter final private Collection<StringHex> _signature;

		public CWTCOSESign(final Collection<StringHex> signature, final StringHex asCWTEncodedBytes) {
			super(CWTCOSEType.COSE_Sign,asCWTEncodedBytes);
			_signature = signature;
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	COSE_Sign1
/////////////////////////////////////////////////////////////////////////////////////////	
	public static class CWTCOSESign1
	 			extends CWTCOSEBase {

		@Getter final private StringHex _signature;

		public CWTCOSESign1(final StringHex asCWTEncodedBytes,final StringHex signature) {
			super(CWTCOSEType.COSE_Sign1,asCWTEncodedBytes);
			_signature = signature;
		}
	}

}
