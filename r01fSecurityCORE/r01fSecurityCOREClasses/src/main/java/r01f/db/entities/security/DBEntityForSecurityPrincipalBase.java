package r01f.db.entities.security;

//import org.eclipse.persistence.annotations.Multitenant;
//import org.eclipse.persistence.annotations.MultitenantType;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.SecurityPrincipalStatus;
import r01f.oids.SecurityIDs.IdentityProvider;

@MappedSuperclass
//@Multitenant(MultitenantType.SINGLE_TABLE)
@NoArgsConstructor
@Accessors(prefix="_")
public abstract class DBEntityForSecurityPrincipalBase
			  extends DBEntityForSecurityObjectWithIDBase {

	private static final long serialVersionUID = -6628025715362798888L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
////////// --- THE [IDENTITY PROVIDER] WHERE THE [SECURITY PRINCIPAL] COMES FROM
	@Column(name="SOURCE_IDENTITY_PROVIDER",nullable=false) 
	@Enumerated(EnumType.STRING)
	@Getter  @Setter protected IdentityProvider _sourceIdentityProvider;
	
////////// --- STATUS	
	@Column(name="STATUS",nullable=true) 
	@Enumerated(EnumType.STRING)
	@Getter	@Setter	protected SecurityPrincipalStatus _status;

////////// --- SEARCHED COLUMNS
	@Column(name="SEARCHED_COLS") @Basic
	@Getter @Setter protected String _searchedColumns;

}
