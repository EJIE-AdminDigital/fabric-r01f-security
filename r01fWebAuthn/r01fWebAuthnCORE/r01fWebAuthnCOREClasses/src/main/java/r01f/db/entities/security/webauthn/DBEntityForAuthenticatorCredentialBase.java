package r01f.db.entities.security.webauthn;

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
import r01f.guids.OID;
import r01f.model.security.webauthn.AuthenticatorCredential;
import r01f.services.core.persistence.db.DBEntityHasModelObjectDescriptor;
import r01f.services.core.persistence.db.entities.DBEntityBaseWithOID;

@MappedSuperclass
@Multitenant(MultitenantType.SINGLE_TABLE)
@NoArgsConstructor
@Accessors(prefix="_")
public abstract class DBEntityForAuthenticatorCredentialBase<C extends AuthenticatorCredential>
			  extends DBEntityBaseWithOID
	 implements DBEntityForAuthenticatorCredential,
   			    DBEntityHasModelObjectDescriptor {

	private static final long serialVersionUID = -8056898420233179664L;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Column(name="DESCRIPTION",length=4000,nullable=true) @Basic
	@Getter @Setter protected String _description;

	@Column(name="DESCRIPTOR") @Lob @Basic(fetch=FetchType.EAGER)
	@Getter @Setter protected String _descriptor;

	@Column(name="USER_OID",nullable=false,length=OID.OID_LENGTH) @Basic
	@Getter @Setter protected String _userOid;

	@Column(name="CREDENTIAL_ID",nullable=false,unique=true,length=OID.OID_LENGTH) @Basic
	@Getter @Setter protected String _credentialId;
}
