package r01f.model.security.principal;


import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.aspects.interfaces.dirtytrack.ConvertToDirtyStateTrackable;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.SecurityPersistableObjectWithIDBase;
import r01f.model.security.SecurityPrincipalStatus;
import r01f.model.security.SecurityRoleInModule;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.SecurityPrincipalID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.util.types.Objects;

@ConvertToDirtyStateTrackable
@Accessors(prefix="_")
public abstract class SecurityPrincipalBase<SELF_TYPE extends SecurityPrincipalBase<SELF_TYPE>> 
			  extends SecurityPersistableObjectWithIDBase<SecurityPrincipalOID,SecurityPrincipalID,
			  											  SELF_TYPE>
           implements IsSecurityPrincipal {

    private static final long serialVersionUID = -9060778463691631210L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
////////// --- IDENTITY PROVIDER 
    @MarshallField(as="sourceIdentityProvider",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter protected IdentityProvider _sourceIdentityProvider;
    
////////// --- STATUS
    @MarshallField(as="status",
    			   whenXml=@MarshallFieldAsXml())
    @Getter @Setter protected SecurityPrincipalStatus _status;
    
////////// --- SYSTEM-WIDE ROLES
    @MarshallField(as="systemWideRoles",
				   whenXml=@MarshallFieldAsXml(collectionElementName="role"))
    @Getter @Setter protected Collection<SecurityRoleInModule> _systemWideRoles;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
    public SecurityPrincipalBase() {
    	// default no-args constructor
    }
    public SecurityPrincipalBase(final SecurityPrincipalOID oid) {
		_oid = oid;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public <P extends IsSecurityPrincipal> boolean hasDifferentDataTo(final P other) {
		if (other == null) return false;

		if (this.getClass() != other.getClass()) throw new IllegalArgumentException(this.getClass() + " cannot be updated from " + other.getClass());

		if (this.getOid() != null && other.getOid() != null
		 && this.getOid().isNOT(other.getOid())) throw new IllegalArgumentException("Cannot update diferent [security principal] objects!!");
		if (this.getId() != null && other.getId() != null
		 && this.getId().isNOT(other.getId())) throw new IllegalArgumentException("Cannot update diferent [security principal] objects!!");		
		
		if (Objects.areNOTEqual(_sourceIdentityProvider,other.getSourceIdentityProvider(),
							    IdentityProvider::equals)) throw new IllegalArgumentException("Cannot update diferent [security principal] objects!!");
		
		return Objects.areNOTEqual(_status,other.getStatus(),
							       SecurityPrincipalStatus::equals);
	}
	@Override
	public <P extends IsSecurityPrincipal> void updateFrom(final P other) {
		if (!this.hasDifferentDataTo(other)) return;

		_status = other.getStatus();
		
		_sourceIdentityProvider = other.getSourceIdentityProvider();
	}
}
