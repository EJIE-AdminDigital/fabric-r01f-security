package r01f.db.entities.security;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.IdClass;
import jakarta.persistence.Lob;
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
public abstract class DBEntityForAuthorizationTargetResource
			  extends DBEntityForSecurityObjectBase {

	private static final long serialVersionUID = -7565664747530887879L;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Column(name="APP_CODE",length=OID.OID_LENGTH,nullable=false) @Basic
	@Getter @Setter protected String  _appCode;

	@Column(name="APP_COMPONENT",length=OID.OID_LENGTH,nullable=false) @Basic
	@Getter @Setter protected String  _appComponent;

	@Column(name="RESOURCES") @Lob @Basic(fetch=FetchType.EAGER)
	@Getter @Setter protected String _resources;

}
