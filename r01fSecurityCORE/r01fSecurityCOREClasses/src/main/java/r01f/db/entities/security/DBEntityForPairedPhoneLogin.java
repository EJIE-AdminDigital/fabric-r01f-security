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
import r01f.services.core.persistence.db.entities.primarykeys.DBPrimaryKeyForModelObjectImpl;


@MappedSuperclass
@Multitenant(MultitenantType.SINGLE_TABLE)
@IdClass(DBPrimaryKeyForModelObjectImpl.class)
@Accessors(prefix="_")
@NoArgsConstructor
public abstract class DBEntityForPairedPhoneLogin
	 extends DBEntityForLoginBase {

	private static final long serialVersionUID = 7767901110826179970L;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Column(name="PHONE",nullable=false,unique=true) @Basic
	@Getter @Setter protected String _phone;
}
