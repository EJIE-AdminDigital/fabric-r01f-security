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
public abstract class DBEntityForXLNetsLogin
			  extends DBEntityForLoginBase {

	private static final long serialVersionUID = 4975099453869498022L;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Column(name="WORKPLACE",nullable=true,unique=false) @Basic
	@Getter @Setter protected String _workPlace; // [Update: Allow to be null, in order to allow xlnets login without workplace vi certificate, bakq, etc..]
}
