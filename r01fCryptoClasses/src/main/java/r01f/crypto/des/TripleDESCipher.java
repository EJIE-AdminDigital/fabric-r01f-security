package r01f.crypto.des;

import r01f.crypto.exceptions.CipherException;
import r01f.crypto.utils.CBCMode;
import r01f.types.StringBase64;
import r01f.types.StringHex;

public interface TripleDESCipher {
/////////////////////////////////////////////////////////////////////////////////////////
//		
/////////////////////////////////////////////////////////////////////////////////////////	
	static final CBCMode DEFAULT_CBC_MODE = CBCMode.DESede_CBC_NoPadding;
/////////////////////////////////////////////////////////////////////////////////////////
// 	ENCRYPT
/////////////////////////////////////////////////////////////////////////////////////////
    public byte[] encrypt(final byte[] message)                 throws CipherException;
    
    public StringHex encryptAsHex(final byte[] message)         throws CipherException;
    
    public StringBase64 encryptAsBase64(final byte[] message)   throws CipherException;    
/////////////////////////////////////////////////////////////////////////////////////////
// DECRYPT
/////////////////////////////////////////////////////////////////////////////////////////
	public byte[] decrypt(final byte[] encrypted) throws CipherException;	
	
	public byte[] decryptFromHex(final StringHex encrypted) throws CipherException;	
	
	public byte[] decryptFromBase64(final StringBase64 encrypted) throws CipherException;	
	
}