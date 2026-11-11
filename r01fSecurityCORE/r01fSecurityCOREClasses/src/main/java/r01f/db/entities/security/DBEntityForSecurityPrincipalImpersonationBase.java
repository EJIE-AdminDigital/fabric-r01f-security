package r01f.db.entities.security;

import java.time.Instant;

//import org.eclipse.persistence.annotations.Multitenant;
//import org.eclipse.persistence.annotations.MultitenantType;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.oids.OID;
import r01f.services.core.persistence.db.entities.DBEntityBaseWithOID;

@MappedSuperclass
//@Multitenant(MultitenantType.SINGLE_TABLE)
@NoArgsConstructor
@Accessors(prefix="_")
public abstract class DBEntityForSecurityPrincipalImpersonationBase
              extends DBEntityBaseWithOID	// NO descriptor!!
           implements IsDBEntityForSecurityObject {

	private static final long serialVersionUID = -7685141208539038818L;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
////////// --- DATE RANGE
	@Column(name="STARTED_AT",
			insertable=true,updatable=false)
	@Getter @Setter protected Instant _startedAt;
	
	@Column(name="FINISHED_AT",
			insertable=true,updatable=false)
	@Getter @Setter protected Instant _finishedAt;
	
////////// IMPERSONATION > the [principal] being impersonated
	@Column(name="IMPERSONATED_OID",length=OID.OID_LENGTH,nullable=true) @Basic
	@Getter @Setter protected String _impersonatedPrincipalOid;
	
	@Column(name="IMPERSONATING_OID",length=OID.OID_LENGTH,nullable=true) @Basic
	@Getter @Setter protected String _impersonatingPrincipalOid;
}
