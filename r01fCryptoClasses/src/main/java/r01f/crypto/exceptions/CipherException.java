package r01f.crypto.exceptions;

import r01f.exceptions.EnrichedRuntimeException;

public class CipherException 
	 extends EnrichedRuntimeException {
	private static final long serialVersionUID = -636807422407148853L;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public CipherException(final String msg,final Throwable th) {
		super(msg,th);		
	}
}
