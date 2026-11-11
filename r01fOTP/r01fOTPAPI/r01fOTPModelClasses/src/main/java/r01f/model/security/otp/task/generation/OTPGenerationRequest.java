package r01f.model.security.otp.task.generation;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.otp.IsOTPObject;
import r01f.model.security.otp.OTPRequestBase;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.CommonOIDs.AppCode;
import r01f.oids.CommonOIDs.AppComponent;
import r01f.oids.SecurityIDs.Secret;


@MarshallType(as="otpGenerationRequest")
@Accessors(prefix="_")
public class OTPGenerationRequest
	 extends OTPRequestBase
  implements IsOTPObject {
	private static final long serialVersionUID = 6848253460622291571L;
///////////////////////////////////////////////////////////////////////////////////////
// 	FIELDS
///////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="appCode",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter AppCode _appCode; 				// The requesting app code

	@MarshallField(as="appComponent",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter AppComponent  _appComponent; 	// The requesting app component

	@MarshallField(as="type",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter OTPSecretType _secretType;

	@MarshallField(as="length",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter long _secretLength = 5;  		// OTP length

	@MarshallField(as="ttl",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter long _TTLSeconds = -1;		   // OTP TTL (time to live): -1 no expiration

	@MarshallField(as="maxRetries",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter long _maxRetries = -1;

	@MarshallField(as="fixedValue", //..used for testing purposes
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter Secret _fixedValue;


}