package r01f.db.entities.security;

import java.time.Instant;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.IdClass;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.guids.OID;
import r01f.model.security.login.pairedphone.PairedPhoneLoginConsentStatus;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObjectImpl;


@MappedSuperclass
@Multitenant(MultitenantType.SINGLE_TABLE)
@IdClass(DBPrimaryKeyForModelObjectImpl.class)
@Accessors(prefix="_")
@NoArgsConstructor
public abstract class DBEntityForPairedPhoneLoginConsent
	 extends DBEntityForSecurityObjectBase {

	private static final long serialVersionUID = 7767901110826179970L;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Column(name="PHONE",nullable=true,unique=false)
	@Basic
	@Getter @Setter protected String _phone;

	@Column(name = "USER_OID", nullable=true, length = OID.OID_LENGTH)
	@Basic
	@Getter	@Setter	protected String _userOid;

    @Column(name="STATUS",nullable=true)
    @Enumerated(EnumType.STRING)
	@Getter	@Setter	protected PairedPhoneLoginConsentStatus _status;

	@Column(name = "CONSENT_REQUEST_DATE", nullable=true, insertable = true, updatable = true)
	@Getter	@Setter
	protected Instant _requestAt;

	@Column(name = "CONSENT_RESPONSE_DATE", nullable=true, insertable = true, updatable = true)
	@Getter	@Setter protected Instant _responseAt;

}
