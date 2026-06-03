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
public abstract class DBEntityForUserLoginEntry
              extends DBEntityForSecurityObjectBase {

	private static final long serialVersionUID = -7685141208539038818L;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
////////// Common fields
	@Column(name="ENTRY_TYPE",nullable=true,length=OID.OID_LENGTH) @Basic
	@Getter @Setter protected String _entryType;

	@Column(name="ENTRY_DATE",
			insertable=true,updatable=false)
	@Getter @Setter protected Instant _entryDate;

	@Column(name="SECURITY_PROVIDER",nullable=true,length=OID.OID_LENGTH) @Basic
	@Getter @Setter protected String _securityProvider;

	@Column(name="LOGIN_ID",nullable=true,length=OID.OID_LENGTH) @Basic
	@Getter @Setter protected String _loginId;

	@Column(name="USER_OID",nullable=false,unique=false,length=OID.OID_LENGTH) @Basic
	@Getter @Setter protected String _userOid;

////////// Security token used when reseting user-password login's password or when impersonating
	@Column(name="TOKEN",length=4000,nullable=true) @Basic
	@Getter @Setter protected String _token;

////////// Impersonation
	@Column(name="IMPERSONATED_USER_OID",length=OID.OID_LENGTH,nullable=true) @Basic
	@Getter @Setter protected String _impersonatedUserOid;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void _preCreate() {
		// nothing
	}
	@Override
	protected void _preUpdate() {
		// nothing
	}
}
