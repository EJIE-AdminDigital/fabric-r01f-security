package r01f.db.entities.keystore;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.services.core.persistence.db.DBEntityHasModelObjectDescriptor;
import r01f.services.core.persistence.db.entities.DBEntityBaseWithOID;
import r01f.services.core.persistence.db.entities.DBEntityForModelObject;
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObject;

@MappedSuperclass
//@Entity
@Accessors(prefix="_")
//@Multitenant(MultitenantType.SINGLE_TABLE)
//@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name="TYPE", discriminatorType=DiscriminatorType.STRING)
public abstract class DBEntityKeystoreBase
			  extends DBEntityBaseWithOID
		   implements DBEntityForModelObject<DBPrimaryKeyForModelObject>,
					  DBEntityHasModelObjectDescriptor {
	private static final long serialVersionUID = -6346709575916216971L;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Column(name="PUBLIC_KEY") @Lob @Basic(fetch=FetchType.EAGER)
	@Getter @Setter protected String _publicKey;

	@Column(name="APP_VERSION") @Basic
	@Getter @Setter protected String _appVersion;

	@Column(name="DESCRIPTOR") @Lob @Basic(fetch=FetchType.EAGER)
	@Getter @Setter protected String _descriptor;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	public <K extends DBEntityKeystoreBase> K as(final Class<K> k) {
		return (K)this;
	}

/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void _preCreate() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void _preUpdate() {
		// TODO Auto-generated method stub

	}
}
