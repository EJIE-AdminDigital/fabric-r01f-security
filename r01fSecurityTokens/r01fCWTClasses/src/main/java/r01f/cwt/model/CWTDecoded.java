package r01f.cwt.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Accessors(prefix="_")
public class CWTDecoded {
/////////////////////////////////////////////////////////////////////////////////////////
// 	STRUCTURE( Header + Payload + Signature) of a Decoded JWT
// ...https://raw.githubusercontent.com/webshield-dev/eudvcdecoder/main/images/eu-dgc-layers.png	
/////////////////////////////////////////////////////////////////////////////////////////	
	private @Setter @Getter CWTProtectedHeader _protectedHeader;
	private @Setter @Getter CWTUnprotectedHeader _unprotectedHeader;
	private @Setter @Getter CWTPayload _payload;
	private @Setter @Getter CWTCOSE _cose; // Signature.Or.Encryption
}
