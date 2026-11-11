package r01f.db.entities.security;

//import org.eclipse.persistence.annotations.Multitenant;
//import org.eclipse.persistence.annotations.MultitenantType;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.IdClass;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.oids.OID;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObjectImpl;

@MappedSuperclass
@IdClass(DBPrimaryKeyForModelObjectImpl.class)
//@Multitenant(MultitenantType.SINGLE_TABLE)
@NoArgsConstructor
@Accessors(prefix="_")
public abstract class DBEntityForAuthorizationOnResourceBase
			  extends DBEntityForSecurityObjectBase {

	private static final long serialVersionUID = -7565664747530887879L;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
////////// --- PRINCIPAL & SOURCE IDENTITY PROVIDER	
	@Column(name="PRINCIPAL_OID",nullable=false,length=OID.OID_LENGTH) @Basic
	@Getter @Setter protected String _securityPrincipalOid;
	
////////// --- RESOURCE OID & ROLE
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
