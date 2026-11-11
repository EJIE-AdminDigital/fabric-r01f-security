package r01f.model.security.principal;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.aspects.interfaces.dirtytrack.ConvertToDirtyStateTrackable;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.IsSecurityPrincipalDataForPerson;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.oids.SecurityIDs.SecurityPrincipalID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;

@ConvertToDirtyStateTrackable
@Accessors(prefix="_")
public abstract class SecurityPrincipalForPersonBase<P extends IsSecurityPrincipalDataForPerson,
													 SELF_TYPE extends SecurityPrincipalForPersonBase<P,SELF_TYPE>> 
			  extends SecurityPrincipalBase<SELF_TYPE> 
		implements IsSecurityPrincipal {

	private static final long serialVersionUID = 5554617064863378215L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="personData")
	@Getter @Setter protected P _personData;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public SecurityPrincipalForPersonBase() {
		super();
	}
	public SecurityPrincipalForPersonBase(final SecurityPrincipalOID oid,final SecurityPrincipalID id) {
		super(oid);
		_id = id;
	}
	public SecurityPrincipalForPersonBase(final SecurityPrincipalOID oid,final SecurityPrincipalID id,
										  final P persondata) {
		this(oid,id);
		_personData = persondata;
	}
}
