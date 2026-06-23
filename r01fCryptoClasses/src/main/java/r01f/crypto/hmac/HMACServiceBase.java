package r01f.crypto.hmac;

import java.nio.charset.Charset;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(prefix="_")
@RequiredArgsConstructor
public abstract class HMACServiceBase
		   implements HMACService  {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Getter final HMACAlgorithm _hashAlgorithm;
	@Getter final Charset _charSet;
}
