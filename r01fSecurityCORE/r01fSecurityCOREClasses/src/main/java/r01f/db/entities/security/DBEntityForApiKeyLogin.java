package r01f.db.entities.security;

import java.time.Instant;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.guids.OID;

@MappedSuperclass
@Multitenant(MultitenantType.SINGLE_TABLE)

@Accessors(prefix="_")
@NoArgsConstructor
public abstract class DBEntityForApiKeyLogin
     		  extends DBEntityForLoginBase {

	private static final long serialVersionUID = -8456777610752040838L;

/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Column(name="VERSION",length=OID.OID_LENGTH,nullable=false) @Basic
    @Getter @Setter protected String  _version;

	@Column(name="PUBLIC_KEY",length=1000,nullable=false) @Basic
	@Getter @Setter protected String _publicKey;

	@Column(name="READ_ONLY",nullable=false) @Basic
	@Getter @Setter protected boolean _readOnly;

	@Column(name="ACTIVE",nullable=false) @Basic
	@Getter @Setter protected boolean _active;

	@Column(name="VALID_FROM",
			nullable=false,
			insertable=true,updatable=false)
	@Getter @Setter protected Instant _validFrom;		// http://www.developerscrappad.com/228/java/java-ee/ejb3-jpa-dealing-with-date-time-and-timestamp/

	@Column(name="VALID_TO",
			nullable=true,		// can be null
			insertable=true,updatable=true)
	@Getter @Setter protected Instant _validTo;		// http://www.developerscrappad.com/228/java/java-ee/ejb3-jpa-dealing-with-date-time-and-timestamp/
}
