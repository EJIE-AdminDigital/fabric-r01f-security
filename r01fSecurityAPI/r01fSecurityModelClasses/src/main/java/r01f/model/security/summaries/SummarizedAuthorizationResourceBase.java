package r01f.model.security.summaries;

import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.guids.OID;
import r01f.model.security.authorization.AuthorizationTargetResource;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.securitycontext.SecurityIDS.UserRole;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.util.types.collections.CollectionUtils;
import r01f.util.types.collections.Lists;

@Accessors(prefix="_")
public abstract class SummarizedAuthorizationResourceBase<A extends AuthorizationTargetResource>
		   implements SummarizedAuthorizationResource<A> {

	private static final long serialVersionUID = -8927180990281716970L;
/////////////////////////////////////////////////////////////////////////////////////////
//  NON SERIALIZABLE FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final transient Class<A> _modelObjectType;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	@MarshallField(as="oid",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private AuthorizationTargetResourceOID _oid;
	
	@MarshallField(as="name",escape=true)
	@Getter @Setter private String _name;
	
	@MarshallField(as="userAuthsCountByRole",
				   whenXml=@MarshallFieldAsXml(collectionElementName="withRole"))
	@Getter @Setter private Collection<UserAuthWithRoleCount> _userAuthsCountByRole;
	
	@MarshallField(as="resource",escape=true)
	@Getter @Setter private String _resources;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public SummarizedAuthorizationResourceBase(final Class<A> authType) {
		_modelObjectType = authType;
	}
	@SuppressWarnings("unchecked")
	public SummarizedAuthorizationResourceBase(final A auth) {
		this((Class<A>)auth.getClass());
		_oid = auth.getOid();
		_name = auth.getName();
		_resources = auth.getResources();
	}
	public SummarizedAuthorizationResourceBase(final A auth,final UserAuthWithRoleCount... userAuthsCountByRole) {
		this(auth,
			 userAuthsCountByRole != null ? Lists.newArrayList(userAuthsCountByRole) : null);
	}
	public SummarizedAuthorizationResourceBase(final A auth,final Collection<UserAuthWithRoleCount> userAuthsCountByRole) {
		this(auth);
		_userAuthsCountByRole = userAuthsCountByRole;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public long getUserAuthWithRoleCount(final UserRole userRole) {
		if (CollectionUtils.isNullOrEmpty(_userAuthsCountByRole)) return 0;
		UserAuthWithRoleCount withRoleCount = _userAuthsCountByRole.stream()
																   .filter(withRole -> withRole.getUserRole().is(userRole))
																   .findFirst().orElse(null);
		return withRoleCount != null ? withRoleCount.getCount()
									 : 0;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	OID
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public void unsafeSetOid(final OID oid) {
		_oid = (AuthorizationTargetResourceOID)oid;
	}
}
