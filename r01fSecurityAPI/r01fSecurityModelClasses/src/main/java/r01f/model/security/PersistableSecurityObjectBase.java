package r01f.model.security;

import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import r01f.aspects.interfaces.dirtytrack.ConvertToDirtyStateTrackable;
import r01f.model.IndexableModelObject;
import r01f.model.PersistableModelObjectBase;
import r01f.securitycontext.SecurityOIDs.SecurityObjectOID;

@ConvertToDirtyStateTrackable
@NoArgsConstructor
@Accessors(prefix="_")
public abstract class PersistableSecurityObjectBase<O extends SecurityObjectOID,
												    SELF_TYPE extends PersistableSecurityObjectBase<O,SELF_TYPE>>
			  extends PersistableModelObjectBase<O,SELF_TYPE>
		   implements PersistableSecurityObject<O>,			// is persistable
					  IndexableModelObject {

	private static final long serialVersionUID = -432678024812158368L;



}
