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

@MappedSuperclass
@Multitenant(MultitenantType.SINGLE_TABLE)

@NoArgsConstructor
@Accessors(prefix="_")
public abstract class DBEntityForGoogleLogin
			  extends DBEntityForLoginBase {

	private static final long serialVersionUID = 4369134740999153589L;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Column(name="EMAIL",nullable=false,unique=true) @Basic
	@Getter @Setter protected String _email;
}
