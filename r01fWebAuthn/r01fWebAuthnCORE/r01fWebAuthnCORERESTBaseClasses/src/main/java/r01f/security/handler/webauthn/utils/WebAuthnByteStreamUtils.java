package r01f.security.handler.webauthn.utils;

import com.yubico.webauthn.data.ByteArray;

import r01f.guids.OID;
import r01f.model.security.keystore.key.PublicKey;
import r01f.types.StringBase64;

public class WebAuthnByteStreamUtils {

////////////////////////////////////////////////
// UTILITIES FOR YUBICO BYTESTREAM
///////////////////////////////////////////////
	public static ByteArray from( final OID oid){
		StringBase64 base64 =  StringBase64.encode(oid.asString().getBytes());
		return ByteArray.fromBase64(base64.asString());
	}

	public static ByteArray from( final byte[] data){
		StringBase64 base64 =  StringBase64.encode(data);
		return ByteArray.fromBase64(base64.asString());
	}

	public static ByteArray from(final PublicKey publicKey) {
		StringBase64 base64 =  publicKey.getKeyValue();
		return ByteArray.fromBase64(base64.asString());
	}

	public static String from( final ByteArray ba){
		StringBase64 base64Decode  = StringBase64.fromBase64EncodedString(ba.getBase64());
		String oidAsString  =new String(base64Decode.decodeAsBytes());
		return oidAsString;
	}

}
