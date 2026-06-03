package r01f.model.security.user;

import java.util.Objects;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.aspects.interfaces.dirtytrack.ConvertToDirtyStateTrackable;
import r01f.debug.Debuggable;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.model.ModelObject;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallFrom;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.securitycontext.SecurityIDS.UserRole;
import r01f.types.CanBeRepresentedAsString;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;

@MarshallType(as="userRoleInModule")
@ConvertToDirtyStateTrackable
@Accessors(prefix="_")
public class UserRoleInModule 
  implements ModelObject,
  			 CanBeRepresentedAsString,
  			 Debuggable {
	
	private static final long serialVersionUID = -94343450693496177L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELD
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="appComponent",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter private final AppComponent _appComponent;
	
	@MarshallField(as="role",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter private final UserRole _role;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public UserRoleInModule(@MarshallFrom("appComponent") final AppComponent component,@MarshallFrom("role") final UserRole role) {
		_appComponent = component;
		_role = role;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public String asString() {
		return Strings.customized("{}:{}",
								  _appComponent,_role);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	IS
/////////////////////////////////////////////////////////////////////////////////////////
	public boolean is(final AppComponent appComp,final UserRole role) {
		return _appComponent.is(appComp)
			&& _role.is(role);
	}
	public boolean is(final AppComponent appComp) {
		return _appComponent.is(appComp);
	}
	public boolean is(final UserRoleInModule userRole) {
		return this.equals(userRole);
	}
	public boolean isNOT(final UserRoleInModule userRole) {
		return !this.is(userRole);
	}
	public boolean isContainedIn(final UserRoleInModule... userRoles) {
		if (CollectionUtils.isNullOrEmpty(userRoles)) return false;
		return Stream.of(userRoles)
					 .anyMatch(userRole -> userRole.is(this));
	}
	public boolean isNOTContainedIn(final UserRoleInModule... userRoles) {
		return !this.isContainedIn(userRoles);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EQUALS & HASHCODE
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		if (!(obj instanceof UserRoleInModule)) return false;
		
		UserRoleInModule other = (UserRoleInModule)obj;
		boolean moduleEq = _appComponent != null ? _appComponent.is(other.getAppComponent())
												 : other.getAppComponent() != null ? false
														 						   : true;	// both null
		boolean roleEq = _role != null ? _role.is(other.getRole())
									   : other.getRole() != null ? false
											   					 : true;	// both null
		return moduleEq && roleEq;
	}
	@Override
	public int hashCode() {
		return Objects.hash(_appComponent,_role);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	DEBUG
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public CharSequence debugInfo() {
		return this.asString();
	}
}
