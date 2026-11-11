package r01f.model.security.otp;


import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.PersistableModelObjectBase;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.CommonOIDs.AppCode;
import r01f.oids.CommonOIDs.AppComponent;
import r01f.oids.OID;
import r01f.oids.SecurityIDs.Secret;
import r01f.types.contact.EMail;
import r01f.types.contact.IsContactMean;
import r01f.types.contact.Phone;

@MarshallType(as="otpTask")
@Accessors(prefix="_")
public class OTPTask
     extends PersistableModelObjectBase<OTPTaskOID,OTPTask>
  implements IsOTPPersistableObject<OTPTaskOID> {

	private static final long serialVersionUID = -680786326519889653L;
///////////////////////////////////////////////////////////////////////////////////////
// 	FIELDS
///////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="appCode",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter AppCode _appCode;  				//  The requesting app code

	@MarshallField(as="appComponent",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter AppComponent _appComponent;    //  Requesting  APP Cdde If needed

	@MarshallField(as="secret",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter Secret _secret;     			//  The OTP Generated secret;

	@MarshallField(as="remainingRetryNum",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter long _remainingRetryNum = 0;

	// If OTPOID is not available _phone o email could be used as identifier of OTP.
	@MarshallField(as="contactMean")
	@Setter @Getter IsContactMean _contactMean;

	// Expiration Date, Only used in DB implementation
	@MarshallField(as="expiresAt")
	@Setter @Getter Instant _expiresAt;

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
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public boolean usesEMail() {
		return _contactMean != null && _contactMean.isEMail();
	}
	public boolean usesPhone() {
		return _contactMean != null && _contactMean.isPhone();
	}
	public EMail getContactMeanAsEMail() {
		if (!this.usesEMail()) throw new IllegalStateException("OTPTask does not use an EMail contact mean");
		return _contactMean.asEMail();
	}
	public Phone getContactMeanAsPhone() {
		if (!this.usesPhone()) throw new IllegalStateException("OTPTask does not use a Phone contact mean");
		return _contactMean.asPhone();
	}
}