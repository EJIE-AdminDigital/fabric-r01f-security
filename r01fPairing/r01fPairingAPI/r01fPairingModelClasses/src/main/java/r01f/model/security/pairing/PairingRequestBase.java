package r01f.model.security.pairing;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.keystore.key.PublicKey;
import r01f.objectstreamer.annotations.MarshallField;

@Accessors(prefix="_")
public abstract class PairingRequestBase
		   implements IsPairingRequest  {
	private static final long serialVersionUID = -1111637486026099684L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	@MarshallField(as="publickey",escape=true)
	@Getter @Setter protected PublicKey _publicKey;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean usingOTP() {	
		return false;
	}		
}
