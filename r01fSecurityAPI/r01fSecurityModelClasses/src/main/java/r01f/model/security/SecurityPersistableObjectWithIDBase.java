package r01f.model.security;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.aspects.interfaces.dirtytrack.ConvertToDirtyStateTrackable;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.oids.SecurityIDs.IsSecurityPersistableObjectID;
import r01f.oids.SecurityOIDs.IsSecurityPersistableObjectOID;

@ConvertToDirtyStateTrackable			// changes in state are tracked
@Accessors(prefix="_")
public abstract class SecurityPersistableObjectWithIDBase<O extends IsSecurityPersistableObjectOID,ID extends IsSecurityPersistableObjectID<O>,
														  SELF_TYPE extends SecurityPersistableObjectWithIDBase<O,ID,SELF_TYPE>>
			  extends SecurityPersistableObjectBase<O,SELF_TYPE>
		   implements IsSecurityPersistableObjectWithID<O,ID> {

	private static final long serialVersionUID = -3535852608223022810L;
/////////////////////////////////////////////////////////////////////////////////////////
//  ID FIELD
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="id",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter protected ID _id;
}
