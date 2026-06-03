package r01f.cwt.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

public interface CWTCustomClaimValue {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
    public <T> Class<T> getValueType();

    public <T> T asRawValue();

    public <C extends CWTCustomClaimValue> C as(final Class<C> classType);

    public CWTCustomClaimStringValue  asStringValue();

    public CWTCustomClaimIntegerValue asIntegerValue();
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
    @RequiredArgsConstructor
    @Accessors(prefix="_")
    public static class CWTCustomClaimValueTyped<T>
             implements CWTCustomClaimValue {

    	        final Class<T> _valueType;
    	@Getter final T _rawValue;

		@Override @SuppressWarnings({ "unchecked","hiding" })
		public <T> Class<T> getValueType() {
			return (Class<T>) _valueType;
		}
		@Override @SuppressWarnings({ "unchecked","hiding" })
		public <T> T asRawValue() {
			return (T) _rawValue;
		}
		@Override @SuppressWarnings("unchecked")
		public <C extends CWTCustomClaimValue> C as( final Class<C> classType) {
			  return (C)this;
		}
		@Override
		public CWTCustomClaimStringValue asStringValue() {
			return as(CWTCustomClaimStringValue.class);
		}
		@Override
		public CWTCustomClaimIntegerValue asIntegerValue() {
			return as(CWTCustomClaimIntegerValue.class);
		}
    }
/////////////////////////////////////////////////////////////////////////////////////////
//	... CAN BE EXTENDED
/////////////////////////////////////////////////////////////////////////////////////////    
    @Accessors(prefix="_")
    public static class CWTCustomClaimStringValue
          	    extends CWTCustomClaimValueTyped<String> {

		public CWTCustomClaimStringValue(final String calue) {
			super(String.class, calue);
		}
    }
    @Accessors(prefix="_")
    public static class CWTCustomClaimIntegerValue
          extends CWTCustomClaimValueTyped<Integer> {

		public CWTCustomClaimIntegerValue(final Integer calue) {
			super(Integer.class, calue);
		}
    }
}
