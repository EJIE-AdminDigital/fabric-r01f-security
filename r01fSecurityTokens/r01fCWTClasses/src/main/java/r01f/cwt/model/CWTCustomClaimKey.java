package r01f.cwt.model;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

public interface CWTCustomClaimKey {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
     public <T> Class<T> getKeyType();
     public <T> T asRawValue();
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////     
     @RequiredArgsConstructor
     @Accessors(prefix="_")
     public static class CWTCustomClaimKeyTyped<T>
          	  implements CWTCustomClaimKey {
    	 
	    final   Class<T>     _keyType;
	    final   T            _rawKey;

		@Override  @SuppressWarnings({ "unchecked","hiding" })
		public <T> Class<T> getKeyType() {
			return (Class<T>) _keyType;
		}
		@Override  @SuppressWarnings({ "unchecked","hiding" })
		public <T> T asRawValue() {
			return (T) _rawKey;
		}
		@Override
		public int hashCode() {
			return _rawKey != null ? this._rawKey.hashCode()
			                       : super.hashCode();
		}
		@Override
		public boolean equals(final Object o) {
			if (this == o) return true;
			if (o == null) return false;
			if (!(o instanceof CWTCustomClaimKeyTyped)) return false;

			@SuppressWarnings("rawtypes")
			CWTCustomClaimKeyTyped other = (CWTCustomClaimKeyTyped)o;
			return _rawKey != null && other.asRawValue() != null ? _rawKey.equals(other.asRawValue())
															     : _rawKey != null ? false
							 : true;	// both null
		}
     }
/////////////////////////////////////////////////////////////////////////////////////////
//	CAN BE EXTENDED
/////////////////////////////////////////////////////////////////////////////////////////     
     @Accessors(prefix="_")
     public static class CWTCustomClaimStringKey
          		 extends CWTCustomClaimKeyTyped<String> {

		public CWTCustomClaimStringKey(final String key) {
			super(String.class, key);
		}
		@Override
		public boolean equals(final Object o) {
			if (this == o) return true;
			if (o == null) return false;
			if (!(o instanceof CWTCustomClaimStringKey)) return false;

			CWTCustomClaimStringKey other = (CWTCustomClaimStringKey)o;
			return _rawKey != null && other.asRawValue() != null ? _rawKey.equals(other.asRawValue())
															     : _rawKey != null ? false
							 : true;	// both null
		}
		public static CWTCustomClaimKey from(final String customClaimKey) {
			return new CWTCustomClaimStringKey(customClaimKey);
		}
    }
    @Accessors(prefix="_")
    public static class CWTCustomClaimIntegerKey
          		 extends CWTCustomClaimKeyTyped<Integer> {

		public CWTCustomClaimIntegerKey(final Integer key) {
			super(Integer.class, key);
		}
		@Override
		public boolean equals(final Object o) {
			if (this == o) return true;
			if (o == null) return false;
			if (!(o instanceof CWTCustomClaimStringKey)) return false;

			CWTCustomClaimIntegerKey other = (CWTCustomClaimIntegerKey)o;
			return _rawKey != null && other.asRawValue() != null ? _rawKey == other.asRawValue()
															     : _rawKey != null ? false
							 : true;	// both null
		}
    }
}
