package r01f.cwt.model.transformers;

import com.authlete.cbor.CBORInteger;
import com.authlete.cbor.CBORItem;
import com.authlete.cbor.CBORPair;
import com.authlete.cbor.CBORString;

import r01f.cwt.model.CWTCustomClaim;
import r01f.cwt.model.CWTCustomClaimKey;
import r01f.cwt.model.CWTCustomClaimKey.CWTCustomClaimIntegerKey;
import r01f.cwt.model.CWTCustomClaimKey.CWTCustomClaimKeyTyped;
import r01f.cwt.model.CWTCustomClaimKey.CWTCustomClaimStringKey;
import r01f.cwt.model.CWTCustomClaimValue;
import r01f.cwt.model.CWTCustomClaimValue.CWTCustomClaimIntegerValue;
import r01f.cwt.model.CWTCustomClaimValue.CWTCustomClaimStringValue;
import r01f.cwt.model.CWTCustomClaimValue.CWTCustomClaimValueTyped;

public class CWTCustomClaimTransformer {

	public static CWTCustomClaim parseClaim(final CBORPair item) {
		return  new CWTCustomClaim() {
										@Override @SuppressWarnings("unchecked")
										public <K extends CWTCustomClaimKey> K getKey() {
											return (K) parseClaimKey(item.getKey());
										}

										@Override 	@SuppressWarnings("unchecked")
										public <V extends CWTCustomClaimValue> V getValue() {
											return (V) parseClaimValue(item.getValue());
										}
									};
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static CWTCustomClaimKey parseClaimKey(final CBORItem key ) {
		if ( key instanceof CBORInteger) {
			return new CWTCustomClaimIntegerKey( ((CBORInteger)key).getValue());
		}
		if ( key instanceof CBORString ) {
			return new CWTCustomClaimStringKey( ((CBORString)key).getValue());
		}
		Object obj = key.parse();
		return new CWTCustomClaimKeyTyped(obj.getClass(),obj);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	static CWTCustomClaimValue parseClaimValue(final CBORItem key ) {
		if ( key instanceof CBORInteger) {
			return new CWTCustomClaimIntegerValue( ((CBORInteger)key).getValue());
		}
		if ( key instanceof CBORString ) {
			return new CWTCustomClaimStringValue( ((CBORString)key).getValue());
		}
		Object obj = key.parse();
		return new CWTCustomClaimValueTyped(obj.getClass(),obj);
	}
}
