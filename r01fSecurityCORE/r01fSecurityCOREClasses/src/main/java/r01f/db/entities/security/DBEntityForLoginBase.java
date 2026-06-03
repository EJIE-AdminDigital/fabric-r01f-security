package r01f.db.entities.security;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.guids.OID;

@MappedSuperclass
@Multitenant(MultitenantType.SINGLE_TABLE)

@NoArgsConstructor
@Accessors(prefix="_")
public abstract class DBEntityForLoginBase
			  extends DBEntityForSecurityObjectBase
		   implements DBEntityForLogin {

	private static final long serialVersionUID = -5690965024006669380L;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Column(name="USER_OID",nullable=false,length=OID.OID_LENGTH) @Basic
	@Getter @Setter protected String _userOid;

	@Column(name="LOGIN_ID",nullable=false,unique=true,length=OID.OID_LENGTH) @Basic
	@Getter @Setter protected String _loginId;
}
