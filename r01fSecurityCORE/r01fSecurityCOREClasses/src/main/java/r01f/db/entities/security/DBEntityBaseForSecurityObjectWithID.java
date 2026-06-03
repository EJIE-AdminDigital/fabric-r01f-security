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
import r01f.services.core.persistence.db.DBEntityHasModelObjectDescriptor;
import r01f.services.core.persistence.db.entities.DBEntityForModelObject;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;

@MappedSuperclass
@Multitenant(MultitenantType.SINGLE_TABLE)
@Accessors(prefix="_")
@NoArgsConstructor
public abstract class DBEntityBaseForSecurityObjectWithID
		      extends DBEntityForSecurityObjectBase
		   implements DBEntityForModelObject<DBPrimaryKeyForModelObject>,
		  			  DBEntityHasModelObjectDescriptor {

	private static final long serialVersionUID = -187718742261139982L;

/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Column(name="ID",length=4000,nullable=false) @Basic
	@Getter @Setter protected String _id;
}
