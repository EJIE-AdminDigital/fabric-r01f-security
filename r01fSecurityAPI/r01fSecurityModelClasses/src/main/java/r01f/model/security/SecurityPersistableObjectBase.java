package r01f.model.security;

import lombok.experimental.Accessors;
import r01f.aspects.interfaces.dirtytrack.ConvertToDirtyStateTrackable;
import r01f.model.PersistableModelObjectBase;
import r01f.oids.SecurityOIDs.IsSecurityPersistableObjectOID;

@ConvertToDirtyStateTrackable
@Accessors(prefix="_")
public abstract class SecurityPersistableObjectBase<O extends IsSecurityPersistableObjectOID,
												    SELF_TYPE extends SecurityPersistableObjectBase<O,SELF_TYPE>>
			  extends PersistableModelObjectBase<O,SELF_TYPE>
		   implements IsSecurityPersistableObject<O> {

	private static final long serialVersionUID = -432678024812158368L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public SecurityPersistableObjectBase() {
		super();
	}
	public SecurityPersistableObjectBase(final O oid) {
		super(oid);
	}

}
