/**
 *
 */
package r01f.model.security.user;

import java.util.Collection;

import r01f.facets.HasLangIndependentDisplayName;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.locale.Language;
import r01f.model.security.PersistableSecurityObject;
import r01f.securitycontext.SecurityIDS.UserRole;
import r01f.securitycontext.SecurityOIDs.UserOID;

public interface User
		 extends PersistableSecurityObject<UserOID>,
		 		 HasLangIndependentDisplayName {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
////////// Status
	public UserStatus getStatus();
	public void setStatus(final UserStatus userStatus);
////////// System wide roles
	public void setUserSystemWideRoles(Collection<UserRoleInModule> roles);
	public Collection<UserRoleInModule> getUserSystemWideRoles();
	public boolean hasAnyUserSystemWideRoleIn(final AppComponent module);
	public boolean hasUserSystemWideRoleIn(final AppComponent module,final UserRole userRole);
	public Collection<UserRole> getUserSystemWideRolesIn(final AppComponent module);
	public boolean addUserSystemWideRoleInModule(final AppComponent module,final UserRole role);
	public boolean addUserSystemWideRoleInModule(final UserRoleInModule roleInModule);
	public boolean removeUserSystemWideRoleIn(final AppComponent module,final UserRole role);
	public boolean removeUserSystemWideRolesIn(final AppComponent module);
////////// Preferred language
	public Language getPrefLang();
	public void setPrefLang(final Language lang);
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Copies the runtime data from the given user
	 * @param <U>
	 * @param other
	 */
	public <U extends User> void copyRuntimeDataFrom(final U other);
	/**
	 * Updates the user data from the given one
	 * returns true if any field was updated; false otherwise
	 * @param other
	 * @return
	 */
	public <U extends User> boolean updateFrom(final U other);
}
