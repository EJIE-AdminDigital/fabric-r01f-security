package r01f.model.security.authorization;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.aspects.interfaces.dirtytrack.ConvertToDirtyStateTrackable;
import r01f.locale.LanguageTexts;
import r01f.model.security.IsSecurityPersistableObject;
import r01f.model.security.SecurityPersistableObjectBase;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.oids.CommonOIDs.AppCode;
import r01f.oids.CommonOIDs.AppComponent;
import r01f.oids.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.types.SerializedData;

@ConvertToDirtyStateTrackable
@Accessors(prefix="_")
@NoArgsConstructor
public class AuthorizationTargetResource
     extends SecurityPersistableObjectBase<AuthorizationTargetResourceOID,
			  							   AuthorizationTargetResource> 
  implements HasAuthorizationTargetResourceSpec,
  			 IsSecurityPersistableObject<AuthorizationTargetResourceOID> {

	private static final long serialVersionUID = -1123012031053377001L;
/////////////////////////////////////////////////////////////////////////////////////////
//	APPCODE & COMPONENT                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="appCode",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private AppCode _appCode;
	
	@MarshallField(as="appComponnet",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private AppComponent _appComponent;
/////////////////////////////////////////////////////////////////////////////////////////
//	NAME & DESCRIPTION
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="name",escape=true)
	@Getter @Setter private String _name;
	
	@MarshallField(as="description",escape=true)
	@Getter @Setter private LanguageTexts _description;
/////////////////////////////////////////////////////////////////////////////////////////
//	RESOURCES
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Encodes the [target resources] in serialized data.
	 * It can be just the oid of the [target resource], a pattern (like *) or anything
	 * It's up to the business to give "meaning" to this field 
	 */
	@MarshallField(as="resources")
	@Getter @Setter protected SerializedData<IsAuthorizationResourcesData> _resources;
}
