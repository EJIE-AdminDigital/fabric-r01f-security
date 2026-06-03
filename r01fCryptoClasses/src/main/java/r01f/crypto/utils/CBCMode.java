package r01f.crypto.utils;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix="_")
public enum CBCMode {
	AES_CBC_NoPadding("AES/CBC/NoPadding", 128),
	AES_CBC_PKCS5Padding("AES/CBC/PKCS5Padding", 128),
	AES_ECB_NoPadding("AES/ECB/NoPadding", 128),
	AES_ECB_PKCS5Padding("AES/ECB/PKCS5Padding", 128),
	DES_CBC_NoPadding("DES/CBC/NoPadding", 56),
	DES_CBC_PKCS5Padding("DES/CBC/PKCS5Padding", 56),
	DES_ECB_NoPadding("DES/ECB/NoPadding", 56),
	DES_ECB_PKCS5Padding("DES/ECB/PKCS5Padding", 56),
	DESede_CBC_NoPadding("DESede/CBC/NoPadding", 168),
	DESede_CBC_PKCS5Padding("DESede/CBC/PKCS5Padding", 168),
	DESede_ECB_NoPadding("DESede/ECB/NoPadding", 168),
	DESede_ECB_PKCS5Padding("DESede/ECB/PKCS5Padding", 168),
	RSA_ECB_PKCS1Padding_1024("RSA/ECB/PKCS1Padding", 1024),
	RSA_ECB_PKCS1Padding_2048("RSA/ECB/PKCS1Padding", 2048),
	RSA_ECB_OAEPWithSHA_1AndMGF1Padding_1024("RSA/ECB/OAEPWithSHA-1AndMGF1Padding", 1024),
	RSA_ECB_OAEPWithSHA_1AndMGF1Padding_2048("RSA/ECB/OAEPWithSHA-1AndMGF1Padding", 2048),
	RSA_ECB_OAEPWithSHA_256AndMGF1Padding_1024("RSA/ECB/OAEPWithSHA-256AndMGF1Padding", 1024),
	RSA_ECB_OAEPWithSHA_256AndMGF1Padding_2048("RSA/ECB/OAEPWithSHA-256AndMGF1Padding", 2048);
	
	@Getter private int _keySize;
	@Getter private String _name;
	
	CBCMode(final String name, final int keySize) {
		_name = name;
		_keySize = keySize;
	}
}
