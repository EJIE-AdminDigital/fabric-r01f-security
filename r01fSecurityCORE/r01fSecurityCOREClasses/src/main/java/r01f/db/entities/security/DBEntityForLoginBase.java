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
import r01f.model.security.LoginStatus;
import r01f.oids.OID;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityBroker;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.LoginIDFormat;

@MappedSuperclass
//@Multitenant(MultitenantType.SINGLE_TABLE)

@NoArgsConstructor
@Accessors(prefix="_")
public abstract class DBEntityForLoginBase
			  extends DBEntityForSecurityObjectWithIDBase {

	private static final long serialVersionUID = -5690965024006669380L;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Column(name="ID_FORMAT",nullable=true) @Basic
	@Enumerated(EnumType.STRING)
	@Getter @Setter protected LoginIDFormat _idFormat;
	
////////// --- STATUS
	@Column(name="STATUS",nullable=false) @Basic
	@Enumerated(EnumType.STRING)
	@Getter @Setter protected LoginStatus _status;	
	
////////// --- PRINCIPAL 
	@Column(name="PRINCIPAL_OID",nullable=false,length=OID.OID_LENGTH) @Basic
	@Getter @Setter protected String _securityPrincipalOid;
	
	@Column(name="PRINCIPAL_ID",nullable=false,length=OID.OID_LENGTH) @Basic
	@Getter @Setter protected String _securityPrincipalId;
	
	@Column(name="PRINCIPAL_SRC_IDENTITY_PROVIDER",nullable=false) 
	@Enumerated(EnumType.STRING)
	@Getter  @Setter protected IdentityProvider _securityPrincipalSourceIdentityProvider;
	
////////// --- AUTHENTICATION METHOD 
	@Column(name="AUTHENTICATION_METHOD",nullable=true) @Basic
	@Enumerated(EnumType.STRING)
	@Getter  @Setter protected AuthenticationMethod _authenticationMethod;
	
	@Column(name="IDENTITY_BROKER",nullable=true) @Basic
	@Enumerated(EnumType.STRING)
	@Getter @Setter protected IdentityBroker _identityBroker;
	
////////// --- VALIDITY RANGE
	@Column(name="VALID_FROM",nullable=true) @Basic
	@Getter @Setter protected Instant _validFrom;
	
	@Column(name="VALID_TO",nullable=true) @Basic
	@Getter @Setter protected Instant _validTo;	
}
