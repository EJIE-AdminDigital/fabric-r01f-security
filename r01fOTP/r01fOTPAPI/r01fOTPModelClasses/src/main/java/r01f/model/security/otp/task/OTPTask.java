package r01f.model.security.otp.task;


import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.guids.OID;
import r01f.model.PersistableModelObjectBase;
import r01f.model.security.otp.OTPPersistableObject;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.securitycontext.SecurityIDS.Password;
import r01f.types.contact.EMail;
import r01f.types.contact.Phone;

@MarshallType(as="otpTask")
@Accessors(prefix="_")
public class OTPTask
     extends PersistableModelObjectBase<OTPTaskOID, OTPTask>
  implements OTPPersistableObject<OTPTaskOID> {

	private static final long serialVersionUID = -680786326519889653L;
///////////////////////////////////////////////////////////////////////////////////////
// 	FIELDS
///////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="appCode",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter AppCode _appCode;  				//  The requesting app code

	@MarshallField(as="appComponent",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter AppComponent  _appComponent;    //  Requesting  APP Cdde If needed

	@MarshallField(as="secret",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter Password _secret;     			//  The OTP Generated secret;

	@MarshallField(as="remainingRetryNum",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter long _remainingRetryNum = 0;

	// If OTPOID is not available _phone o email could be used as identifier of OTP.
	@MarshallField(as="phone",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter Phone  _phone;

	@MarshallField(as="email",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter EMail  _email;

	// Expiration Date, Only used in DB implementation
	@MarshallField(as="expirationDate")
	@Setter @Getter Instant _expirationDate;

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public OTPTask() {
		// default no-args constructor
	}
	public OTPTask(final OTPTaskOID oid) {
		_oid = oid;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void unsafeSetOid(final OID oid) {
		setOid((OTPTaskOID) oid);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public long decrementRemainingRetryNumber() {
		_remainingRetryNum = _remainingRetryNum - 1;
		return _remainingRetryNum;
	}

}