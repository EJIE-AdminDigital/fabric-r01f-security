package r01f.db.entities.security;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.MappedSuperclass;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.services.core.persistence.db.DBEntityHasModelObjectDescriptor;
import r01f.services.core.persistence.db.entities.DBEntityBaseWithOID;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObjectImpl;

@MappedSuperclass
@Multitenant(MultitenantType.SINGLE_TABLE)
@Accessors(prefix = "_")
@NoArgsConstructor
public abstract class DBEntityForPushToken
			  extends DBEntityBaseWithOID
		   implements DBEntityHasModelObjectDescriptor {

	private static final long serialVersionUID = 1280257537786910664L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Column(name="PUSH_TOKEN_ID",nullable=false) @Basic
	@Getter @Setter protected String _pushTokenId;
	
	@Column(name="PUSH_TOKEN_ID_TYPE",nullable=false) @Basic
	@Getter @Setter protected String _pushTokenIdType;	

	@Column(name="TOKEN",nullable=true) @Basic
    @Getter @Setter protected String _token;

	@Column(name="SYSTEM_ID",nullable=true) @Basic
    @Getter @Setter protected String _systemId;
	
	@Column(name="PLATAFORM",nullable=true) @Basic
    @Getter @Setter protected String _plataform;

	@Column(name = "DESCRIPTOR") @Lob @Basic(fetch = FetchType.EAGER)
	@Getter @Setter protected String _descriptor;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public DBPrimaryKeyForModelObject getDBEntityPrimaryKey() {
		return DBPrimaryKeyForModelObjectImpl.from(_oid);
	}
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
