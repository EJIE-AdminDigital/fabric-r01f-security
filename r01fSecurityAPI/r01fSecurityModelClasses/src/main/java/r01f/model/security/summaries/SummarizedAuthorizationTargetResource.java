package r01f.model.security.summaries;

import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.facets.HasOID;
import r01f.facets.LangInDependentNamed;
import r01f.model.SummarizedModelObject;
import r01f.model.security.authorization.AuthorizationTargetResource;
import r01f.model.security.authorization.HasAuthorizationTargetResourceSpec;
import r01f.model.security.authorization.IsAuthorizationResourcesData;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.oids.OID;
import r01f.oids.SecurityIDs.SecurityRole;
import r01f.oids.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.types.SerializedData;
import r01f.util.types.collections.CollectionUtils;
import r01f.util.types.collections.Lists;

@Accessors(prefix="_")
public class SummarizedAuthorizationTargetResource
  implements HasAuthorizationTargetResourceSpec,
		 	 SummarizedModelObject<AuthorizationTargetResource>,
		 	 HasOID<AuthorizationTargetResourceOID>,
		 	 LangInDependentNamed {

	private static final long serialVersionUID = -8927180990281716970L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	@MarshallField(as="oid",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private AuthorizationTargetResourceOID _oid;
	
	@MarshallField(as="name",escape=true)
	@Getter @Setter private String _name;
	
	@MarshallField(as="authsCountByRole",
				   whenXml=@MarshallFieldAsXml(collectionElementName="withRole"))
	@Getter @Setter private Collection<AuthorizationsWithRoleCount> _authsCountByRole;
	
	/**
	 * Encodes the [target resources] in serialized data.
	 * It can be just the oid of the [target resource], a pattern (like *) or anything
	 * It's up to the business to give "meaning" to this field 
	 */
	@MarshallField(as="resources")
	@Getter @Setter protected SerializedData<IsAuthorizationResourcesData> _resources;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public SummarizedAuthorizationTargetResource(final AuthorizationTargetResource auth) {
		_oid = auth.getOid();
		_name = auth.getName();
		_resources = auth.getResources();
	}
	public SummarizedAuthorizationTargetResource(final AuthorizationTargetResource auth,final AuthorizationsWithRoleCount... authsCountByRole) {
		this(auth,
			 authsCountByRole != null ? Lists.newArrayList(authsCountByRole) : null);
	}
	public SummarizedAuthorizationTargetResource(final AuthorizationTargetResource auth,final Collection<AuthorizationsWithRoleCount> authsCountByRole) {
		this(auth);
		_authsCountByRole = authsCountByRole;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public long getAuthorizationsWithRoleCount(final SecurityRole aRole) {
		if (CollectionUtils.isNullOrEmpty(_authsCountByRole)) return 0;
		AuthorizationsWithRoleCount withRoleCount = _authsCountByRole.stream()
																     .filter(withRole -> withRole.getRole().is(aRole))
																     .findFirst().orElse(null);
		return withRoleCount != null ? withRoleCount.getCount()
									 : 0;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public Class<AuthorizationTargetResource> getModelObjectType() {
		return AuthorizationTargetResource.class;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	OID
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public void unsafeSetOid(final OID oid) {
		_oid = (AuthorizationTargetResourceOID)oid;
	}
}
