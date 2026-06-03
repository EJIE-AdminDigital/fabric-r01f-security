package r01f.model.security.authorization;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.aspects.interfaces.dirtytrack.ConvertToDirtyStateTrackable;
import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.locale.LanguageTexts;
import r01f.model.security.PersistableSecurityObjectBase;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;

@ConvertToDirtyStateTrackable
@Accessors(prefix="_")
@NoArgsConstructor
public abstract class AuthorizationTargetResourceBase<SELF_TYPE extends AuthorizationTargetResourceBase<SELF_TYPE>>
			  extends PersistableSecurityObjectBase<AuthorizationTargetResourceOID,
			  										SELF_TYPE>
		   implements AuthorizationTargetResource {

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
	 * Encodes the [target resources] in an string
	 * It can be just the oid of the [target resource], a pattern (like *) or anything
	 * It's up to the user to give "meaning" to this field 
	 */
	@MarshallField(as="resources",escape=true)
	@Getter @Setter private String _resources;
}
