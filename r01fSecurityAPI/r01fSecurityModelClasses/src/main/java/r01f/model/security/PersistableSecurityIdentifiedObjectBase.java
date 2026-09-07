package r01f.model.security;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.aspects.interfaces.dirtytrack.ConvertToDirtyStateTrackable;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.securitycontext.SecurityIDS.IsSecurityObjectID;
import r01f.securitycontext.SecurityOIDs.IsSecurityObjectOID;

@ConvertToDirtyStateTrackable			// changes in state are tracked
@Accessors(prefix="_")
public abstract class PersistableSecurityIdentifiedObjectBase<O extends IsSecurityObjectOID,
															  ID extends IsSecurityObjectID,
															  SELF_TYPE extends PersistableSecurityIdentifiedObjectBase<O,ID,SELF_TYPE>>
			  extends PersistableSecurityObjectBase<O,SELF_TYPE>
		   implements PersistableSecurityIdentifiedObject<O,ID> {

	private static final long serialVersionUID = -3535852608223022810L;
/////////////////////////////////////////////////////////////////////////////////////////
//  ID FIELD
/////////////////////////////////////////////////////////////////////////////////////////

	@MarshallField(as="id",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter protected ID _id;
}
