package r01f.db.entities.keystore;

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

@MappedSuperclass
@Multitenant(MultitenantType.SINGLE_TABLE)
@Accessors(prefix="_")
@NoArgsConstructor
public abstract class DBEntityKeystoreWithPKBase
	 		  extends DBEntityKeystoreBase {
	private static final long serialVersionUID = 4742841541285523584L;
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Column(name="PRIVATE_KEY") @Lob @Basic(fetch=FetchType.EAGER)
	@Getter @Setter protected String _privateKey;

/////////////////////////////////////////////////////////////////////////////////////////
//  METHOD
/////////////////////////////////////////////////////////////////////////////////////////

}