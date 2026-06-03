package r01f.crypto.hashing;

import r01f.securitycontext.SecurityIDS.SecurityToken;

public interface HashingService {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public 	HashBase64 createHMAC(final SecurityToken key,
			                      final CharSequence  data);
	public 	HashBase64 createHMAC(final SecurityToken key,
			                      final byte[]  data);
/////////////////////////////////////////////////////////////////////////////////////////
//	INNER CLASS
/////////////////////////////////////////////////////////////////////////////////////////	
	public static class HashingServiceException 
			extends RuntimeException {

		private static final long serialVersionUID = -895871400115552953L;
	
		public HashingServiceException(final String msg,  final Throwable th) {
			super(msg, th);
		}
	}
}
