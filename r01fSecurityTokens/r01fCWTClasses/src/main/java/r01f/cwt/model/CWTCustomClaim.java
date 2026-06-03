package r01f.cwt.model;

import r01f.cwt.model.CWTCustomClaimKey.CWTCustomClaimIntegerKey;
import r01f.cwt.model.CWTCustomClaimKey.CWTCustomClaimStringKey;
import r01f.cwt.model.CWTCustomClaimValue.CWTCustomClaimStringValue;
import r01f.cwt.model.CWTCustomClaimValue.CWTCustomClaimValueTyped;

public interface CWTCustomClaim {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	 public <K extends CWTCustomClaimKey>    K  getKey();
	 public <V extends CWTCustomClaimValue>  V getValue();
/////////////////////////////////////////////////////////////////////////////////////////
//	BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////	 
	 public static CWTCustomClaim create(final String key, final String value) {
		 return new  CWTCustomClaim() {
							@Override @SuppressWarnings("unchecked")
							public <K extends CWTCustomClaimKey> K getKey() {
								return (K)new CWTCustomClaimStringKey(key);
							}
							@Override @SuppressWarnings("unchecked")
							public <V extends CWTCustomClaimValue> V getValue() {
								return (V)new CWTCustomClaimStringValue(value);
							}

					 };
	 }
	 public static CWTCustomClaim create(final int key, final String value) {
		 return new  CWTCustomClaim() {
							@Override @SuppressWarnings("unchecked")
							public <K extends CWTCustomClaimKey> K getKey() {
								return (K)new CWTCustomClaimIntegerKey(key);
							}
							@SuppressWarnings("unchecked")
							@Override
							public <V extends CWTCustomClaimValue> V getValue() {
								return (V)new CWTCustomClaimStringValue(value);
							}
					 };
	 }
	 public static CWTCustomClaim create(final String key, final Object value) {
		 return new  CWTCustomClaim() {
						@Override @SuppressWarnings("unchecked")
						public <K extends CWTCustomClaimKey> K getKey() {
							return (K)new CWTCustomClaimStringKey(key);
						}
						@Override 	@SuppressWarnings({ "unchecked", "rawtypes" })
						public <V extends CWTCustomClaimValue> V getValue() {
							return (V)new CWTCustomClaimValueTyped(value.getClass(), value);
						}
					 };
	 }
	 public static CWTCustomClaim create(final int key, final Object value) {
		 return new  CWTCustomClaim() {
						@Override @SuppressWarnings("unchecked")
						public <K extends CWTCustomClaimKey> K getKey() {
							return (K)new CWTCustomClaimIntegerKey(key);
						}
						@Override @SuppressWarnings({ "unchecked", "rawtypes" })
						public <V extends CWTCustomClaimValue> V getValue() {
							return (V)new CWTCustomClaimValueTyped(value.getClass(), value);
						}
					 };
	 }
}
