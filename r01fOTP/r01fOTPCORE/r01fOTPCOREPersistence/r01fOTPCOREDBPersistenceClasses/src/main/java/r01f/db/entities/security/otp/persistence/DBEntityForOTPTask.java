package r01f.db.entities.security.otp.persistence;

import java.time.Instant;

import jakarta.persistence.Basic;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.IdClass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.oids.OID;
import r01f.services.core.persistence.db.entities.DBEntityBaseWithOID;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObjectImpl;
import r01f.types.contact.ContactMeanType;

@Entity @Cacheable(false)
@IdClass(DBPrimaryKeyForModelObjectImpl.class)
//@Multitenant(MultitenantType.SINGLE_TABLE)
//@Table(name="OTPTASK")
@Accessors(prefix="_")
@NoArgsConstructor
public class DBEntityForOTPTask
     extends DBEntityBaseWithOID {

	private static final long serialVersionUID = -680786326519889653L;
///////////////////////////////////////////////////////////////////////////////////////
// 	FIELDS
///////////////////////////////////////////////////////////////////////////////////////
	@Column(name="APP_CODE",length = OID.OID_LENGTH) @Basic
	@Getter @Setter String _appCode;
 
	@Column(name="APP_COMPONENT",length = OID.OID_LENGTH) @Basic
	@Getter @Setter String _appComponent;

	@Column(name="SECRET",length = OID.OID_LENGTH) @Basic
	@Getter @Setter String _secret;     // TODO secure this, do not store as plain text

	@Column(name="REMAINING_RETRY_NUM")
	@Setter @Getter long _remainingRetryNum = 0;

	// If OTPOID is not available _phone o email could be used as identifier of OTP.
	@Column(name="CONTACT_MEAN",length = OID.OID_LENGTH) @Basic
	@Getter @Setter String _contactMean;  // PHONE or EMAIL
	
	@Column(name="CONTACT_MEAN_TYPE") @Basic
	@Enumerated(EnumType.STRING)
	@Getter @Setter ContactMeanType _contactMeanType;

	@Column(name="EXPIRES_AT") @Basic
	@Getter @Setter Instant _expiresAt;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void _preCreate() {
		// Auto-generated method stub
	}
	@Override
	protected void _preUpdate() {
		// Auto-generated method stub
	}
}
