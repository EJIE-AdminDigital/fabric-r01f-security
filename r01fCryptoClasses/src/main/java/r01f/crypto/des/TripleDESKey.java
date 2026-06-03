package r01f.crypto.des;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.util.encoders.Hex;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.securitycontext.SecurityIDS.Password;
import r01f.types.StringHex;

@Accessors(prefix="_")
public class TripleDESKey {
  
    public static final String ALGORITHM_3DES = "DESede";
	private static final byte[] DEFAULT_IV ={0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,0x00};
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Getter private final SecretKey _key;
	@Getter private final byte[] _IV;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////
	public TripleDESKey(final SecretKey sk,final byte[] ivBytes) {
		_key = sk;
		_IV = ivBytes;
	}	
	public TripleDESKey(final StringHex passAsHex) {
		byte [] seed_key = Hex.decode(passAsHex.asString());
		_key = new SecretKeySpec(seed_key,ALGORITHM_3DES);
		_IV  = DEFAULT_IV;
	}
	public TripleDESKey(final Password pass) {
		byte [] seed_key = Hex.decode(pass.asString());
		_key = new SecretKeySpec(seed_key,ALGORITHM_3DES);
		_IV  = DEFAULT_IV;
	}	
}
