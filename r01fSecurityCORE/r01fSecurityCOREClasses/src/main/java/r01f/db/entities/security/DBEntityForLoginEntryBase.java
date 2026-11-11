package r01f.db.entities.security;

import java.time.Instant;


import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.oids.OID;
import r01f.oids.SecurityIDs.LoginEntryEvent;
import r01f.services.core.persistence.db.entities.DBEntityBaseWithOID;

@MappedSuperclass

@NoArgsConstructor
@Accessors(prefix="_")
public abstract class DBEntityForLoginEntryBase
              extends DBEntityBaseWithOID	// NO descriptor!!
           implements IsDBEntityForSecurityObject {

	private static final long serialVersionUID = -7685141208539038818L;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
////////// Common fields
	@Column(name="ENTRY_EVENT",nullable=false) @Basic
	@Enumerated(EnumType.STRING)
	@Getter @Setter protected LoginEntryEvent _event;

	@Column(name="AT",
			insertable=true,updatable=false)
	@Getter @Setter protected Instant _at;

////////// LOGIN
	@Column(name="LOGIN_OID",nullable=false,length=OID.OID_LENGTH) @Basic
	@Getter @Setter protected String _loginOid;
	
	@Column(name="LOGIN_ID",nullable=false,length=OID.OID_LENGTH) @Basic
	@Getter @Setter protected String _loginId;
	
////////// PRINCIPAL
	@Column(name="PRINCIPAL_OID",nullable=false,length=OID.OID_LENGTH) @Basic
	@Getter @Setter protected String _securityPrincipalOid;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	protected void _preCreate() {
		// nothing
	}
	@Override
	protected void _preUpdate() {
		// nothing
	}
}
