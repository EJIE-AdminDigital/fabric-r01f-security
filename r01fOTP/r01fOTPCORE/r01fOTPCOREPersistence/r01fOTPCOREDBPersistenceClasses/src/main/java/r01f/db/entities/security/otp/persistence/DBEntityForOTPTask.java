package r01f.db.entities.security.otp.persistence;

import java.time.Instant;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.services.core.persistence.db.entities.DBEntityBaseWithOID;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObjectImpl;

@Entity @Cacheable(false)
@IdClass(DBPrimaryKeyForModelObjectImpl.class)
//@Multitenant(MultitenantType.SINGLE_TABLE)
//@Table(name="OTPTASK")
@Accessors(prefix="_")
@NoArgsConstructor
public  class DBEntityForOTPTask
     extends DBEntityBaseWithOID {

	private static final long serialVersionUID = -680786326519889653L;
///////////////////////////////////////////////////////////////////////////////////////
// 	FIELDS
///////////////////////////////////////////////////////////////////////////////////////
	@Column(name="APP_CODE")
	@Setter @Getter String _appCode;

	@Column(name="APP_COMPONENT")
	@Setter @Getter String  _appComponent;

	@Column(name="SECRET")
	@Setter @Getter String _secret;     // TODO secure this, do not store as plain text

	@Column(name="REMAINING_RETRY_NUM")
	@Setter @Getter long _remainingRetryNum = 0;

	// If OTPOID is not available _phone o email could be used as identifier of OTP.
	@Column(name="PHONE")
	@Setter @Getter String _phone;

	@Column(name="EMAIL")
	@Setter @Getter String _email;

	@Column(name="EXPIRATION_DATE")
	@Setter @Getter Instant _expirationDate;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void _preCreate() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void _preUpdate() {
		// TODO Auto-generated method stub
	}
}
