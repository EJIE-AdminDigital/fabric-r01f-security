package r01f.model.security.otp.task.validation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.model.security.otp.OTPRequestBase;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.oids.CommonOIDs.AppCode;
import r01f.oids.CommonOIDs.AppComponent;
import r01f.oids.SecurityIDs.Secret;



@Accessors(prefix="_")
@RequiredArgsConstructor
abstract class OTPValidationRequestBase<T>	// T = email, phone, taskOid
	   extends OTPRequestBase
    implements IsOTPValidationRequest<T> {

	private static final long serialVersionUID = 1206292544725125704L;
///////////////////////////////////////////////////////////////////////////////////////
// 	FIELDS
///////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="appCode",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter protected final AppCode _appCode;
	
	@MarshallField(as="appComponent",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter protected final AppComponent _appComponent;
	
	@MarshallField(as="taskOid",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter protected final OTPTaskOID _taskOid;

	@MarshallField(as="key")
	@Getter protected final T _key;
	
	@MarshallField(as="secret")
	@Getter protected final Secret _secret;
}