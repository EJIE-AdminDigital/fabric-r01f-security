package r01f.db.entities.security;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.IdClass;
import jakarta.persistence.MappedSuperclass;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.guids.OID;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObjectImpl;

@MappedSuperclass
@IdClass(DBPrimaryKeyForModelObjectImpl.class)
@Multitenant(MultitenantType.SINGLE_TABLE)
@NoArgsConstructor
@Accessors(prefix="_")
public abstract class DBEntityForUserAuthorizationOnResource
			  extends DBEntityForSecurityObjectBase {

	private static final long serialVersionUID = -7565664747530887879L;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Column(name="USER_OID",length=OID.OID_LENGTH,nullable=false) @Basic
	@Getter @Setter protected String  _userOid;

	@Column(name="RESOURCE_OID",length=OID.OID_LENGTH,nullable=false) @Basic
	@Getter @Setter protected String _resourceOid;

	@Column(name="ROLE",length=OID.OID_LENGTH) @Basic
	@Getter @Setter protected String  _role;

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
