package r01f.crypto.hashing;

import java.nio.charset.Charset;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(prefix="_")
@RequiredArgsConstructor
public abstract class HashingServiceBase
		   implements HashingService  {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Getter final HashAlgorithm _hashAlgorithm;
	@Getter final Charset _charSet;
}
