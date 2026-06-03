package r01f.model.security.keystore;

import java.util.UUID;

public class KeystoreGUIDDispenser {
	
/////////////////////////////////////////////////////////////////////////////////////////
// 	GUID GENERATION
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Generates a GUID
	 * @return 
	 */
	public static String generateGUID() {
		UUID uuid = UUID.randomUUID();	
		return uuid.toString();
	}
}