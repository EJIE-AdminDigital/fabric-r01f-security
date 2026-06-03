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
@NoArgsConstructor
@Accessors(prefix="_")
public abstract class DBEntityForUserPasswordLogin
			  extends DBEntityForLoginBase {

	private static final long serialVersionUID = -6628025715362798888L;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Column(name="PASSWORD_HASH",nullable=false,length=OID.OID_LENGTH) @Basic
	@Getter @Setter protected String  _passwordHash;

	@Column(name="EXPIRATES_AT",
			insertable=true,updatable=true)
	@Getter @Setter protected Instant _expiratesAt;		// http://www.developerscrappad.com/228/java/java-ee/ejb3-jpa-dealing-with-date-time-and-timestamp/

	@Column(name="IS_CONFIRMED",nullable=true) //, columnDefinition = "boolean default true")
	@Getter @Setter protected boolean _isConfirmed;
}
