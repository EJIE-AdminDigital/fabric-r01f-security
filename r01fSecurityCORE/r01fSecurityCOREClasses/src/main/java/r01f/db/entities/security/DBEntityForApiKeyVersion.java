package r01f.db.entities.security;

import java.time.Instant;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.guids.OID;
import r01f.services.core.persistence.db.DBEntityHasModelObjectDescriptor;
import r01f.services.core.persistence.db.entities.DBEntityBaseWithOID;

@MappedSuperclass
@Multitenant(MultitenantType.SINGLE_TABLE)

@Accessors(prefix="_")
@NoArgsConstructor
public abstract class DBEntityForApiKeyVersion
     		  extends DBEntityBaseWithOID
           implements DBEntityForSecurityObject,
  			     	  DBEntityHasModelObjectDescriptor {

	private static final long serialVersionUID = -8456777610752040838L;

/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Column(name="CODE",length=OID.OID_LENGTH,nullable=true) @Basic
    @Getter @Setter protected String  _id;

	@Column(name="NAME",length=100,nullable=false) @Basic
	@Getter @Setter protected String _name;

	@Column(name="DESCRIPTION",length=4000,nullable=true) @Basic
	@Getter @Setter protected String _description;

	@Column(name="DESCRIPTOR") @Lob @Basic(fetch=FetchType.EAGER)
	@Getter @Setter protected String _descriptor;

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
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void _preCreate() {
		// pre-create
	}
	@Override
	protected void _preUpdate() {
		// pre-update
	}
}
