package r01f.model.security.keystore.key;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.crypto.KeySpecAlgorithmFamily;
import r01f.crypto.KeySpecAlgorithmStdName;
import r01f.crypto.KeySpecPKCS;
import r01f.model.ModelObject;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.types.StringBase64;
import r01f.util.types.Strings;

@NoArgsConstructor
@Accessors(prefix="_")
public abstract class KeyBase<SELF_TYPE extends KeyBase<SELF_TYPE>>
           implements ModelObject {

	private static final long serialVersionUID = -2375099769288630707L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="keyValue", escape=true)
	@Getter @Setter private StringBase64 _keyValue;

///////////////////////////////////////////////////////////////////////////////////////
 // Optional! keySpecs
///////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="algorithm",
			       whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private KeySpecAlgorithmFamily  _algorithm;

	@MarshallField(as="algorithmStdName",
			       whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private KeySpecAlgorithmStdName _algorithmStdName;

	@MarshallField(as="pkcs",
			       whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private KeySpecPKCS _pkcs;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public boolean isNullOrEmpty() {
		return _keyValue == null
			 || Strings.isNullOrEmpty(_keyValue.asString());
	}
	@SuppressWarnings("unchecked")
	public SELF_TYPE withValue(final String privateKeyAsBase64) {
		_keyValue =  new StringBase64(privateKeyAsBase64);
		return (SELF_TYPE)this;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	FLUENT
/////////////////////////////////////////////////////////////////////////////////////////	
	@SuppressWarnings("unchecked")
	public SELF_TYPE withValueAsBase64String(final String publicKeyValueAsBas64) {
		_keyValue = new  StringBase64(publicKeyValueAsBas64);
		return (SELF_TYPE)this;
	}
	@SuppressWarnings("unchecked")
	public SELF_TYPE withValueAsRawString(final String rawString) {
		_keyValue = StringBase64.encode(rawString);
		return (SELF_TYPE)this;
	}
	@SuppressWarnings("unchecked")
	public SELF_TYPE withValueAsBytes(final byte[]  bytes) {
		_keyValue = StringBase64.encode(bytes);
		return (SELF_TYPE)this;
	}
}