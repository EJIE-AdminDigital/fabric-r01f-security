package r01f.db.entities.security;

//import org.eclipse.persistence.annotations.Multitenant;
//import org.eclipse.persistence.annotations.MultitenantType;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@MappedSuperclass
//@Multitenant(MultitenantType.SINGLE_TABLE)
@Accessors(prefix="_")
@NoArgsConstructor
public abstract class DBEntityForSecurityObjectWithIDBase
		      extends DBEntityForSecurityObjectBase
		   implements IsDBEntityForSecurityObjectWithID {

	private static final long serialVersionUID = -187718742261139982L;

/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Column(name="ID",length=4000,nullable=false) @Basic
	@Getter @Setter protected String _id;
}
