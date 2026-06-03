package r01f.model.security.user;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.aspects.interfaces.dirtytrack.ConvertToDirtyStateTrackable;
import r01f.comparable.ComparableObject;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.locale.Language;
import r01f.model.metadata.annotations.ModelObjectData;
import r01f.model.security.PersistableSecurityObjectBase;
import r01f.model.security.metadata.MetaDataForSecurityObjectBase;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallIgnoredField;
import r01f.securitycontext.SecurityIDS.UserRole;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.util.comparable.ComparableObjectUtils;
import r01f.util.types.collections.CollectionUtils;
import r01f.util.types.collections.Lists;

/**
 * Base type for every user object
 * @param <SELF_TYPE>
 */
@ModelObjectData(MetaDataForSecurityObjectBase.class)
@ConvertToDirtyStateTrackable
@Accessors(prefix="_")
public abstract class UserBase<SELF_TYPE extends UserBase<SELF_TYPE>>
			  extends PersistableSecurityObjectBase<UserOID,SELF_TYPE>
		   implements ComparableObject<SELF_TYPE>,
		   			  User {

	private static final long serialVersionUID = 8628625324366860597L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Display data
	 */
	@MarshallField(as="displayName",escape=true)
	@Getter @Setter protected String _displayName;
	/**
	 * User status: active, pending approval, etc
	 */
	@MarshallField(as="status",
			   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter	@Setter	protected UserStatus _status = UserStatus.ACTIVE;
	/**
	 * User system-wide role
	 * DO NO confuse with the user's role on a resource!!!
	 * This a general role like admin, master, or whatever
	 */
	@MarshallField(as="userSystemWideRole",
				   whenXml=@MarshallFieldAsXml(collectionElementName="role"))
	@Getter @Setter protected Collection<UserRoleInModule> _userSystemWideRoles;
	/**
	 * The preferred language
	 */
	@MarshallField(as="prefLang",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter protected Language _prefLang = Language.ENGLISH;
/////////////////////////////////////////////////////////////////////////////////////////
//	RUNTIME TRANSIENT DATA (not serializable fields)
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * A convenient collection of run-time data
	 * This data is NOT serialized when the object is persisted!
	 */
	@MarshallIgnoredField	// DO NOT SERIALIZE
	@Getter protected transient Collection<UserRuntimeData<?>> _runtimeData;

	@Accessors(prefix="_")
	@RequiredArgsConstructor
	private class UserRuntimeData<T> {
		@Getter private final String _id;
		@Getter private final T _data;
	}

	@SuppressWarnings("unchecked")
	public <T> SELF_TYPE addRuntimeData(final UserRuntimeData<T> runTimeData) {
		this.removeRuntimeDataWithId(runTimeData.getId());	// ensure there DOES NOT exist two entries with the same ID

		if (_runtimeData == null) _runtimeData = Lists.newArrayList();
		_runtimeData.add(runTimeData);
		return (SELF_TYPE)this;
	}
	public <T> SELF_TYPE addRuntimeData(final String id,final T runTimeData) {
		return this.addRuntimeData(new UserRuntimeData<>(id,runTimeData));
	}
	public boolean removeRuntimeDataWithId(final String id) {
		if (!this.hasRuntimeData() || !this.containsRuntimeDataWithId(id)) return false;

		UserRuntimeData<?> prev = _getRuntimeDataWithId(id);
		if (prev != null) _runtimeData.remove(prev);
		return prev != null;
	}
	public boolean hasRuntimeData() {
		return CollectionUtils.hasData(_runtimeData);
	}
	public boolean containsRuntimeDataWithId(final String id) {
		return this.getRuntimeDataWithId(id) != null;
	}
	@SuppressWarnings("unchecked")
	public <T> T getRuntimeDataWithId(final String id) {
		if (id == null || CollectionUtils.isNullOrEmpty(_runtimeData)) return null;
		return _runtimeData.stream()
						   .filter(_createUserRuntimeDataByIdFilter(id))
						   .map(data -> (T)data.getData())
						   .findFirst().orElse(null);
	}
	private UserRuntimeData<?> _getRuntimeDataWithId(final String id) {
		if (id == null || CollectionUtils.isNullOrEmpty(_runtimeData)) return null;
		return _runtimeData.stream()
						   .filter(_createUserRuntimeDataByIdFilter(id))
						   .findFirst().orElse(null);
	}
	@Override @SuppressWarnings("unchecked")
	public <U extends User> void copyRuntimeDataFrom(final U user) {
		SELF_TYPE other = (SELF_TYPE)user;
		if (!other.hasRuntimeData()) return;

		Collection<UserRuntimeData<?>> otherRuntimeData = other.getRuntimeData();
		for (UserRuntimeData<?> otherRuntimeDataItem : otherRuntimeData) {
			this.addRuntimeData(otherRuntimeDataItem);
		}
	}
	private Predicate<UserRuntimeData<?>> _createUserRuntimeDataByIdFilter(final String id) {
		return data -> data.getData() != null
					&& id.equals(data.getId());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	PREF LANG OR DEFAULT
/////////////////////////////////////////////////////////////////////////////////////////
	public Language getPrefLangOrDefault(final Language def) {
		Language prefLang = this.getPrefLang();
		return prefLang != null ? prefLang : def;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	ROLES
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean hasAnyUserSystemWideRoleIn(final AppComponent module) {
		return CollectionUtils.hasData(_userSystemWideRoles) ? _userSystemWideRoles.stream()
																				   .anyMatch(role -> role.getAppComponent().is(module))
															 : false;
	}
	@Override
	public boolean hasUserSystemWideRoleIn(final AppComponent module,final UserRole userRole) {
		return CollectionUtils.hasData(_userSystemWideRoles) ? _userSystemWideRoles.stream()
																				   .anyMatch(role -> role.getAppComponent().is(module)
																						   		  && role.getRole() != null
																						   		  && role.getRole().is(userRole))
															 : false;
	}
	@Override
	public boolean addUserSystemWideRoleInModule(final AppComponent module,final UserRole role) {
		return this.addUserSystemWideRoleInModule(new UserRoleInModule(module,role));
	}
	@Override
	public boolean addUserSystemWideRoleInModule(final UserRoleInModule roleInModule) {
		if (roleInModule == null) return false;
		if (_userSystemWideRoles == null) _userSystemWideRoles = Lists.newArrayList();
		return !_userSystemWideRoles.contains(roleInModule)
						? _userSystemWideRoles.add(roleInModule)
						: false;
	}
	@Override
	public Collection<UserRole> getUserSystemWideRolesIn(final AppComponent module) {
		Collection<UserRoleInModule> rolesInModule = _userSystemWideRolesIn(module);
		return CollectionUtils.hasData(rolesInModule)
					? rolesInModule.stream()
								   .map(UserRoleInModule::getRole)
								   .collect(Collectors.toList())
					: null;
	}
	private Collection<UserRoleInModule> _userSystemWideRolesIn(final AppComponent module) {
		return CollectionUtils.hasData(_userSystemWideRoles) ? _userSystemWideRoles.stream()
																				   .filter(role -> role.getAppComponent().is(module))
																				   .collect(Collectors.toList())
															 : null;
	}
	@Override
	public boolean removeUserSystemWideRolesIn(final AppComponent module) {
		Collection<UserRoleInModule> rolesIn = _userSystemWideRolesIn(module);
		return rolesIn != null ? _userSystemWideRoles.removeAll(rolesIn) : false;
	}
	@Override
	public boolean removeUserSystemWideRoleIn(final AppComponent module,final UserRole role) {
		return this.hasUserSystemWideRoleIn(module,role)
					? _userSystemWideRoles.remove(new UserRoleInModule(module,role))
					: false;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public <U extends User> boolean updateFrom(final U other) {
		if (other == null) return false;

		if (this.getClass() != other.getClass()) throw new IllegalArgumentException(this.getClass() + " cannot be updated from " + other.getClass());

		if (this.getOid() != null && other.getOid() != null
		 && this.getOid().isNOT(other.getOid())) throw new IllegalArgumentException("Cannot update diferent user objects!!");

		// TODO update and compare person data
		return false;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public boolean isSameObject(final SELF_TYPE  other) {
		return  ComparableObjectUtils.compare(_status, other.getStatus())
				&&
				ComparableObjectUtils.compare(_prefLang,other.getPrefLang());

	}
}
